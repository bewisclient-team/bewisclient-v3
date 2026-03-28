package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.util.color.Color
import net.minecraft.network.chat.Component

class InfoTextRenderable(val text: Component, val color: Color = OptionsMenuSettings.getThemeColor(), val centered: Boolean = false, val selfResize: Boolean = true, val padding: Int = 5) : Renderable() {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val lines = screenDrawing.wrapText(text.string, width)
        lines.forEachIndexed { index, line ->
            if (centered) {
                screenDrawing.drawCenteredText(line, centerX, y + index * screenDrawing.getTextHeight() + padding, color)
            } else {
                screenDrawing.drawText(line, x, y + index * screenDrawing.getTextHeight() + padding, color)
            }
        }
        if (selfResize) setHeight(lines.size * screenDrawing.getTextHeight() + padding * 2)
    }
}