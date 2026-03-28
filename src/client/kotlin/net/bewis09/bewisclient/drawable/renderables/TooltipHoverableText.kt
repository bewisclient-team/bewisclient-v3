package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.color.within
import net.minecraft.network.chat.Component

class TooltipHoverableText(val text: Component, val color: Color, val hoverColor: Color = color, tooltip: Component? = null, val centered: Boolean = false, val onClick: (() -> Unit)? = null) : TooltipHoverable(tooltip) {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.translate(0f, height / 2f - screenDrawing.getTextHeight() / 2f) {
            if (centered) {
                screenDrawing.drawCenteredText(text, centerX, y, hoverFactor within (color to hoverColor))
            } else {
                screenDrawing.drawText(text, x, y, hoverFactor within (color to hoverColor))
            }
        }
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return onClick?.let { it() } != null
    }
}