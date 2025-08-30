package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing

class InfoTextRenderable(val text: String, val color: Int = -1, val centered: Boolean = false, val selfResize: Boolean = true) : Renderable() {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val lines = screenDrawing.wrapText(text, getWidth())
        lines.forEachIndexed { index, line ->
            if (centered) {
                screenDrawing.drawCenteredText(line, getX() + getWidth() / 2, getY() + index * screenDrawing.getTextHeight() + 5, color)
            } else {
                screenDrawing.drawText(line, getX(), getY() + index * screenDrawing.getTextHeight() + 5, color)
            }
        }
        if (selfResize) setHeight(lines.size * screenDrawing.getTextHeight() + 10)
    }
}