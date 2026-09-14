package net.bewis09.bewisclient.util.logic

import com.mojang.blaze3d.platform.NativeImage
import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.version.registerTexture
import net.minecraft.client.Minecraft
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.net.URL
import javax.imageio.ImageIO

interface DrawingLogic : InGameLogic {
    val isMinecrafty: Boolean
        get() = !General.oldOptionsMenu.get()

    val animationDuration: Long
        get() = General.animationTime.get().toLong()

    fun createTexture(identifier: Identifier, width: Int, height: Int, supplier: (img: BufferedImage) -> Unit): Identifier {
        return createTexture(identifier, BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB).apply { supplier(this) })
    }

    fun createTexture(identifier: Identifier, byteArray: ByteArray): Identifier = identifier.apply {
        Minecraft.getInstance().registerTexture(identifier, NativeImage.read(ByteArrayInputStream(byteArray)))
    }

    fun createTexture(identifier: Identifier, image: BufferedImage): Identifier {
        return createTexture(identifier, ByteArrayOutputStream().apply { ImageIO.write(image, "png", this) }.toByteArray())
    }

    fun createTexture(identifier: Identifier, fileURL: URL): Identifier {
        Minecraft.getInstance().registerTexture(identifier, NativeImage.read(fileURL.openStream()))

        return identifier
    }

    fun isMouseOver(
        mouseX: Number, mouseY: Number, x: Int, y: Int, width: Int, height: Int
    ): Boolean = mouseX.toInt() >= x && mouseX.toInt() <= x + width && mouseY.toInt() >= y && mouseY.toInt() <= y + height
}