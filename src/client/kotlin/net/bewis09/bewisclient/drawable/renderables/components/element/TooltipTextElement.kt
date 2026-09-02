package net.bewis09.bewisclient.drawable.renderables.components.element

import net.bewis09.bewisclient.drawable.Initializer
import net.bewis09.renderite.logic.Color
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipElement
import net.bewis09.renderite.RenderiteElement.Props
import net.bewis09.renderite.logic.TextAlign
import net.bewis09.renderite.logic.within
import net.minecraft.network.chat.Component

class TooltipTextElement(p: Props<TooltipTextElement>) : TooltipElement<TooltipTextElement>(p) {
    var textProvider = { text }
    lateinit var text: Component
    var color: Color? = null
    var hoverColor: Color? = null
    var centered: Boolean = false
    var onClick: (() -> Unit)? = null

    init { props() }

    override fun Init.init() {
        Text {
            textProvider = this@TooltipTextElement.textProvider
            colorProvider = { hoverFactor within (require { this@TooltipTextElement.color } to require { hoverColor }) }
            textAlign = if (centered) TextAlign.CENTER else TextAlign.START
        }
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean = onClick?.let { it() } != null
}

fun Initializer.TooltipHoverableText(p: Props<TooltipTextElement>) = TooltipTextElement(p).add()