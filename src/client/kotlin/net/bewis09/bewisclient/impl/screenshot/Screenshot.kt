package net.bewis09.bewisclient.impl.screenshot

import net.bewis09.bewisclient.core.registerTexture
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.Translations
import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.renderables.options_structure.SidebarCategory
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.transform
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.screenshot.ScreenshotElement.loading
import net.bewis09.bewisclient.impl.screenshot.ScreenshotElement.loadingFailed
import net.bewis09.bewisclient.impl.screenshot.ScreenshotElement.screenshotName
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.impl.settings.functionalities.ScreenshotSettings
import net.bewis09.bewisclient.process.CopyImage
import net.bewis09.bewisclient.process.ProcessCreator
import net.bewis09.bewisclient.screen.RenderableScreen
import net.bewis09.bewisclient.util.catch
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.util.then
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds

val contents = mutableMapOf<File, ScreenshotFileData>()

class ScreenshotFileData(val nativeImage: NativeImage?, val identifier: Identifier?, val failed: Boolean)

object Screenshot : SidebarCategory(createIdentifier("bewisclient", "screenshots"), "Screenshots", ScreenshotElement)

object ScreenshotElement : Renderable() {
    val loading = Translation("menu.general.loading", "Loading...")
    val loadingFailed = Translation("menu.general.file_load_fail", "Failed to load file")

    val screenshotName = Translation("menu.general.screenshot_name", "Screenshot: %s")
    val redirectElement = ScreenshotSettings.redirect.createRenderable("screenshot.redirect", "Redirect screenshot chat click event", "When clicking the screenshot name in chat, the screenshot opens in the in-game screen instead of an external program.")

    val noScreenshotsYet = Translation("menu.screenshot.no_screenshots_yet", "Taken screenshots will appear here.")

    fun load() {
        Util.getIoWorkerExecutor().execute {
            val screenshotDir = client.runDirectory.toPath().resolve("screenshots").toFile()

            if (screenshotDir.exists() && screenshotDir.isDirectory) {
                screenshotDir.listFiles()?.filter { it.isFile && (it.extension == "png") }?.sortedBy { it.name }?.apply {
                    this.forEach {
                        if (!contents.containsKey(it))
                            contents[it] = ScreenshotFileData(null, null, false)
                    }
                    resize()
                }?.forEach(::loadFileData)
            }

            while (true) {
                catch {
                    val service = FileSystems.getDefault().newWatchService()

                    client.runDirectory.toPath().resolve("screenshots").register(
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
                            val file = screenshotDir.toPath().resolve(fileName).toFile()
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
                                screenDrawing.fillWithBorder(x, y, width, height, OptionsMenuSettings.getThemeColor(alpha = 0.7f, black = 0.2f), OptionsMenuSettings.getThemeColor(alpha = 0.5f))
                                val lines = screenDrawing.wrapText(noScreenshotsYet().string, width - 8)
                                screenDrawing.drawCenteredWrappedText(lines, x + width / 2, y + height / 2 - lines.size * screenDrawing.getTextHeight() / 2, OptionsMenuSettings.getThemeColor(white = 0.3f, alpha = 0.7f))
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

        screenDrawing.transform(x + width / 2f, y + height / 2f, 1f - hoverAnimation["hovering"] * 0.1f) {
            screenDrawing.fillWithBorder(-width / 2, -height / 2, width, height, OptionsMenuSettings.getThemeColor(alpha = 0.7f, black = 0.2f), OptionsMenuSettings.getThemeColor(white = 1f - hoverAnimation["hovering"] * .5f, alpha = 0.5f + hoverAnimation["hovering"] * .5f))
            val data = contents.getOrDefault(file, null) ?: return

            data.identifier?.also {
                val nativeImage = data.nativeImage ?: return@also

                val aspectRatio = nativeImage.width.toFloat() / nativeImage.height.toFloat()

                val imgHeight = ((width - 2) * (1 / aspectRatio)).coerceAtMost(height - 2f)
                val imgWidth = (imgHeight * aspectRatio).toInt()

                screenDrawing.pushColor(1f - hoverAnimation["hovering"] * 0.4f, 1f - hoverAnimation["hovering"] * 0.4f, 1f - hoverAnimation["hovering"] * 0.4f, 1f - hoverAnimation["hovering"] * 0.4f)
                screenDrawing.drawTexture(it, -imgWidth / 2, -imgHeight.toInt() / 2, imgWidth, imgHeight.toInt())
                screenDrawing.popColor()
            } ?: run {
                screenDrawing.drawCenteredText((data.failed then { loadingFailed() }) ?: loading(), 0, -5, Color.WHITE)
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
        Util.getIoWorkerExecutor().execute {
            contents[file] = ScreenshotFileData(null, null, false)
            ScreenshotElement.loadFileData(file)
        }
    }

    MinecraftClient.getInstance().setScreen(RenderableScreen(OptionScreen().apply {
        page = OptionScreen.Page(
            Plane { x, y, width, _ -> listOf(TextElement(screenshotName(file.name), OptionsMenuSettings.getTextThemeColor(), centered = true)(x, y, width, 13)) }.setHeight(14), VerticalAlignScrollPlane({ w ->
                listOf(
                    BigScreenshotViewElement(file).setWidth(w)
                )
            }, 5),
            null
        )
        category.value = Screenshot.id.toString()
    }))
}

fun openBigScreenshot(file: File) {
    OptionScreen.currentInstance?.openPage(
        Plane { x, y, width, _ -> listOf(TextElement(screenshotName(file.name), OptionsMenuSettings.getTextThemeColor(), centered = true)(x, y, width, 13)) }.setHeight(14),
        VerticalAlignScrollPlane({ w ->
            listOf(
                BigScreenshotViewElement(file).setWidth(w)
            )
        }, 5),
        null
    )
}

fun loadTexture(file: File, nativeImage: NativeImage) {
    createIdentifier("bewisclient", "screenshot/${file.nameWithoutExtension}_" + (Math.random() * 0x10000).toInt().toString(16)).also {
        try {
            MinecraftClient.getInstance().registerTexture(it, nativeImage)
            contents[file] = ScreenshotFileData(nativeImage, it, false)
        } catch (e: Exception) {
            contents[file] = ScreenshotFileData(null, null, true)
            e.printStackTrace()
        }
    }
}

class BigScreenshotViewElement(val file: File) : Renderable() {
    var deleteRequest = false

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.fillWithBorder(x, y, width, height - 19, OptionsMenuSettings.getThemeColor(black = 0.2f, alpha = 0.7f), OptionsMenuSettings.getThemeColor(alpha = 0.5f))

        val data = contents.getOrDefault(file, null) ?: return

        data.identifier?.also {
            val nativeImage = data.nativeImage ?: return@also

            val aspectRatio = nativeImage.width.toFloat() / nativeImage.height.toFloat()

            val imgHeight = ((width - 2) * (1 / aspectRatio)).coerceAtMost(height - 21f)
            val imgWidth = (imgHeight * aspectRatio).toInt()

            screenDrawing.drawTexture(it, (x + width / 2 - imgWidth / 2), (y + (height - 19) / 2 - imgHeight.toInt() / 2), imgWidth, imgHeight.toInt())
        } ?: run {
            screenDrawing.drawCenteredText((data.failed then { loadingFailed() }) ?: loading(), x + width / 2, y + (height - 19) / 2 - 5, Color.WHITE)
            if (!data.failed && (data.nativeImage != null)) {
                loadTexture(file, data.nativeImage)
            }
        }

        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        internalHeight = (width - 2) * 9 / 16 + 21

        addRenderable(Button(Translations.OPEN()) {
            Util.getOperatingSystem().open(file)
        }(x, y + height - 14, (width - 15) / 4, 14))
        addRenderable(Button(Translations.OPEN_FOLDER()) {
            Util.getOperatingSystem().open(file.parentFile)
        }(x + (width - 15) / 4 + 5, y + height - 14, (width - 15) / 4, 14))
        addRenderable(Button(Translations.COPY()) { button ->
            button.text = Translations.COPYING()
            ProcessCreator.create(CopyImage::class.java, file.path) {
                if (it != 0) {
                    println("Failed to copy image to clipboard, exit code: $it")
                    button.text = Translations.COPY_FAILED()
                } else {
                    button.text = Translations.COPIED()
                }
            }
        }(x + width - 2 * (width - 15) / 4 - 5, y + height - 14, (width - 15) / 4, 14))
        addRenderable(Button(Translations.DELETE()) {
            if (!deleteRequest) {
                deleteRequest = true
                it.text = Translations.CONFIRM_DELETE()
            } else {
                file.delete()
                OptionScreen.currentInstance?.let { screen -> Screenshot.invoke(screen).apply { this.onClick(this) }; screen.category.value = Screenshot.id.toString() }
            }
        }(x + width - (width - 15) / 4, y + height - 14, (width - 15) / 4, 14))
    }
}