package net.bewis09.bewisclient.widget.types

import com.google.gson.JsonObject
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.logic.catch
import net.bewis09.bewisclient.logic.color.ColorSaver

abstract class LineWidget(): ScalableWidget() {
    var textColor: ColorSaver? = null
    var backgroundColor: ColorSaver? = null
    var borderColor: ColorSaver? = null
    var borderRadius: Int? = null
    var paddingSize: Int? = null
    var lineSpacing: Int? = null

    abstract fun getLines(): List<String>
    open fun isCentered(): Boolean = true

    override fun render(screenDrawing: ScreenDrawing) {
        val textColor = textColor?.getColor() ?: getSettings().widgetSettings.defaults.textColor.get().getColor()
        val backgroundColor = backgroundColor?.getColor() ?: getSettings().widgetSettings.defaults.backgroundColor.get().getColor()
        val borderColor = borderColor?.getColor() ?: getSettings().widgetSettings.defaults.borderColor.get().getColor()
        val borderRadius = borderRadius ?: getSettings().widgetSettings.defaults.borderRadius.get()
        val paddingSize = paddingSize ?: getSettings().widgetSettings.defaults.paddingSize.get()
        val lineSpacing = lineSpacing ?: getSettings().widgetSettings.defaults.lineSpacing.get()

        val lines = getLines()
        if (lines.isEmpty()) return

        screenDrawing.fillWithBorderRounded(0, 0, getWidth(), getHeight(), borderRadius, backgroundColor, borderColor)

        lines.forEach { line ->
            val y = (lines.indexOf(line) * (9 + lineSpacing)) + paddingSize
            if (isCentered()) {
                screenDrawing.drawCenteredText(line, getWidth() / 2, y, textColor)
            } else {
                screenDrawing.drawText(line, paddingSize, y, textColor)
            }
        }
    }

    override fun saveProperties(properties: JsonObject) {
        textColor?.convertToElement()?.let { properties.add("text_color", it) }
        backgroundColor?.convertToElement()?.let { properties.add("background_color", it) }
        borderColor?.convertToElement()?.let { properties.add("border_color", it) }
        borderRadius?.let { properties.addProperty("border_radius", it) }
        paddingSize?.let { properties.addProperty("padding_size", it) }

        super.saveProperties(properties)
    }

    override fun loadProperties(properties: JsonObject) {
        textColor = ColorSaver.fromJson(properties.get("text_color"))
        backgroundColor = ColorSaver.fromJson(properties.get("background_color"))
        borderColor = ColorSaver.fromJson(properties.get("border_color"))
        borderRadius = catch { properties.get("border_radius")?.asInt }
        paddingSize = catch { properties.get("padding_size")?.asInt }

        super.loadProperties(properties)
    }

    override fun getHeight(): Int {
        val paddingSize = paddingSize ?: getSettings().widgetSettings.defaults.paddingSize.get()
        val lineSpacing = lineSpacing ?: getSettings().widgetSettings.defaults.lineSpacing.get()

        val lines = getLines()
        if (lines.isEmpty()) return 0

        return lines.size * (9 + lineSpacing) + 2 * paddingSize - lineSpacing - 2
    }
}