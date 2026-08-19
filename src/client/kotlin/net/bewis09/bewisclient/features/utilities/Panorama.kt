package net.bewis09.bewisclient.features.utilities

import com.mojang.blaze3d.platform.NativeImage
import net.bewis09.bewisclient.common.*
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.components.logic.Hoverable
import net.bewis09.bewisclient.drawable.renderables.components.button.ImageButton
import net.bewis09.bewisclient.drawable.renderables.components.structure.VerticalAlignScrollPlane
import net.bewis09.bewisclient.drawable.renderables.notification.NotificationManager
import net.bewis09.bewisclient.drawable.renderables.notification.SimpleTextNotification
import net.bewis09.bewisclient.drawable.renderables.popup.ConfirmPopup
import net.bewis09.bewisclient.drawable.renderables.popup.InputTextPopup
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.renderables.settings.InfoTextRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.BewisclientResourcePack
import net.bewis09.bewisclient.game.keybinds.Keybind
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.features.sidebar.Screenshot
import net.bewis09.bewisclient.settings.logic.Settings
import net.bewis09.bewisclient.settings.structure.ImageFeature
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.version.registerTexture
import net.bewis09.bewisclient.version.takePanoramaFull
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.chat.Component
import net.minecraft.server.packs.resources.IoSupplier
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO
import kotlin.io.path.exists

object Panorama : ImageFeature("panorama", "Panorama"), EventEntrypoint, BewisclientResourcePack.CustomResourceProvider {
    val path = string("path", "")

    val deletedPanoramaText = createTranslation("delete_panorama_success", "Deleted panorama")
    val noEmptyNameText = createTranslation("no_empty_name", "Name cannot be empty")
    val nameAlreadyExistsText = createTranslation("name_already_exists", "A panorama with this name already exists")
    val renameSuccessText = createTranslation("rename_success", "Renamed panorama")
    val renameFailedText = createTranslation("rename_failed", "Renaming failed")
    val renamePanoramaText = createTranslation("rename_panorama", "Enter name for panorama")
    val confirmPanoramaDelete = createTranslation("confirm_panorama_delete", "Are you sure you want to delete this panorama?")

    override fun enabledListener(oldValue: Boolean?, newValue: Boolean?) {
        if (path.get().isNotEmpty() && !Settings.isLoading) {
            client.reloadResourcePacks()
        }
    }

    object TakePanoramaScreenshot : Keybind(-1, "screenshot.take_panorama", "Take Panorama Screenshot", {
        showSystemMessage(client.takePanoramaFull(FabricLoader.getInstance().gameDir.resolve("screenshots/panorama_" + (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss-S")))).toFile().apply {
            if (!exists()) mkdirs()
        }))
    })

    val images = mutableMapOf<File, PanoramaScreenshots>()

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(InfoTextRenderable(
            createTranslation("info_text", "The panorama functionality allows you to set a custom panorama background for the main menu. You can create the panorama by pressing the \"%s\" button [%s]. After taking the screenshot select the screenshot below.")(Component.translatable("bewisclient.key.screenshot.take_panorama"), Component.keybind("bewisclient.key.screenshot.take_panorama")),
            centered = true
        ))
    }

    override fun getPane(): Renderable {
        return VerticalAlignScrollPlane(getSettingRenderables().toMutableList().apply {
            addAll(FabricLoader.getInstance().gameDir.resolve("screenshots").toFile().listFiles {
                it.isDirectory && it.resolve("screenshots").exists() && it.resolve("screenshots").listFiles().map { f -> f.name }.let { name ->
                    name.contains("panorama_0.png") && name.contains("panorama_1.png") && name.contains("panorama_2.png") && name.contains("panorama_3.png") && name.contains("panorama_4.png") && name.contains("panorama_5.png")
                }
            }.mapIndexedWithSelf { index, file -> PanoramaElement(file, index, size) })
        }, 1)
    }

    inline fun <T, R> Array<T>.mapIndexedWithSelf(transform: Array<T>.(index: Int, element: T) -> R): List<R> {
        return this.mapIndexed { index, element -> this.transform(index, element) }
    }

    class PanoramaElement(val file: File, val index: Int, val size: Int) : Hoverable() {
        init {
            internalHeight = 64
        }

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            if (!images.containsKey(file)) {
                images[file] = PanoramaScreenshots(file).apply { Util.ioPool().execute(::loadAll) }
            }

            images[file]?.registerAll()

            super.render(screenDrawing, mouseX, mouseY)

            if (path.get() == file.absolutePath) screenDrawing.fillWithBorderRounded(x, y, width, height, 5, General.getThemeColor(alpha = 0.25f), General.getThemeColor(alpha = 0.5f), topLeft = index == 0, topRight = index == 0, bottomLeft = index == size - 1, bottomRight = index == size - 1)
            else screenDrawing.fillRounded(x, y, width, height, 5, General.getThemeColor(alpha = hoverFactor * 0.15f + 0.1f), topLeft = index == 0, topRight = index == 0, bottomLeft = index == size - 1, bottomRight = index == size - 1)
            screenDrawing.drawText(file.name, x + 8, y + 8, General.getTextThemeColor())

            images[file]?.identifiers?.forEachIndexed { index, identifier ->
                if (identifier != null) {
                    screenDrawing.drawTexture(identifier, x + 8 + index * 36, y + 24, 32, 32)
                }
            }

            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            super.init()
            addRenderable(ImageButton(createIdentifier("bewisclient", "textures/gui/sprites/select.png")) {
                if (path.get() == file.absolutePath) return@ImageButton

                path.set(file.absolutePath)
                if (enabled) client.reloadResourcePacks()
            }.setImagePadding(2)(x + width - 21, y + 7, 14, 14))
            addRenderable(ImageButton(createIdentifier("bewisclient", "textures/gui/sprites/delete.png")) {
                OptionScreen.currentInstance?.openPopup(
                    ConfirmPopup(confirmPanoramaDelete(), {
                        if (catch { file.deleteRecursively() } == true) {
                            NotificationManager.addNotification(SimpleTextNotification(deletedPanoramaText()))
                        } else {
                            NotificationManager.addNotification(SimpleTextNotification(Screenshot.deleteFailedNotifText()))
                        }
                        OptionScreen.currentInstance?.goBack(instant = true)
                        OptionScreen.currentInstance?.openPage(title(), getPane(), enabledSetting, true)
                    })
                )
            }.setImagePadding(2)(x + width - 21, y + 25, 14, 14))
            addRenderable(ImageButton(createIdentifier("bewisclient", "textures/gui/sprites/rename.png")) {
                OptionScreen.currentInstance?.openPopup(InputTextPopup(renamePanoramaText(), default = file.name, onConfirm = { newName ->
                    if (newName.isBlank()) {
                        NotificationManager.addNotification(SimpleTextNotification(noEmptyNameText()))
                        return@InputTextPopup
                    }

                    if (newName == file.name) return@InputTextPopup

                    if (FabricLoader.getInstance().gameDir.resolve("screenshots").resolve(newName).exists()) {
                        NotificationManager.addNotification(SimpleTextNotification(nameAlreadyExistsText()))
                        return@InputTextPopup
                    }

                    if (catch { Files.move(file.toPath(), file.parentFile.resolve(newName).toPath(), StandardCopyOption.REPLACE_EXISTING) } != null) {
                        OptionScreen.currentInstance?.goBack(instant = true)
                        OptionScreen.currentInstance?.openPage(title(), getPane(), enabledSetting, instant = true)
                        if (path.get() == file.absolutePath) {
                            path.set(file.parentFile.resolve(newName).absolutePath)
                        }
                        OptionScreen.currentInstance?.resize()
                        NotificationManager.addNotification(SimpleTextNotification(renameSuccessText()))
                    } else {
                        NotificationManager.addNotification(SimpleTextNotification(renameFailedText()))
                    }
                }))
            }.setImagePadding(2)(x + width - 21, y + 43, 14, 14))
        }
    }

    override fun provideResources(id: Identifier): IoSupplier<InputStream>? {
        if (id.namespace != "minecraft" || path.get().isEmpty() || !enabledSetting) return null

        if (id.path == "textures/gui/title/background/panorama") return null
        if (id.path == "textures/gui/title/background/panorama_overlay.png") return null

        if (id.path.startsWith("textures/gui/title/background/panorama_") && id.path.endsWith(".png")) {
            val file = File(path.get()).resolve("screenshots/" + id.path.replace("textures/gui/title/background/", ""))
            try {
                ImageIO.read(file).apply {
                    if (width != 1024 || height != 1024) return null
                }
            } catch (_: Exception) {
                return null
            }
            return IoSupplier { file.inputStream() }
        }

        return null
    }

    class PanoramaScreenshots(val directory: File) {
        val images = arrayOf<NativeImage?>(null, null, null, null, null, null)
        val identifiers = arrayOf<Identifier?>(null, null, null, null, null, null)

        var loaded = false
        var registered = false

        fun loadAll() {
            if (loaded) return

            for (i in 0..5) {
                images[i] = NativeImage.read(directory.resolve("screenshots/panorama_$i.png").inputStream())
            }

            loaded = true
        }

        fun registerAll() {
            if (registered || !loaded) return

            registered = true

            for (i in 0..5) {
                val identifier = createIdentifier("bewisclient", directory.name.filter(::isAllowedInIdentifier) + "_" + i)
                identifiers[i] = identifier
                client.registerTexture(identifier, images[i]!!)
            }
        }
    }
}