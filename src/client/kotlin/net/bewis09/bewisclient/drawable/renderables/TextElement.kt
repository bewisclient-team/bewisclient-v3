package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.logic.color.Color
import net.minecraft.text.Style
import net.minecraft.text.Text

class TextElement(val text: () -> Text, val color: () -> Color, val centered: Boolean = false, val style: Style? = null) : Renderable() {
    constructor(text: Text, color: () -> Color, centered: Boolean = false, style: Style? = null) : this({ text }, color, centered, style)
    constructor(text: () -> Text, color: Color = Color.WHITE, centered: Boolean = false, style: Style? = null) : this(text, { color }, centered, style)
    constructor(text: Text, color: Color = Color.WHITE, centered: Boolean = false, style: Style? = null) : this({ text }, { color }, centered, style)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val s = screenDrawing.style
        if (style != null) screenDrawing.style = style
        screenDrawing.translate(0f, height / 2f - screenDrawing.getTextHeight() / 2f) {
            if (centered) {
                screenDrawing.drawCenteredText(text(), centerX, y, color())
            } else {
                screenDrawing.drawText(text(), x, y, color())
            }
        }
        screenDrawing.style = s
    }
}