package net.bewis09.bewisclient.widget.types

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.DefaultWidgetSettings

abstract class LineWidget(): ScalableWidget() {
    var backgroundColor = DefaultWidgetSettings.backgroundColor.cloneWithDefault()
    var backgroundOpacity = DefaultWidgetSettings.backgroundOpacity.cloneWithDefault()
    var borderColor = DefaultWidgetSettings.borderColor.cloneWithDefault()
    var borderOpacity = DefaultWidgetSettings.borderOpacity.cloneWithDefault()
    var paddingSize = DefaultWidgetSettings.paddingSize.cloneWithDefault()
    var lineSpacing = DefaultWidgetSettings.lineSpacing.cloneWithDefault()
    var textColor = DefaultWidgetSettings.textColor.cloneWithDefault()
    var borderRadius = DefaultWidgetSettings.borderRadius.cloneWithDefault()

    init {
        create("text_color", textColor)
        create("background_color", backgroundColor)
        create("background_opacity", backgroundOpacity)
        create("border_color", borderColor)
        create("border_opacity", borderOpacity)
        create("border_radius", borderRadius)
        create("padding_size", paddingSize)
        create("line_spacing", lineSpacing)
    }

    open fun hasMultipleLines(): Boolean = false

    abstract fun getLines(): List<String>
    open fun isCentered(): Boolean = true

    override fun render(screenDrawing: ScreenDrawing) {
        val textColor = textColor.get().getColor()
        val backgroundColor = backgroundColor.get().getColor()
        val backgroundOpacity = backgroundOpacity.get()
        val borderColor = borderColor.get().getColor()
        val borderOpacity = borderOpacity.get()
        val borderRadius = borderRadius.get()
        val paddingSize = paddingSize.get()
        val lineSpacing = lineSpacing.get()

        val lines = getLines()
        if (lines.isEmpty()) return

        screenDrawing.fillWithBorderRounded(0, 0, getWidth(), getHeight(), borderRadius, backgroundColor, backgroundOpacity, borderColor, borderOpacity)

        lines.forEach { line ->
            val y = (lines.indexOf(line) * (9 + lineSpacing)) + paddingSize
            if (isCentered()) {
                screenDrawing.drawCenteredText(line, getWidth() / 2, y, textColor, 1f)
            } else {
                screenDrawing.drawText(line, paddingSize, y, textColor, 1f)
            }
        }
    }

    override fun getHeight(): Int {
        val paddingSize = paddingSize.get()
        val lineSpacing = lineSpacing.get()

        val lines = getLines()
        if (lines.isEmpty()) return 0

        return lines.size * (9 + lineSpacing) + 2 * paddingSize - lineSpacing - 2
    }

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(backgroundColor.createRenderableWithFader("widget.background", "Background", "Set the color and opacity of the widget", backgroundOpacity))
        list.add(borderColor.createRenderableWithFader("widget.border", "Border", "Set the color and opacity of the widget's border", borderOpacity))
        list.add(paddingSize.createRenderable("widget.padding_size", "Padding Size", "Set the padding at the edge of the widget to the text"))
        if (hasMultipleLines())
            list.add(lineSpacing.createRenderable("widget.line_spacing", "Line Spacing", "Set the spacing between lines of text in the widget"))
        list.add(textColor.createRenderable("widget.text_color", "Text Color", "Set the color of the text in the widget"))
        list.add(borderRadius.createRenderable("widget.border_radius", "Border Radius", "Set the radius of the widget's border corners"))
        super.appendSettingsRenderables(list)
    }
}