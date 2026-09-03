package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.TextAlign
import net.bewis09.renderite.style.RenderiteChild
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
        val lines = screenDrawing.wrapText(text, width - padding * 2)
        if (selfResize) updateHeight(lines.size * 9 + padding * 2)
    }

    override fun init() {
        Text {
            text = this@InfoTextRenderable.text
            wrap = true
            padding = this@InfoTextRenderable.padding
            color = this@InfoTextRenderable.color
            textAlign = if (centered) TextAlign.CENTER else TextAlign.START
            verticalAlign = TextAlign.START
        }
    }
}

@RenderiteChild
fun Renderable.InfoTextRenderable(p: RenderiteElement.Props<InfoTextRenderable>) = net.bewis09.bewisclient.drawable.renderables.settings.InfoTextRenderable(p).also(::addRenderable)