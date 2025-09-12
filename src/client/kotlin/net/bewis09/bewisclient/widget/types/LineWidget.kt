package net.bewis09.bewisclient.widget.types

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.DefaultWidgetSettings
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper

abstract class LineWidget(id: Identifier) : ScalableWidget(id) {
    private var lineWidth = 0

    companion object {
        private val EMPTY = Text.empty()
    }

    val backgroundColor = create("background_color", DefaultWidgetSettings.backgroundColor.cloneWithDefault())
    val backgroundOpacity = create("background_opacity", DefaultWidgetSettings.backgroundOpacity.cloneWithDefault())
    val borderColor = create("border_color", DefaultWidgetSettings.borderColor.cloneWithDefault())
    val borderOpacity = create("border_opacity", DefaultWidgetSettings.borderOpacity.cloneWithDefault())
    val paddingSize = create("padding_size", DefaultWidgetSettings.paddingSize.cloneWithDefault())
    val shadow = create("shadow", DefaultWidgetSettings.shadow.cloneWithDefault())
    val lineSpacing = create("line_spacing", DefaultWidgetSettings.lineSpacing.cloneWithDefault())
    val textColor = create("text_color", DefaultWidgetSettings.textColor.cloneWithDefault())
    val borderRadius = create("border_radius", DefaultWidgetSettings.borderRadius.cloneWithDefault())

    open fun hasMultipleLines(): Boolean = getLine() === EMPTY

    open fun getLines() = listOf(getLine())
    open fun getLine(): Text = EMPTY

    open fun isCentered(): Boolean = true

    override fun render(screenDrawing: ScreenDrawing) {
        val textColor = textColor.get().getColor()
        val backgroundColor = backgroundColor.get().getColor()
        val backgroundOpacity = backgroundOpacity.get()
        val borderColor = borderColor.get().getColor()
        val borderOpacity = borderOpacity.get()
        val borderRadius = borderRadius.get()
        val shadow = shadow.get()
        val paddingSize = paddingSize.get()
        val lineSpacing = lineSpacing.get()

        val lines = getLines()
        if (lines.isEmpty()) return

        lineWidth = lines.maxOfOrNull { screenDrawing.getTextWidth(it) }?.plus(2 * paddingSize) ?: 0

        screenDrawing.fillWithBorderRounded(
            0, 0, getWidth(), getHeight(), borderRadius, backgroundColor alpha backgroundOpacity, borderColor alpha borderOpacity
        )

        lines.forEach { line ->
            val y = (lines.indexOf(line) * (9 + lineSpacing)) + paddingSize
            if (!shadow) {
                if (isCentered()) {
                    screenDrawing.drawCenteredText(line, getWidth() / 2, y, textColor)
                } else {
                    screenDrawing.drawText(line, paddingSize, y, textColor)
                }
            } else {
                if (isCentered()) {
                    screenDrawing.drawCenteredTextWithShadow(line, getWidth() / 2, y, textColor)
                } else {
                    screenDrawing.drawTextWithShadow(line, paddingSize, y, textColor)
                }
            }
        }
    }

    final override fun getWidth(): Int {
        return MathHelper.clamp(lineWidth, getMinimumWidth(), getMaximumWidth())
    }

    abstract fun getMinimumWidth(): Int

    open fun getMaximumWidth(): Int = getMinimumWidth()

    override fun getHeight(): Int {
        val paddingSize = paddingSize.get()
        val lineSpacing = lineSpacing.get()

        val lines = getLines()
        if (lines.isEmpty()) return 0

        return lines.size * (9 + lineSpacing) + 2 * paddingSize - lineSpacing - 2
    }

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(
            backgroundColor.createRenderableWithFader(
                "widget.background", "Background", "Set the color and opacity of the widget", backgroundOpacity
            )
        )
        list.add(borderColor.createRenderableWithFader("widget.border", "Border", "Set the color and opacity of the widget's border", borderOpacity))
        list.add(
            paddingSize.createRenderable(
                "widget.padding_size", "Padding Size", "Set the padding at the edge of the widget to the text"
            )
        )
        if (hasMultipleLines()) list.add(
            lineSpacing.createRenderable(
                "widget.line_spacing", "Line Spacing", "Set the spacing between lines of text in the widget"
            )
        )
        list.add(
            textColor.createRenderable(
                "widget.text_color", "Text Color", "Set the color of the text in the widget"
            )
        )
        list.add(
            borderRadius.createRenderable(
                "widget.border_radius", "Border Radius", "Set the radius of the widget's border corners"
            )
        )
        list.add(
            shadow.createRenderable(
                "widget.text_shadow", "Text Shadow", "Set whether text in the widget has a shadow"
            )
        )
        super.appendSettingsRenderables(list)
    }
}
