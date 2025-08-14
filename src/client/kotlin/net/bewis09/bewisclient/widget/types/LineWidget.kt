package net.bewis09.bewisclient.widget.types

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing

abstract class LineWidget(): ScalableWidget() {
    var backgroundColor = getSettings().widgetSettings.defaults.backgroundColor.cloneWithDefault()
    var backgroundOpacity = getSettings().widgetSettings.defaults.backgroundOpacity.cloneWithDefault()
    var borderColor = getSettings().widgetSettings.defaults.borderColor.cloneWithDefault()
    var borderOpacity = getSettings().widgetSettings.defaults.borderOpacity.cloneWithDefault()
    var paddingSize = getSettings().widgetSettings.defaults.paddingSize.cloneWithDefault()
    var lineSpacing = getSettings().widgetSettings.defaults.lineSpacing.cloneWithDefault()
    var textColor = getSettings().widgetSettings.defaults.textColor.cloneWithDefault()
    var borderRadius = getSettings().widgetSettings.defaults.borderRadius.cloneWithDefault()

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
        list.add(backgroundColor.createRenderable("widget.background_color", "Background Color"))
        list.add(backgroundOpacity.createRenderable("widget.background_opacity", "Background Opacity"))
        list.add(borderColor.createRenderable("widget.border_color", "Border Color"))
        list.add(borderOpacity.createRenderable("widget.border_opacity", "Border Opacity"))
        list.add(paddingSize.createRenderable("widget.padding_size", "Padding Size"))
        list.add(lineSpacing.createRenderable("widget.line_spacing", "Line Spacing"))
        list.add(textColor.createRenderable("widget.text_color", "Text Color"))
        list.add(borderRadius.createRenderable("widget.border_radius", "Border Radius"))
        super.appendSettingsRenderables(list)
    }
}