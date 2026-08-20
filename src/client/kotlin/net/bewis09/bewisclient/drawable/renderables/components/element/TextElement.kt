package net.bewis09.bewisclient.drawable.renderables.components.element

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.logic.Color
import net.minecraft.network.chat.Component

class TextElement(val text: () -> Component, val color: () -> Color, val centered: Boolean = false, val font: Identifier? = null) : Renderable() {
    constructor(text: Component, color: () -> Color, centered: Boolean = false, font: Identifier? = null) : this({ text }, color, centered, font)
    constructor(text: () -> Component, color: Color = Color.WHITE, centered: Boolean = false, font: Identifier? = null) : this(text, { color }, centered, font)
    constructor(text: Component, color: Color = Color.WHITE, centered: Boolean = false, font: Identifier? = null) : this({ text }, { color }, centered, font)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        if (centered) {
            screenDrawing.drawCenteredText(text(), exactCenterX, screenDrawing.getTextYCenter(this), color(), font)
        } else {
            screenDrawing.drawText(text(), x, screenDrawing.getTextYCenter(this), color(), font)
        }
    }
}