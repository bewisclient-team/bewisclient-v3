package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.color.Color
import net.bewis09.bewisclient.logic.color.within

class TooltipHoverableText(val text: Translation, val color: Color, val hoverColor: Color = color, tooltip: Translation? = null, val centered: Boolean = false, val onClick: (() -> Unit)? = null) : TooltipHoverable(tooltip) {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.push()
        screenDrawing.translate(0f, getHeight() / 2f - screenDrawing.getTextHeight() / 2f)
        if (centered) {
            screenDrawing.drawCenteredText(text.getTranslatedString(), getX() + getWidth() / 2, getY(), hoverAnimation["hovering"] within (color to hoverColor))
        } else {
            screenDrawing.drawText(text.getTranslatedString(), getX(), getY(), hoverAnimation["hovering"] within (color to hoverColor))
        }
        screenDrawing.pop()
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return onClick?.let { it() } != null
    }
}