package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.color.times
import java.awt.Color

class InfoTextRenderable(val text: String, val color: Color = Color.WHITE, val centered: Boolean = false, val selfResize: Boolean = true) : Renderable() {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val lines = screenDrawing.wrapText(text, getWidth())
        lines.forEachIndexed { index, line ->
            if (centered) {
                screenDrawing.drawCenteredText(line, getX() + getWidth() / 2, getY() + index * screenDrawing.getTextHeight() + 5, color * OptionsMenuSettings.themeColor.get().getColor())
            } else {
                screenDrawing.drawText(line, getX(), getY() + index * screenDrawing.getTextHeight() + 5, color * OptionsMenuSettings.themeColor.get().getColor())
            }
        }
        if (selfResize) setHeight(lines.size * screenDrawing.getTextHeight() + 10)
    }
}