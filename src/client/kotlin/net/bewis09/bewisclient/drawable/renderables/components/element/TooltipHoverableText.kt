package net.bewis09.bewisclient.drawable.renderables.components.element

import net.bewis09.renderite.logic.Color
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.logic.within
import net.minecraft.network.chat.Component

class TooltipHoverableText(val text: Component, val color: Color, val hoverColor: Color = color, tooltip: Component? = null, val centered: Boolean = false, val onClick: (() -> Unit)? = null) : TooltipHoverable(tooltip) {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        if (centered) {
            screenDrawing.drawCenteredText(text, exactCenterX, screenDrawing.getTextYCenter(this), hoverFactor within (color to hoverColor))
        } else {
            screenDrawing.drawText(text, x, screenDrawing.getTextYCenter(this), hoverFactor within (color to hoverColor))
        }
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean = onClick?.let { it() } != null
}