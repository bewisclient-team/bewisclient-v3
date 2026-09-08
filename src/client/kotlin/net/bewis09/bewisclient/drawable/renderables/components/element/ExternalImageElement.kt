package net.bewis09.bewisclient.drawable.renderables.components.element

import com.mojang.blaze3d.platform.NativeImage
import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.then
import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.Screenshot.ScreenshotElement
import net.bewis09.bewisclient.version.registerTexture
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.Padding
import net.bewis09.renderite.logic.TextAlign
import net.bewis09.renderite.style.RenderiteChild
import net.minecraft.client.Minecraft
import java.io.File

class ExternalImageElement(p: Props<ExternalImageElement>): PropedRenderable<ExternalImageElement>(p), Padding {
    lateinit var file: File
    override var padding: Int = 0
    override var verticalPadding: Int? = null
    override var horizontalPadding: Int? = null
    override var paddingLeft: Int? = null
    override var paddingTop: Int? = null
    override var paddingRight: Int? = null
    override var paddingBottom: Int? = null

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
            val aspectRatio = (data.nativeImage ?: return@also).width.toFloat() / data.nativeImage.height.toFloat()

            val imgHeight = ((width - paddingLeft() - paddingRight()) * (1 / aspectRatio)).coerceAtMost((height - paddingTop() - paddingBottom()).toFloat())
            val imgWidth = (imgHeight * aspectRatio).toInt()

            screenDrawing.drawTexture(it, (x + width / 2 - imgWidth / 2) + paddingLeft() / 2 - paddingRight() / 2, (y + height / 2 - imgHeight.toInt() / 2) + paddingTop() / 2 - paddingBottom() / 2, imgWidth, imgHeight.toInt())
        } ?: run {
            screenDrawing.drawText((data.failed then { ScreenshotElement.loadingFailed() }) ?: ScreenshotElement.loading(), x + width / 2, y + (height - 19) / 2 - 5) {
                color = Color.WHITE
                textAlign = TextAlign.CENTER
            }
            if (!data.failed && (data.nativeImage != null)) {
                loadTexture(file, data.nativeImage)
            }
        }
    }

    class ImageFileData(val nativeImage: NativeImage?, val identifier: Identifier?, val failed: Boolean)
}

@RenderiteChild
fun Renderable.ExternalImage(p: RenderiteElement.Props<ExternalImageElement>) = ExternalImageElement(p).add()