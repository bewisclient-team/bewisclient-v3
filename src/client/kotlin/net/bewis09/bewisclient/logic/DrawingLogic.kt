package net.bewis09.bewisclient.logic

import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import java.awt.image.BufferedImage
import java.io.*
import java.net.URL
import javax.imageio.ImageIO

interface DrawingLogic : InGameLogic {
    fun createTexture(identifier: Identifier, width: Int, height: Int, supplier: (img: BufferedImage) -> Unit): Identifier {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        supplier(image)

        val os = ByteArrayOutputStream()
        ImageIO.write(image, "png", os)
        val fis: InputStream = ByteArrayInputStream(os.toByteArray())

        client.textureManager.registerTexture(
            identifier, NativeImageBackedTexture({ identifier.toString() }, NativeImage.read(fis))
        )

        return identifier
    }

    fun createTexture(identifier: Identifier, fileURL: URL): Identifier {
        client.textureManager.registerTexture(
            identifier, NativeImageBackedTexture({ identifier.toString() }, NativeImage.read(fileURL.openStream()))
        )

        return identifier
    }

    fun isMouseOver(
        mouseX: Float, mouseY: Float, x: Int, y: Int, width: Int, height: Int
    ): Boolean {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height
    }

    fun withAlpha(rgb: Number, alpha: Float): Int {
        return ((rgb.toLong() and 0xFFFFFF) or ((alpha * 255).toLong() shl 24)).toInt()
    }
}

/** Checks if the mouse is hovering over a specific area and executes the appropriate action. */
inline fun <T> DrawingLogic.hoverSeparate(
    mouseX: Float, mouseY: Float, x: Int, y: Int, width: Int, height: Int, normal: (x: Int, y: Int, width: Int, height: Int) -> T, hovered: (x: Int, y: Int, width: Int, height: Int) -> T
): T {
    return if (isMouseOver(mouseX, mouseY, x, y, width, height)) {
        hovered(x, y, width, height)
    } else {
        normal(x, y, width, height)
    }
}

/** Checks if the mouse is hovering over a specific area and executes the appropriate action. */
inline fun <T> DrawingLogic.hoverSeparate(
    mouseX: Float, mouseY: Float, x: Int, y: Int, width: Int, height: Int, normal: () -> T, hovered: () -> T
): T {
    return if (isMouseOver(mouseX, mouseY, x, y, width, height)) {
        hovered()
    } else {
        normal()
    }
}