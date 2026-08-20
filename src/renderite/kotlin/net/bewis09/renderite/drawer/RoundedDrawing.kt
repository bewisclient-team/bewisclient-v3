package net.bewis09.renderite.drawer

import net.bewis09.renderite.logic.Color
import kotlin.math.*

interface RoundedDrawing<I, T, F> : RectDrawing<I, T, F>, TextureDrawing<I, T, F> {
    val imageScaleFactor: Int
        get() = 1

    val fillCache: MutableMap<Pair<Int, Int>, I>
    val borderCache: MutableMap<Pair<Int, Int>, I>

    fun getRoundedImage(radius: Int): I {
        val scale = imageScaleFactor

        val id = fillCache[radius to scale]

        if (id != null) return id

        val identifier = createBorderImage("rounded_${radius}_$scale")

        val r = radius * scale

        createTexture(identifier, r, r) {
            for (i in 0 until r) {
                val height = sqrt((r * r - i * i).toDouble()).roundToInt()
                for (j in 0 until height) {
                    it(i, j, 0xFFFFFFFF.toInt())
                }
            }

            for (i in 0 until r) {
                val height = sqrt((r * r - i * i).toDouble()).roundToInt()
                for (j in 0 until height) {
                    it(j, i, 0xFFFFFFFF.toInt())
                }
            }
        }

        fillCache[radius to scale] = identifier

        return identifier
    }

    fun getRoundedBorderImage(radius: Int): I {
        val scale = imageScaleFactor

        val id = borderCache[radius to scale]

        if (id != null) {
            return id
        }

        val identifier = createBorderImage("rounded_border_${radius}_$scale")

        val r = radius * scale

        createTexture(identifier, r, r) {
            for (i in 0 until r) {
                val height = sqrt((r * r - i * i).toDouble()).roundToInt()
                val inner = sqrt(0.0.coerceAtLeast(((r - scale).toDouble()).pow(2) - i * i)).roundToInt()
                for (j in inner until height) {
                    it(j, i, 0xFFFFFFFF.toInt())
                }
            }
        }

        borderCache[radius to scale] = identifier

        return identifier
    }

    fun createBorderImage(name: String): I

    fun createTexture(i: I, w: Int, h: Int, func: ((Int, Int, Int) -> Unit) -> Unit)

    fun fillRounded(x: Int, y: Int, width: Int, height: Int, radius: Int, color: Color, topLeft: Boolean = true, topRight: Boolean = true, bottomLeft: Boolean = true, bottomRight: Boolean = true) {
        if (radius == 0) {
            fill(x, y, width, height, color)
            return
        }

        val adjustedRadius = min(radius, min(width / 2, height / 2))

        // Fill the main rectangle (without corners)
        fill(x + adjustedRadius, y, width - 2 * adjustedRadius, adjustedRadius, color)
        fill(x, y + adjustedRadius, width, height - 2 * adjustedRadius, color)
        fill(x + adjustedRadius, y + height - adjustedRadius, width - 2 * adjustedRadius, adjustedRadius, color)

        if (adjustedRadius <= 0) return

        // Draw rounded corners using circles
        if (topLeft)
            drawRoundedCorner(x + adjustedRadius, y + adjustedRadius, adjustedRadius, color, 180f) // Top-left
        else
            fill(x, y, adjustedRadius, adjustedRadius, color)

        if (topRight)
            drawRoundedCorner(x + width - adjustedRadius, y + adjustedRadius, adjustedRadius, color, 270f) // Top-right
        else
            fill(x + width - adjustedRadius, y, adjustedRadius, adjustedRadius, color)

        if (bottomLeft)
            drawRoundedCorner(x + adjustedRadius, y + height - adjustedRadius, adjustedRadius, color, 90f) // Bottom-left
        else
            fill(x, y + height - adjustedRadius, adjustedRadius, adjustedRadius, color)

        if (bottomRight)
            drawRoundedCorner(x + width - adjustedRadius, y + height - adjustedRadius, adjustedRadius, color, 0f) // Bottom-right
        else
            fill(x + width - adjustedRadius, y + height - adjustedRadius, adjustedRadius, adjustedRadius, color)
    }

    fun drawBorderRounded(x: Int, y: Int, width: Int, height: Int, radius: Int, color: Color, topLeft: Boolean = true, topRight: Boolean = true, bottomLeft: Boolean = true, bottomRight: Boolean = true) {
        if (radius == 0) {
            drawBorder(x, y, width, height, color)
            return
        }

        val adjustedRadius = min(radius, min(width / 2, height / 2))

        // Draw the border lines (without corners)
        drawHorizontalLine(x + adjustedRadius, y, width - 2 * adjustedRadius, color) // Top
        drawHorizontalLine(x + adjustedRadius, y + height - 1, width - 2 * adjustedRadius, color) // Bottom
        drawVerticalLine(x, y + adjustedRadius, height - 2 * adjustedRadius, color) // Left
        drawVerticalLine(x + width - 1, y + adjustedRadius, height - 2 * adjustedRadius, color) // Right

        if (adjustedRadius <= 0) return

        // Draw rounded corner borders
        if (topLeft)
            drawRoundedCornerBorder(x + adjustedRadius, y + adjustedRadius, adjustedRadius, color, 180f) // Top-left
        else {
            drawHorizontalLine(x, y, adjustedRadius, color)
            drawVerticalLine(x, y + 1, adjustedRadius - 1, color)
        }

        if (topRight)
            drawRoundedCornerBorder(x + width - adjustedRadius, y + adjustedRadius, adjustedRadius, color, 270f) // Top-right
        else {
            drawHorizontalLine(x + width - adjustedRadius, y, adjustedRadius - 1, color)
            drawVerticalLine(x + width - 1, y, adjustedRadius, color)
        }

        if (bottomLeft)
            drawRoundedCornerBorder(x + adjustedRadius, y + height - adjustedRadius, adjustedRadius, color, 90f) // Bottom-left
        else {
            drawHorizontalLine(x + 1, y + height - 1, adjustedRadius - 1, color)
            drawVerticalLine(x, y + height - adjustedRadius, adjustedRadius, color)
        }

        if (bottomRight)
            drawRoundedCornerBorder(x + width - adjustedRadius, y + height - adjustedRadius, adjustedRadius, color, 0f) // Bottom-right
        else {
            drawHorizontalLine(x + width - adjustedRadius, y + height - 1, adjustedRadius - 1, color)
            drawVerticalLine(x + width - 1, y + height - adjustedRadius, adjustedRadius, color)
        }
    }

    fun fillWithBorderRounded(
        x: Int, y: Int, width: Int, height: Int, radius: Int, fillColor: Color, borderColor: Color, topLeft: Boolean = true, topRight: Boolean = true, bottomLeft: Boolean = true, bottomRight: Boolean = true
    ) {
        fillRounded(x, y, width, height, radius, fillColor, topLeft, topRight, bottomLeft, bottomRight)
        drawBorderRounded(x, y, width, height, radius, borderColor, topLeft, topRight, bottomLeft, bottomRight)
    }

    private fun drawRoundedCorner(
        centerX: Int, centerY: Int, radius: Int, color: Color, startAngle: Float
    ) {
        transform(centerX.toFloat(), centerY.toFloat(), 1 / imageScaleFactor.toFloat()) {
            rotateDegrees(startAngle)

            val r = radius * (imageScaleFactor)

            drawTexture(getRoundedImage(radius), 0, 0, 0f, 0f, r, r, r, r, color)
        }
    }

    private fun drawRoundedCornerBorder(
        centerX: Int, centerY: Int, radius: Int, color: Color, startAngle: Float
    ) {
        transform(centerX.toFloat(), centerY.toFloat(), 1 / imageScaleFactor.toFloat()) {
            rotateDegrees(startAngle)

            val r = radius * (imageScaleFactor)

            drawTexture(getRoundedBorderImage(radius), 0, 0, 0f, 0f, r, r, r, r, color)
        }
    }
}