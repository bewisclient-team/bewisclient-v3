package net.bewis09.bewisclient.drawable.renderables.components.element

import net.bewis09.bewisclient.drawable.Init
import net.bewis09.renderite.logic.Color
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.RenderiteElement.Props
import net.bewis09.renderite.logic.within
import net.minecraft.network.chat.Component

class TooltipHoverableTextElement(p: Props<TooltipHoverableTextElement>) : TooltipHoverable<TooltipHoverableTextElement>(p) {
    var textProvider = { text }
    lateinit var text: Component
    var color: Color? = null
    var hoverColor: Color? = null
    var centered: Boolean = false
    var onClick: (() -> Unit)? = null

    init { props() }

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val color = require { color }
        val hoverColor = require { hoverColor }

        if (centered) {
            screenDrawing.drawCenteredText(textProvider(), exactCenterX, screenDrawing.getTextYCenter(this), hoverFactor within (color to hoverColor))
        } else {
            screenDrawing.drawText(textProvider(), x, screenDrawing.getTextYCenter(this), hoverFactor within (color to hoverColor))
        }
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean = onClick?.let { it() } != null
}

fun Init.TooltipHoverableText(p: Props<TooltipHoverableTextElement>) = TooltipHoverableTextElement(p).add()