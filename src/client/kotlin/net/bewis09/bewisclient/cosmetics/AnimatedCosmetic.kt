package net.bewis09.bewisclient.cosmetics

import net.bewis09.bewisclient.util.Bewisclient
import net.bewis09.bewisclient.version.Identifier
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.imageio.ImageIO

class AnimatedCosmetic(val baseIdentifier: Identifier, val frames: Int) : Cosmetic {
    override fun getIdentifier(): Identifier {
        return baseIdentifier.withSuffix("_" + ((System.currentTimeMillis() / 80) % frames))
    }

    companion object {
        fun create(baseIdentifier: CosmeticIdentifier, byteArray: ByteArray, frames: Int): AnimatedCosmetic {
            val frameArray = getFrames(byteArray, frames)

            frameArray.forEachIndexed { index: Int, image: BufferedImage ->
                Bewisclient.createTexture(baseIdentifier.identifier.withSuffix("_$index"), image)
            }

            return AnimatedCosmetic(baseIdentifier.identifier, frames)
        }

        @Throws(IOException::class)
        fun getFrames(input: ByteArray, count: Int): List<BufferedImage> {
            val reader = ImageIO.getImageReadersByFormatName("gif").next()
            reader.input = ImageIO.createImageInputStream(ByteArrayInputStream(input))

            val frames = Array<BufferedImage>(count) { index ->
                reader.read(index)
            }

            return frames.toList()
        }
    }
}