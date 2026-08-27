package net.bewis09.renderite.components

import net.bewis09.renderite.logic.TextAlign
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.drawer.RenderiteDrawer
import net.bewis09.renderite.logic.Color

class Text<S: RenderiteDrawer<I, T, F>, T: Any, F, I: Any>(p: Props<Text<S, T, F, I>>) : RenderiteElement<S, Text<S, T, F, I>, T, F, I>(p) {
    var textProvider: () -> T = { text }
    var colorProvider: () -> Color = { require { color } }
    lateinit var text: T
    var color: Color? = General.getTextThemeColor()
    var font: F? = null
    var selfResize = false
    var textAlign = TextAlign.START
    var verticalAlign = TextAlign.CENTER
    var padding: Int = 0
    var verticalPadding: Int? = null
    var horizontalPadding: Int? = null
    var paddingLeft: Int? = null
    var paddingTop: Int? = null
    var paddingRight: Int? = null
    var paddingBottom: Int? = null
    var wrap = false
    var lineHeight = 1f

    init { props() }

    override fun renderLogic(screenDrawing: S, mouseX: Int, mouseY: Int) {
        if (selfResize) {
            width = screenDrawing.getTextWidth(textProvider(), font)
        }
    }

    override fun renderElement(screenDrawing: S, mouseX: Int, mouseY: Int) {
        val paddingTop = paddingTop ?: verticalPadding ?: padding
        val paddingBottom = paddingBottom ?: verticalPadding ?: padding
        val paddingLeft = paddingLeft ?: horizontalPadding ?: padding
        val paddingRight = paddingRight ?: horizontalPadding ?: padding

        val lines = if (wrap) screenDrawing.wrapText(textProvider(), width - paddingLeft - paddingRight, font) else listOf(textProvider())

        val y = when (verticalAlign) {
            TextAlign.START -> this.y.toFloat() + (paddingTop)
            TextAlign.CENTER -> centerY - screenDrawing.getTextHeight() * lines.size / 2f * lineHeight + (paddingTop) / 2f - (paddingBottom) / 2f
            TextAlign.END -> this.y2.toFloat() - screenDrawing.getTextHeight() * lines.size * lineHeight - (paddingBottom)
        }

        when (textAlign) {
            TextAlign.CENTER -> screenDrawing.drawCenteredWrappedText(lines, exactCenterX + (paddingLeft) / 2 - (paddingRight) / 2, y, colorProvider(), font, (lineHeight * screenDrawing.getTextHeight()).toInt())
            TextAlign.START -> screenDrawing.drawWrappedText(lines, x + (paddingLeft), y, colorProvider(), font, (lineHeight * screenDrawing.getTextHeight()).toInt())
            TextAlign.END -> screenDrawing.drawRightAlignedWrappedText(lines, x2 - (paddingRight), y, colorProvider(), font, (lineHeight * screenDrawing.getTextHeight()).toInt())
        }
    }
}