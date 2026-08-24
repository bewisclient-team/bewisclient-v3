package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.logic.Color
import net.minecraft.network.chat.Component

class InfoTextRenderable(p: Props<InfoTextRenderable>) : PropedRenderable<InfoTextRenderable>(p) {
    lateinit var text: Component
    var color: Color = General.getThemeColor()
    var centered: Boolean = false
    var selfResize: Boolean = true
    var padding: Int = 5

    init { props() }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val lines = screenDrawing.wrapText(text.string, width)
        lines.forEachIndexed { index, line ->
            if (centered) {
                screenDrawing.drawCenteredText(line, centerX, y + index * screenDrawing.getTextHeight() + padding, color)
            } else {
                screenDrawing.drawText(line, x, y + index * screenDrawing.getTextHeight() + padding, color)
            }
        }
        if (selfResize) updateHeight(lines.size * screenDrawing.getTextHeight() + padding * 2)
    }
}