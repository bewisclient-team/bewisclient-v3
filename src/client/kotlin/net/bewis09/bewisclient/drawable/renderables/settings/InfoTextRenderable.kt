package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.Init
import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.logic.Color
import net.minecraft.network.chat.Component
import kotlin.also

class InfoTextRenderable(p: Props<InfoTextRenderable>) : PropedRenderable<InfoTextRenderable>(p) {
    lateinit var text: Component
    var color: Color = General.getThemeColor()
    var centered: Boolean = false
    var selfResize: Boolean = true
    var padding: Int = 5

    init { props() }

    override fun renderLogic(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val lines = screenDrawing.wrapText(text.string, width - padding * 2)
        if (selfResize) updateHeight(lines.size * screenDrawing.getTextHeight() + padding * 2)
    }

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val lines = screenDrawing.wrapText(text.string, width - padding * 2)
        lines.forEachIndexed { index, line ->
            if (centered) {
                screenDrawing.drawCenteredText(line, centerX, y + index * screenDrawing.getTextHeight() + padding, color)
            } else {
                screenDrawing.drawText(line, x, y + index * screenDrawing.getTextHeight() + padding, color)
            }
        }
    }
}

fun Init.InfoTextRenderable(p: RenderiteElement.Props<InfoTextRenderable>) = net.bewis09.bewisclient.drawable.renderables.settings.InfoTextRenderable(p).also(::addRenderable)