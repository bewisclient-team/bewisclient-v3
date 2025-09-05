package net.bewis09.bewisclient.drawable.screen_drawing

interface RectDrawing : ScreenDrawingInterface {
    fun fill(x: Int, y: Int, width: Int, height: Int, @ArgbColor color: Number) {
        drawContext.fill(x, y, x + width, y + height, applyAlpha(color))
    }

    fun fill(x: Int, y: Int, width: Int, height: Int, @RgbColor color: Number, alpha: Float) {
        fill(x, y, width, height, withAlpha(color, alpha))
    }

    fun drawBorder(x: Int, y: Int, width: Int, height: Int, @ArgbColor color: Number) {
        drawContext.fill(x, y, x + width, y + 1, applyAlpha(color))
        drawContext.fill(x, y + height - 1, x + width, y + height, applyAlpha(color))
        drawContext.fill(x, y, x + 1, y + height, applyAlpha(color))
        drawContext.fill(x + width - 1, y, x + width, y + height, applyAlpha(color))
    }

    fun drawBorder(x: Int, y: Int, width: Int, height: Int, @RgbColor color: Number, alpha: Float) {
        drawBorder(x, y, width, height, withAlpha(color, alpha))
    }

    fun fillWithBorder(x: Int, y: Int, width: Int, height: Int, @ArgbColor color: Number, @ArgbColor borderColor: Number) {
        fill(x, y, width, height, color)
        drawBorder(x, y, width, height, borderColor)
    }

    fun fillWithBorder(
        x: Int, y: Int, width: Int, height: Int, @RgbColor color: Number, @RgbColor borderColor: Number, alpha: Float
    ) {
        fill(x, y, width, height, withAlpha(color, alpha))
        drawBorder(x, y, width, height, withAlpha(borderColor, alpha))
    }

    fun fillWithBorder(
        x: Int, y: Int, width: Int, height: Int, @RgbColor color: Number, alpha: Float, @RgbColor borderColor: Number, borderAlpha: Float
    ) {
        fill(x, y, width, height, withAlpha(color, alpha))
        drawBorder(x, y, width, height, withAlpha(borderColor, borderAlpha))
    }

    fun drawHorizontalLine(startX: Int, y: Int, width: Int, @ArgbColor color: Number) {
        fill(startX, y, width, 1, color)
    }

    fun drawHorizontalLine(startX: Int, y: Int, width: Int, @RgbColor color: Number, alpha: Float) {
        fill(startX, y, width, 1, color, alpha)
    }

    fun drawVerticalLine(x: Int, startY: Int, height: Int, @ArgbColor color: Number) {
        fill(x, startY, 1, height, color)
    }

    fun drawVerticalLine(x: Int, startY: Int, height: Int, @RgbColor color: Number, alpha: Float) {
        fill(x, startY, 1, height, color, alpha)
    }

    fun drawHorizontalGradient(
        x: Int, y: Int, width: Int, height: Int, @ArgbColor startColor: Number, @ArgbColor endColor: Number
    ) {
        drawContext.fillGradient(x, y, x + width, y + height, applyAlpha(startColor), applyAlpha(endColor))
    }

    fun drawHorizontalGradient(
        x: Int, y: Int, width: Int, height: Int, @RgbColor startColor: Number, @RgbColor endColor: Number, alpha: Float
    ) {
        drawHorizontalGradient(
            x, y, width, height, withAlpha(startColor, alpha), withAlpha(endColor, alpha)
        )
    }

    fun drawVerticalGradient(
        x: Int, y: Int, width: Int, height: Int, @ArgbColor startColor: Number, @ArgbColor endColor: Number
    ) {
        push()
        translate((x - width).toFloat(), -y.toFloat())
        rotate(-90f)
        drawHorizontalGradient(0, 0, height, width, startColor, endColor)
        pop()
    }

    /**
     * TODO Has to be tested, might not work as expected
     */
    fun drawVerticalGradient(
        x: Int, y: Int, width: Int, height: Int, @RgbColor startColor: Number, @RgbColor endColor: Number, alpha: Float
    ) {
        drawVerticalGradient(
            x, y, width, height, withAlpha(startColor, alpha), withAlpha(endColor, alpha)
        )
    }
}