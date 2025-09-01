package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing

class Text(val text: () -> String, val color: Int = -1, val centered: Boolean = false) : Renderable() {
    constructor(text: String, color: Int = -1, centered: Boolean = false) : this({ text }, color, centered)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.push()
        screenDrawing.translate(0f, getHeight() / 2f - screenDrawing.getTextHeight() / 2f)
        if (centered) {
            screenDrawing.drawCenteredText(text(), getX() + getWidth() / 2, getY(), -1)
        } else {
            screenDrawing.drawText(text(), getX(), getY(), -1)
        }
        screenDrawing.pop()
    }
}