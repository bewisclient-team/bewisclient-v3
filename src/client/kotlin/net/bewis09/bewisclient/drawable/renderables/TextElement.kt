package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.core.Identifier
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.util.color.Color
import net.minecraft.network.chat.Component

class TextElement(val text: () -> Component, val color: () -> Color, val centered: Boolean = false, val font: Identifier? = null) : Renderable() {
    constructor(text: Component, color: () -> Color, centered: Boolean = false, font: Identifier? = null) : this({ text }, color, centered, font)
    constructor(text: () -> Component, color: Color = Color.WHITE, centered: Boolean = false, font: Identifier? = null) : this(text, { color }, centered, font)
    constructor(text: Component, color: Color = Color.WHITE, centered: Boolean = false, font: Identifier? = null) : this({ text }, { color }, centered, font)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.translate(0f, height / 2f - screenDrawing.getTextHeight() / 2f) {
            if (centered) {
                screenDrawing.drawCenteredText(text(), centerX, y, color(), font)
            } else {
                screenDrawing.drawText(text(), x, y, color(), font)
            }
        }
    }
}