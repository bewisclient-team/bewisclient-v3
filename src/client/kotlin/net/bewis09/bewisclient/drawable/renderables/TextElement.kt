package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.logic.color.Color

class TextElement(val text: () -> String, val color: () -> Color, val centered: Boolean = false) : Renderable() {
    constructor(text: String, color: () -> Color, centered: Boolean = false) : this({ text }, color, centered)
    constructor(text: () -> String, color: Color = Color.WHITE, centered: Boolean = false) : this(text, { color }, centered)
    constructor(text: String, color: Color = Color.WHITE, centered: Boolean = false) : this({ text }, { color }, centered)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.translate(0f, getHeight() / 2f - screenDrawing.getTextHeight() / 2f) {
            if (centered) {
                screenDrawing.drawCenteredText(text(), getX() + getWidth() / 2, getY(), color())
            } else {
                screenDrawing.drawText(text(), getX(), getY(), color())
            }
        }
    }
}