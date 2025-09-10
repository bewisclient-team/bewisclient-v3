package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.logic.color.Color
import net.minecraft.util.Identifier
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

interface RoundedDrawing : RectDrawing, TextureDrawing {
    companion object {
        val roundFillCache = mutableMapOf<Pair<Int, Int>, Identifier>()
        val roundBorderCache = mutableMapOf<Pair<Int, Int>, Identifier>()
    }

    fun getRoundedImage(radius: Int): Identifier {
        val scale = client.window.scaleFactor

        val id = roundFillCache[radius to scale]

        if (id != null) return id

        val identifier = Identifier.of("bewisclient", "rounded_${radius}_$scale")

        val r = radius * scale

        createTexture(identifier, r, r) {
            for (i in 0 until r) {
                val height = sqrt((r * r - i * i).toDouble()).roundToInt()
                for (j in 0 until height) {
                    it.setRGB(i, j, 0xFFFFFFFF.toInt())
                }
            }

            for (i in 0 until r) {
                val height = sqrt((r * r - i * i).toDouble()).roundToInt()
                for (j in 0 until height) {
                    it.setRGB(j, i, 0xFFFFFFFF.toInt())
                }
            }
        }

        roundFillCache[radius to scale] = identifier

        return identifier
    }

    fun getRoundedBorderImage(radius: Int): Identifier {
        val scale = client.window.scaleFactor

        val id = roundBorderCache[radius to scale]

        if (id != null) {
            return id
        }

        val identifier = Identifier.of("bewisclient", "rounded_border_${radius}_$scale")

        val r = radius * scale

        createTexture(identifier, r, r) {
            for (i in 0 until r) {
                val height = sqrt((r * r - i * i).toDouble()).roundToInt()
                val inner = sqrt(0.0.coerceAtLeast(((r - scale).toDouble()).pow(2) - i * i)).roundToInt()
                for (j in inner until height) {
                    it.setRGB(i, j, 0xFFFFFFFF.toInt())
                }
            }

            for (i in 0 until r) {
                val height = sqrt((r * r - i * i).toDouble()).roundToInt()
                val inner = sqrt(0.0.coerceAtLeast(((r - scale).toDouble()).pow(2) - i * i)).roundToInt()
                for (j in inner until height) {
                    it.setRGB(j, i, 0xFFFFFFFF.toInt())
                }
            }
        }

        roundBorderCache[radius to scale] = identifier

        return identifier
    }

    fun fillRounded(x: Int, y: Int, width: Int, height: Int, radius: Int, color: Color) {
        val adjustedRadius = min(radius, min(width / 2, height / 2))

        // Fill the main rectangle (without corners)
        fill(x + adjustedRadius, y, width - 2 * adjustedRadius, adjustedRadius, color)
        fill(x, y + adjustedRadius, width, height - 2 * adjustedRadius, color)
        fill(x + adjustedRadius, y + height - adjustedRadius, width - 2 * adjustedRadius, adjustedRadius, color)

        if (adjustedRadius <= 0) return

        // Draw rounded corners using circles
        drawRoundedCorner(x + adjustedRadius, y + adjustedRadius, adjustedRadius, color, 180f) // Top-left
        drawRoundedCorner(x + width - adjustedRadius, y + adjustedRadius, adjustedRadius, color, 270f) // Top-right
        drawRoundedCorner(x + adjustedRadius, y + height - adjustedRadius, adjustedRadius, color, 90f) // Bottom-left
        drawRoundedCorner(x + width - adjustedRadius, y + height - adjustedRadius, adjustedRadius, color, 0f) // Bottom-right
    }

    fun drawBorderRounded(x: Int, y: Int, width: Int, height: Int, radius: Int, color: Color) {
        val adjustedRadius = min(radius, min(width / 2, height / 2))

        // Draw the border lines (without corners)
        drawHorizontalLine(x + adjustedRadius, y, width - 2 * adjustedRadius, color) // Top
        drawHorizontalLine(x + adjustedRadius, y + height - 1, width - 2 * adjustedRadius, color) // Bottom
        drawVerticalLine(x, y + adjustedRadius, height - 2 * adjustedRadius, color) // Left
        drawVerticalLine(x + width - 1, y + adjustedRadius, height - 2 * adjustedRadius, color) // Right

        if (adjustedRadius <= 0) return

        // Draw rounded corner borders
        drawRoundedCornerBorder(x + adjustedRadius, y + adjustedRadius, adjustedRadius, color, 180f) // Top-left
        drawRoundedCornerBorder(x + width - adjustedRadius, y + adjustedRadius, adjustedRadius, color, 270f) // Top-right
        drawRoundedCornerBorder(x + adjustedRadius, y + height - adjustedRadius, adjustedRadius, color, 90f) // Bottom-left
        drawRoundedCornerBorder(x + width - adjustedRadius, y + height - adjustedRadius, adjustedRadius, color, 0f) // Bottom-right
    }

    fun fillWithBorderRounded(
        x: Int, y: Int, width: Int, height: Int, radius: Int, fillColor: Color, borderColor: Color
    ) {
        fillRounded(x, y, width, height, radius, fillColor)
        drawBorderRounded(x, y, width, height, radius, borderColor)
    }

    private fun drawRoundedCorner(
        centerX: Int, centerY: Int, radius: Int, color: Color, startAngle: Float
    ) {
        push()
        translate(centerX.toFloat(), centerY.toFloat())
        rotateDegrees(startAngle)
        scale(
            1 / client.window.scaleFactor.toFloat(), 1 / client.window.scaleFactor.toFloat()
        )

        val r = radius * (client.window.scaleFactor)

        drawTexture(getRoundedImage(radius), 0, 0, 0f, 0f, r, r, r, r, color)
        pop()
    }

    private fun drawRoundedCornerBorder(
        centerX: Int, centerY: Int, radius: Int, color: Color, startAngle: Float
    ) {
        push()
        translate(centerX.toFloat(), centerY.toFloat())
        rotateDegrees(startAngle)
        scale(
            1 / client.window.scaleFactor.toFloat(), 1 / client.window.scaleFactor.toFloat()
        )

        val r = radius * (client.window.scaleFactor)

        drawTexture(getRoundedBorderImage(radius), 0, 0, 0f, 0f, r, r, r, r, color)
        pop()
    }
}