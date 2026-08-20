package net.bewis09.renderite.drawer

import net.bewis09.renderite.logic.Color

interface RectDrawing<I, T, F> : ScreenDrawingInterface<I, T, F> {
    fun fill(x: Int, y: Int, width: Int, height: Int, color: Color) {
        fillIntern(x, y, width, height, applyAlpha(color))
    }

    fun fillIntern(x: Int, y: Int, width: Int, height: Int, color: Int)

    fun drawBorder(x: Int, y: Int, width: Int, height: Int, color: Color) {
        fill(x, y, width, 1, color)
        fill(x, y + height - 1, width, 1, color)
        fill(x, y + 1, 1, height - 2, color)
        fill(x + width - 1, y + 1, 1, height - 2, color)
    }

    fun fillWithBorder(x: Int, y: Int, width: Int, height: Int, color: Color, borderColor: Color) {
        fill(x, y, width, height, color)
        drawBorder(x, y, width, height, borderColor)
    }

    fun drawHorizontalLine(startX: Int, y: Int, width: Int, color: Color) {
        fill(startX, y, width, 1, color)
    }

    fun drawVerticalLine(x: Int, startY: Int, height: Int, color: Color) {
        fill(x, startY, 1, height, color)
    }

    fun drawHorizontalGradient(x: Int, y: Int, width: Int, height: Int, startColor: Color, endColor: Color) {
        drawHorizontalGradientIntern(x, y, x + width, y + height, applyAlpha(startColor), applyAlpha(endColor))
    }

    fun drawHorizontalGradientIntern(
        x: Int, y: Int, width: Int, height: Int, startColor: Int, endColor: Int
    )

    fun drawVerticalGradient(x: Int, y: Int, width: Int, height: Int, startColor: Color, endColor: Color) {
        translate(x.toFloat(), (y + height).toFloat()) {
            rotateDegrees(-90f)
            drawHorizontalGradient(0, 0, height, width, startColor, endColor)
        }
    }
}