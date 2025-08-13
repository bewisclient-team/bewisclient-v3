package net.bewis09.bewisclient.logic

import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO

interface TextureLogic {
    fun createTexture(identifier: Identifier, width: Int, height: Int, supplier: (img: BufferedImage) -> Unit): Identifier {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        supplier(image)

        val os = ByteArrayOutputStream()
        ImageIO.write(image, "png", os)
        val fis: InputStream = ByteArrayInputStream(os.toByteArray())

        MinecraftClient.getInstance().textureManager.registerTexture(
            identifier,
            NativeImageBackedTexture({ identifier.toString() }, NativeImage.read(fis))
        )

        return identifier
    }
}