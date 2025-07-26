package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.ScreenDrawing

class Button(val text: String, val onClick: (Button) -> Unit): Hoverable() {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.fillRounded(getX(), getY(), getWidth(), getHeight(), 5, 0xFFFFFF, hoverAnimation["hovering"] * 0.15f + 0.15f)
        screenDrawing.push()
        screenDrawing.translate(0f,getHeight() / 2f - screenDrawing.getTextHeight() / 2f)
        screenDrawing.drawCenteredText(text, getX() + getWidth() / 2, getY(), 0xFFFFFF, 1.0F)
        screenDrawing.pop()
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        onClick(this)
        return true
    }
}