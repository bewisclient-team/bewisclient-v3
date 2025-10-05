package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.util.color.Color

interface RectDrawing : ScreenDrawingInterface {
    fun fill(x: Int, y: Int, width: Int, height: Int, color: Color) {
        drawContext.fill(x, y, x + width, y + height, applyAlpha(color))
    }

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

    fun drawHorizontalGradient(
        x: Int, y: Int, width: Int, height: Int, startColor: Color, endColor: Color
    ) {
        drawContext.fillGradient(x, y, x + width, y + height, applyAlpha(startColor), applyAlpha(endColor))
    }

    fun drawVerticalGradient(
        x: Int, y: Int, width: Int, height: Int, startColor: Color, endColor: Color
    ) {
        translate((x - width).toFloat(), -y.toFloat()) {
            rotate(-90f)
            drawHorizontalGradient(0, 0, height, width, startColor, endColor)
        }
    }
}