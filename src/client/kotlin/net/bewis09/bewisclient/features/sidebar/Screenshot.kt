package net.bewis09.bewisclient.features.sidebar

import com.mojang.blaze3d.platform.NativeImage
import net.bewis09.bewisclient.common.*
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.Button
import net.bewis09.bewisclient.drawable.renderables.components.logic.Hoverable
import net.bewis09.bewisclient.drawable.renderables.components.structure.VerticalAlignScrollPlane
import net.bewis09.bewisclient.drawable.renderables.components.structure.VerticalScrollGrid
import net.bewis09.bewisclient.drawable.renderables.notification.NotificationManager
import net.bewis09.bewisclient.drawable.renderables.notification.SimpleTextNotification
import net.bewis09.bewisclient.drawable.renderables.popup.ConfirmPopup
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.drawer.pushColor
import net.bewis09.renderite.drawer.scale
import net.bewis09.renderite.drawer.transform
import net.bewis09.bewisclient.process.CopyImage
import net.bewis09.bewisclient.process.ProcessCreator
import net.bewis09.bewisclient.settings.structure.SidebarFeature
import net.bewis09.bewisclient.version.registerTexture
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.alpha
import net.minecraft.client.Minecraft
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds

object Screenshot : SidebarFeature(createIdentifier("bewisclient", "screenshot"), "Screenshots") {
    val redirect = boolean("redirect", true)

    val copyButtonText = createTranslation("copy", "Copy")
    val copyingButtonText = createTranslation("copying", "Copying...")
    val copySuccessNotifText = createTranslation("copy_screenshot_success", "Copied screenshot to clipboard")
    val copyFailedNotifText = createTranslation("copy_failed", "Copying Failed: Exit code %s")
    val deleteSuccessNotifText = createTranslation("delete_screenshot_success", "Deleted screenshot")
    val confirmDeletePopupText = createTranslation("confirm_delete", "Are you sure you want to delete this screenshot?")
    val deleteButtonText = createTranslation("delete", "Delete")
    val openButtonText = createTranslation("open", "Open")
    val openFolderButtonText = createTranslation("open_folder", "Open Folder")
    val deleteFailedNotifText = createTranslation("delete_failed", "Deleting failed")

    override fun getRenderable(): Renderable = ScreenshotElement

    val contents = mutableMapOf<File, ScreenshotFileData>()

    class ScreenshotFileData(val nativeImage: NativeImage?, val identifier: Identifier?, val failed: Boolean)

    object ScreenshotElement : Renderable() {
        val loading = createTranslation("loading", "Loading...")
        val loadingFailed = createTranslation("file_load_fail", "Failed to load file")

        val screenshotName = createTranslation("screenshot_name", "Screenshot: %s")
        val redirectElement = redirect.createRenderable(this@Screenshot, "redirect", "Redirect screenshot chat click event", "When clicking the screenshot name in chat, the screenshot opens in the in-game screen instead of an external program.").addToQuickSettings(this@Screenshot, "click")

        val noScreenshotsYet = createTranslation("no_screenshots_yet", "Taken screenshots will appear here.")

        fun load() {
            Util.ioPool().execute {
                val screenshotDir = client.gameDirectory.toPath().resolve("screenshots")
                val screenshotFile = screenshotDir.toFile()

                while (true) {
                    catch {
                        if (screenshotFile.exists() && screenshotFile.isDirectory) {
                            screenshotFile.listFiles()?.filter { it.isFile && (it.extension == "png") }?.sortedBy { it.name }?.apply {
                                this.forEach {
                                    if (!contents.containsKey(it))
                                        contents[it] = ScreenshotFileData(null, null, false)
                                }
                                resize()
                            }?.forEach(::loadFileData)
                        }

                        val service = FileSystems.getDefault().newWatchService()

                        screenshotDir.register(
                            service,
                            StandardWatchEventKinds.ENTRY_CREATE,
                            StandardWatchEventKinds.ENTRY_MODIFY,
                            StandardWatchEventKinds.ENTRY_DELETE,
                        )

                        while (true) {
                            val key = service.take()
                            val events = key.pollEvents()

                            for (event in events) {
                                val kind = event.kind()
                                val fileName = event.context() as? Path ?: continue
                                val file = screenshotDir.resolve(fileName).toFile()
                                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                                    if (file.isFile && (file.extension == "png") && !contents.containsKey(file)) {
                                        contents[file] = ScreenshotFileData(null, null, false)
                                        resize()
                                        loadFileData(file)
                                    }
                                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                                    if (file.isFile && (file.extension == "png")) {
                                        contents[file] = ScreenshotFileData(null, null, false)
                                        resize()
                                        loadFileData(file)
                                    }
                                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                                    if (contents.containsKey(file)) {
                                        contents.remove(file)
                                        resize()
                                    }
                                }
                            }
                            key.reset()
                        }
                    }

                    Thread.sleep(1000)
                }
            }
        }

        val elementGrid by lazy {
            load()
            VerticalScrollGrid({ width ->
                contents.toSortedMap().map { ScreenshotViewElement(it.key) }.toList().let {
                    it.ifEmpty {
                        listOf(
                            object : Renderable() {
                                override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
                                    screenDrawing.fillWithBorder(x, y, width, height, if (isMinecrafty) 0x333333 alpha 0.7f else General.getThemeColor(alpha = 0.7f, black = 0.2f), if (isMinecrafty) Color.WHITE alpha 0.5f else General.getThemeColor(alpha = 0.5f))
                                    val lines = screenDrawing.wrapText(noScreenshotsYet().string, width - 8)
                                    screenDrawing.drawCenteredWrappedText(lines, x + width / 2, y + height / 2 - lines.size * screenDrawing.getTextHeight() / 2, if (isMinecrafty) Color.WHITE alpha 0.7f else General.getThemeColor(white = 0.3f, alpha = 0.7f))
                                }
                            }.setHeight((width - 2) * 9 / 16 + 2)
                        )
                    }
                }
            }, 5, 100)
        }

        fun loadFileData(file: File) {
            if (contents[file]?.nativeImage != null || contents[file]?.failed == true) return
            contents[file] = catch {
                val data = file.readBytes()
                return@catch if (data.size < 8)
                    ScreenshotFileData(null, null, false)
                else
                    ScreenshotFileData(NativeImage.read(ByteArrayInputStream(data)), null, false)
            } ?: ScreenshotFileData(null, null, true)
        }

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            internalHeight = height.coerceAtLeast(27)
            addRenderable(redirectElement(x, y, width, 22))
            addRenderable(elementGrid(x, y + 27, width, height - 27))
        }
    }

    class ScreenshotViewElement(val file: File) : Hoverable(100) {
        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            super.render(screenDrawing, mouseX, mouseY)

            screenDrawing.transform(x + width / 2f, y + height / 2f, if (isMinecrafty) 1f else 1f - hoverFactor * 0.1f) {
                if (isMinecrafty) {
                    SelectiveScreenDrawer.renderButtonBackground(screenDrawing, hoverFactor, 1f, -width / 2, -height / 2, width, height, 1f, mouseX, mouseY)
                } else {
                    screenDrawing.fillWithBorder(-width / 2, -height / 2, width, height, General.getThemeColor(alpha = 0.7f, black = 0.2f), General.getThemeColor(white = 1f - hoverFactor * .5f, alpha = 0.5f + hoverFactor * .5f))
                }

                val data = contents.getOrDefault(file, null) ?: return

                data.identifier?.also {
                    val nativeImage = data.nativeImage ?: return@also

                    val aspectRatio = nativeImage.width.toFloat() / nativeImage.height.toFloat()

                    val imgHeight = ((width - 2) * (1 / aspectRatio)).coerceAtMost(height - 2f)
                    val imgWidth = (imgHeight * aspectRatio).toInt()

                    val value = 1f - hoverFactor * 0.4f
                    screenDrawing.pushColor(if (isMinecrafty) 1f else value, if (isMinecrafty) 1f else value, if (isMinecrafty) 1f else value, if (isMinecrafty) 1f else value) {
                        screenDrawing.scale(if (isMinecrafty) (1 - 4f / imgHeight) else 1f, if (isMinecrafty) (1 - 4f / imgHeight) else 1f) {
                            screenDrawing.drawTexture(it, -imgWidth / 2, -imgHeight.toInt() / 2, imgWidth, imgHeight.toInt())
                            if (isMinecrafty) {
                                screenDrawing.fill(-imgWidth / 2, -imgHeight.toInt() / 2, imgWidth, imgHeight.toInt(), Color.WHITE alpha (0.2f * hoverFactor))
                            }
                        }
                    }
                } ?: run {
                    screenDrawing.drawCenteredText((data.failed then { ScreenshotElement.loadingFailed() }) ?: ScreenshotElement.loading(), 0, -5, Color.WHITE)
                    if (!data.failed && (data.nativeImage != null)) {
                        loadTexture(file, data.nativeImage)
                    }
                }
            }
        }

        override fun init() {
            internalHeight = (width - 2) * 9 / 16 + 2
        }

        override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
            if (button == 0) {
                openBigScreenshot(file)
                return true
            }
            return false
        }
    }

    fun openBigScreenshotNewScreen(file: File) {
        if (!file.exists()) return

        if (!contents.containsKey(file)) {
            Util.ioPool().execute {
                contents[file] = ScreenshotFileData(null, null, false)
                ScreenshotElement.loadFileData(file)
            }
        }

        OptionScreen().apply {
            changeCategory(Screenshot, instant = true)
            openBigScreenshot(file, this, true)
            setRenderableScreen(this)
        }
    }

    fun openBigScreenshot(file: File, screen: OptionScreen? = OptionScreen.currentInstance, instant: Boolean = false) {
        screen?.openPage(
            ScreenshotElement.screenshotName(file.name),
            VerticalAlignScrollPlane({ w ->
                listOf(
                    BigScreenshotViewElement(file).setWidth(w)
                )
            }, 5),
            instant = instant
        )
    }

    fun loadTexture(file: File, nativeImage: NativeImage) {
        createIdentifier("bewisclient", "screenshot/${file.nameWithoutExtension}_" + (Math.random() * 0x10000).toInt().toString(16)).also {
            try {
                Minecraft.getInstance().registerTexture(it, nativeImage)
                contents[file] = ScreenshotFileData(nativeImage, it, false)
            } catch (e: Exception) {
                contents[file] = ScreenshotFileData(null, null, true)
                e.printStackTrace()
            }
        }
    }

    class BigScreenshotViewElement(val file: File) : Renderable() {
        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            if (isMinecrafty) {
                SelectiveScreenDrawer.renderButtonBackground(screenDrawing, 0f, 0f, x, y, width, height - SelectiveScreenDrawer.getSideButtonHeight() - 5, 1f, mouseX, mouseY)
            } else {
                screenDrawing.fillWithBorder(x, y, width, height - SelectiveScreenDrawer.getSideButtonHeight() - 5, if (isMinecrafty) Color.BLACK alpha 0.7f else General.getThemeColor(black = 0.2f, alpha = 0.7f), if (isMinecrafty) Color.WHITE alpha 0.5f else General.getThemeColor(alpha = 0.5f))
            }

            val data = contents.getOrDefault(file, null) ?: return

            data.identifier?.also {
                val nativeImage = data.nativeImage ?: return@also

                val aspectRatio = nativeImage.width.toFloat() / nativeImage.height.toFloat()

                val imgHeight = ((width - if (isMinecrafty) 6 else 2) * (1 / aspectRatio)).coerceAtMost(height - if (isMinecrafty) 29f else 21f)
                val imgWidth = (imgHeight * aspectRatio).toInt()

                screenDrawing.drawTexture(it, (x + width / 2 - imgWidth / 2), (y + (height - SelectiveScreenDrawer.getSideButtonHeight() - 5) / 2 - imgHeight.toInt() / 2), imgWidth, imgHeight.toInt())
            } ?: run {
                screenDrawing.drawCenteredText((data.failed then { ScreenshotElement.loadingFailed() }) ?: ScreenshotElement.loading(), x + width / 2, y + (height - 19) / 2 - 5, Color.WHITE)
                if (!data.failed && (data.nativeImage != null)) {
                    loadTexture(file, data.nativeImage)
                }
            }

            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            internalHeight = (width - 2) * 9 / 16 + SelectiveScreenDrawer.getSideButtonHeight() + 7

            addRenderable(Button(openButtonText()) {
                Util.getPlatform().openFile(file)
            }(x, y + height - SelectiveScreenDrawer.getSideButtonHeight(), (width - 15) / 4, SelectiveScreenDrawer.getSideButtonHeight()))
            addRenderable(Button(openFolderButtonText()) {
                Util.getPlatform().openFile(file.parentFile)
            }(x + (width - 15) / 4 + 5, y + height - SelectiveScreenDrawer.getSideButtonHeight(), (width - 15) / 4, SelectiveScreenDrawer.getSideButtonHeight()))
            addRenderable(Button(copyButtonText()) { button ->
                button.text = copyingButtonText()
                ProcessCreator.create(CopyImage::class.java, file.path) {
                    if (it != 0) {
                        println("Failed to copy image to clipboard, exit code: $it")
                        NotificationManager.addNotification(SimpleTextNotification(copyFailedNotifText(it)))
                        button.text = copyButtonText()
                    } else {
                        NotificationManager.addNotification(SimpleTextNotification(copySuccessNotifText()))
                        button.text = copyButtonText()
                    }
                }
            }(x + width - 2 * (width - 15) / 4 - 5, y + height - SelectiveScreenDrawer.getSideButtonHeight(), (width - 15) / 4, SelectiveScreenDrawer.getSideButtonHeight()))
            addRenderable(Button(deleteButtonText()) {
                OptionScreen.currentInstance?.openPopup(ConfirmPopup(confirmDeletePopupText(), {
                    if (catch { file.delete() } == true) {
                        NotificationManager.addNotification(SimpleTextNotification(deleteSuccessNotifText()))
                    } else {
                        NotificationManager.addNotification(SimpleTextNotification(deleteFailedNotifText()))
                    }
                    OptionScreen.currentInstance?.goBack()
                }))
            }(x + width - (width - 15) / 4, y + height - SelectiveScreenDrawer.getSideButtonHeight(), (width - 15) / 4, SelectiveScreenDrawer.getSideButtonHeight()))
        }
    }
}