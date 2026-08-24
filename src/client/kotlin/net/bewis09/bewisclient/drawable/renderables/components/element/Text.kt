package net.bewis09.bewisclient.drawable.renderables.components.element

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.renderables.components.logic.TextAlign
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.logic.Color
import net.minecraft.network.chat.Component

class Text(p: Props<Text>) : PropedRenderable<Text>(p) {
    var textProvider: () -> Component = { text }
    var colorProvider: () -> Color = { require { color } }
    lateinit var text: Component
    var color: Color? = General.getTextThemeColor()
    var font: Identifier? = null
    var selfResize = false
    var textAlign = TextAlign.START
    var verticalAlign = TextAlign.CENTER

    init { props() }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        if (selfResize) {
            width = screenDrawing.getTextWidth(textProvider(), font)
        }

        val y = when (verticalAlign) {
            TextAlign.START -> this.y
            TextAlign.CENTER -> screenDrawing.getTextYCenter(this)
            TextAlign.END -> this.y2 - screenDrawing.getTextHeight()
        }

        when (textAlign) {
            TextAlign.CENTER -> screenDrawing.drawCenteredText(textProvider(), exactCenterX, y, colorProvider(), font)
            TextAlign.START -> screenDrawing.drawText(textProvider(), x, y, colorProvider(), font)
            else -> screenDrawing.drawRightAlignedText(textProvider(), x2, y, colorProvider(), font)
        }
    }
}