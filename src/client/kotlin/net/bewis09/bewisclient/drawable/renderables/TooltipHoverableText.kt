package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.drawable.interpolateColor
import net.bewis09.bewisclient.game.Translation

class TooltipHoverableText(val text: Translation, val color: Int, val hoverColor: Int = color, tooltip: Translation? = null, val centered: Boolean = false, val onClick: (() -> Unit)? = null) : TooltipHoverable(tooltip) {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.push()
        screenDrawing.translate(0f,getHeight() / 2f - screenDrawing.getTextHeight() / 2f)
        if (centered) {
            screenDrawing.drawCenteredText(text.getTranslatedString(), getX() + getWidth() / 2, getY(), interpolateColor(color, hoverColor, hoverAnimation["hovering"]), 1.0F)
        } else {
            screenDrawing.drawText(text.getTranslatedString(), getX(), getY(), interpolateColor(color, hoverColor, hoverAnimation["hovering"]), 1.0F)
        }
        screenDrawing.pop()
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return onClick?.let { it() } != null
    }
}