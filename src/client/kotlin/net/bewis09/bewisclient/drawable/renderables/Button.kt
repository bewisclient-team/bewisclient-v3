package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.game.Translation

class Button(val text: String, val onClick: (Button) -> Unit, tooltip: Translation? = null, val selected: (() -> Boolean)? = null) : TooltipHoverable(tooltip) {
    constructor(text: String, onClick: (Button) -> Unit) : this(text, onClick, null)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.fillWithBorderRounded(getX(), getY(), getWidth(), getHeight(), 5, 0xFFFFFF, hoverAnimation["hovering"] * 0.15f + 0.15f, 0xAAAAAA, if (selected?.invoke() == true) 0.5f else 0f)
        screenDrawing.push()
        screenDrawing.translate(0f, getHeight() / 2f - screenDrawing.getTextHeight() / 2f)
        screenDrawing.drawCenteredText(text, getX() + getWidth() / 2, getY(), 0xFFFFFF, 1.0F)
        screenDrawing.pop()
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        onClick(this)
        return true
    }
}