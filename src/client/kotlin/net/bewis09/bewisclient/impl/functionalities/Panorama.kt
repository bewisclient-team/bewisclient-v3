package net.bewis09.bewisclient.impl.functionalities

import net.bewis09.bewisclient.core.registerTexture
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.Hoverable
import net.bewis09.bewisclient.drawable.renderables.ImageButton
import net.bewis09.bewisclient.drawable.renderables.VerticalAlignScrollPlane
import net.bewis09.bewisclient.drawable.renderables.options_structure.ImageSettingCategory
import net.bewis09.bewisclient.drawable.renderables.settings.InfoTextRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.BewisclientResourcePack
import net.bewis09.bewisclient.game.Keybind
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.impl.settings.functionalities.PanoramaSettings
import net.bewis09.bewisclient.impl.settings.functionalities.PanoramaSettings.path
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.util.createIdentifier
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.resource.InputSupplier
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import java.io.File
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO

object Panorama : ImageSettingCategory(
    "panorama", Translation("menu.category.panorama", "Panorama"), arrayOf(
        InfoTextRenderable(
            Translation("panorama.info_text", "The panorama functionality allows you to set a custom panorama background for the main menu. You can create the panorama by pressing the \"%s\" button [%s]. After taking the screenshot select the screenshot below.")(Text.translatable("bewisclient.key.screenshot.take_panorama"), Text.keybind("bewisclient.key.screenshot.take_panorama")),
            centered = true
        ),
    ), PanoramaSettings.enabled
), EventEntrypoint, BewisclientResourcePack.CustomResourceProvider {
    object TakePanoramaScreenshot : Keybind(-1, "screenshot.take_panorama", "Take Panorama Screenshot", {
        MinecraftClient.getInstance().player?.sendMessage(MinecraftClient.getInstance().takePanorama(FabricLoader.getInstance().gameDir.resolve("screenshots/panorama_" + (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss-S")))).toFile().apply {
            if (!exists()) mkdirs()
        }), false)
    })

    val images = mutableMapOf<File, PanoramaScreenshots>()

    override fun getPane(): Renderable {
        return VerticalAlignScrollPlane(setting.toMutableList().apply {
            addAll(FabricLoader.getInstance().gameDir.resolve("screenshots").toFile().listFiles {
                it.isDirectory && it.name.startsWith("panorama_") && it.resolve("screenshots").exists() && it.resolve("screenshots").listFiles().map { f -> f.name }.let { name ->
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
                images[file] = PanoramaScreenshots(file).apply { Util.getIoWorkerExecutor().execute(::loadAll) }
            }

            images[file]?.registerAll()

            super.render(screenDrawing, mouseX, mouseY)

            if (path.get() == file.absolutePath)
                screenDrawing.fillWithBorderRounded(x, y, width, height, 5, OptionsMenuSettings.getThemeColor(alpha = 0.25f), OptionsMenuSettings.getThemeColor(alpha = 0.5f), topLeft = index == 0, topRight = index == 0, bottomLeft = index == size - 1, bottomRight = index == size - 1)
            else
                screenDrawing.fillRounded(x, y, width, height, 5, OptionsMenuSettings.getThemeColor(alpha = hoverAnimation["hovering"] * 0.15f + 0.1f), topLeft = index == 0, topRight = index == 0, bottomLeft = index == size - 1, bottomRight = index == size - 1)
            screenDrawing.drawText(file.name, x + 8, y + 8, OptionsMenuSettings.getTextThemeColor())

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
                if (PanoramaSettings.enabled.get())
                    client.reloadResources()
            }.setImagePadding(2)(x + width - 21, y + 7, 14, 14))
            addRenderable(ImageButton(createIdentifier("bewisclient", "textures/gui/sprites/delete.png")) {

            }.setImagePadding(2)(x + width - 21, y + 25, 14, 14))
            addRenderable(ImageButton(createIdentifier("bewisclient", "textures/gui/sprites/rename.png")) {

            }.setImagePadding(2)(x + width - 21, y + 43, 14, 14))
        }
    }

    override fun provideResources(id: Identifier): InputSupplier<InputStream?>? {
        if (id.namespace != "minecraft" || path.get().isEmpty() || !PanoramaSettings.enabled) return null

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
            return InputSupplier { file.inputStream() }
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
                images[i] = NativeImage.read(directory.resolve("screenshots/panorama_$i.png").readBytes())
            }

            loaded = true
        }

        fun registerAll() {
            if (registered || !loaded) return

            registered = true

            for (i in 0..5) {
                val identifier = createIdentifier("bewisclient", directory.name.filter(Identifier::isCharValid) + "_" + i)
                identifiers[i] = identifier
                client.registerTexture(identifier, images[i]!!)
            }
        }
    }
}