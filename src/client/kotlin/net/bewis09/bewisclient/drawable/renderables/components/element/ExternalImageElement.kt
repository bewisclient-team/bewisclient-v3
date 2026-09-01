package net.bewis09.bewisclient.drawable.renderables.components.element

import com.mojang.blaze3d.platform.NativeImage
import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.then
import net.bewis09.bewisclient.drawable.Init
import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.Screenshot.ScreenshotElement
import net.bewis09.bewisclient.version.registerTexture
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.logic.Color
import net.minecraft.client.Minecraft
import java.io.File

class ExternalImageElement(p: Props<ExternalImageElement>): PropedRenderable<ExternalImageElement>(p) {
    lateinit var file: File
    var padding: Int = 0
    var verticalPadding: Int? = null
    var horizontalPadding: Int? = null
    var paddingLeft: Int? = null
    var paddingTop: Int? = null
    var paddingRight: Int? = null
    var paddingBottom: Int? = null

    init { props() }

    companion object {
        val contents = mutableMapOf<File, ImageFileData>()

        fun loadTexture(file: File, nativeImage: NativeImage) {
            createIdentifier("bewisclient", "screenshot/${file.nameWithoutExtension}_" + (Math.random() * 0x10000).toInt().toString(16)).also {
                try {
                    Minecraft.getInstance().registerTexture(it, nativeImage)
                    contents[file] = ImageFileData(nativeImage, it, false)
                } catch (e: Exception) {
                    contents[file] = ImageFileData(null, null, true)
                    e.printStackTrace()
                }
            }
        }
    }

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val data = contents.getOrDefault(file, null) ?: return

        data.identifier?.also {
            val paddingTop = paddingTop ?: verticalPadding ?: padding
            val paddingBottom = paddingBottom ?: verticalPadding ?: padding
            val paddingLeft = paddingLeft ?: horizontalPadding ?: padding
            val paddingRight = paddingRight ?: horizontalPadding ?: padding

            val nativeImage = data.nativeImage ?: return@also

            val aspectRatio = nativeImage.width.toFloat() / nativeImage.height.toFloat()

            val imgHeight = ((width - paddingLeft - paddingRight) * (1 / aspectRatio)).coerceAtMost((height - paddingTop - paddingBottom).toFloat())
            val imgWidth = (imgHeight * aspectRatio).toInt()

            screenDrawing.drawTexture(it, (x + width / 2 - imgWidth / 2) + paddingLeft / 2 - paddingRight / 2, (y + height / 2 - imgHeight.toInt() / 2) + paddingTop / 2 - paddingBottom / 2, imgWidth, imgHeight.toInt())
        } ?: run {
            screenDrawing.drawCenteredText((data.failed then { ScreenshotElement.loadingFailed() }) ?: ScreenshotElement.loading(), x + width / 2, y + (height - 19) / 2 - 5, Color.WHITE)
            if (!data.failed && (data.nativeImage != null)) {
                loadTexture(file, data.nativeImage)
            }
        }
    }

    override fun Init.init() {

    }

    class ImageFileData(val nativeImage: NativeImage?, val identifier: Identifier?, val failed: Boolean)
}

fun Init.ExternalImage(p: RenderiteElement.Props<ExternalImageElement>) = ExternalImageElement(p).add()