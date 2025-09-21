package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.color.Color

class InfoTextRenderable(val text: String, val color: Color = Color.WHITE, val centered: Boolean = false, val selfResize: Boolean = true, val padding: Int = 5) : Renderable() {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val lines = screenDrawing.wrapText(text, width)
        lines.forEachIndexed { index, line ->
            if (centered) {
                screenDrawing.drawCenteredText(line, centerX, y + index * screenDrawing.getTextHeight() + padding, color * OptionsMenuSettings.themeColor.get().getColor())
            } else {
                screenDrawing.drawText(line, x, y + index * screenDrawing.getTextHeight() + padding, color * OptionsMenuSettings.themeColor.get().getColor())
            }
        }
        if (selfResize) setHeight(lines.size * screenDrawing.getTextHeight() + padding * 2)
    }
}