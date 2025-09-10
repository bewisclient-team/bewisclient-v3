package net.bewis09.bewisclient.drawable.screen_drawing

import java.awt.Color

interface RectDrawing : ScreenDrawingInterface {
    fun fill(x: Int, y: Int, width: Int, height: Int, color: Color) {
        drawContext.fill(x, y, x + width, y + height, applyAlpha(color))
    }

    fun drawBorder(x: Int, y: Int, width: Int, height: Int, color: Color) {
        drawContext.fill(x, y, x + width, y + 1, applyAlpha(color))
        drawContext.fill(x, y + height - 1, x + width, y + height, applyAlpha(color))
        drawContext.fill(x, y, x + 1, y + height, applyAlpha(color))
        drawContext.fill(x + width - 1, y, x + width, y + height, applyAlpha(color))
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
        push()
        translate((x - width).toFloat(), -y.toFloat())
        rotate(-90f)
        drawHorizontalGradient(0, 0, height, width, startColor, endColor)
        pop()
    }
}