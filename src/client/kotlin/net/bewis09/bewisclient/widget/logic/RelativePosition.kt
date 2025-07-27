package net.bewis09.bewisclient.widget.logic

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.WidgetLoader

class RelativePosition(val parent: String, val side: String): WidgetPosition {
    override fun getX(widget: Widget): Int {
        val parentWidget = WidgetLoader.widgets.find { it.getId().toString() == parent } ?: return 0
        val gap = widget.getSettings().widgetSettings.defaults.gap.get()

        return when (side) {
            "left" -> parentWidget.getX() * parentWidget.getScale() / widget.getScale() - widget.getWidth() - gap
            "right" -> parentWidget.getX() * parentWidget.getScale() / widget.getScale() + parentWidget.getWidth() + gap
            "top" -> parentWidget.getX() * parentWidget.getScale() / widget.getScale()
            "bottom" -> parentWidget.getX() * parentWidget.getScale() / widget.getScale()
            else -> 0
        }.toInt()
    }

    override fun getY(widget: Widget): Int {
        val parentWidget = WidgetLoader.widgets.find { it.getId().toString() == parent } ?: return 0
        val gap = widget.getSettings().widgetSettings.defaults.gap.get()

        return when (side) {
            "left" -> parentWidget.getY() * parentWidget.getScale() / widget.getScale()
            "right" -> parentWidget.getY() * parentWidget.getScale() / widget.getScale()
            "top" -> parentWidget.getY() * parentWidget.getScale() / widget.getScale() - widget.getHeight() - gap
            "bottom" -> parentWidget.getY() * parentWidget.getScale() / widget.getScale() + parentWidget.getHeight() + gap
            else -> 0
        }.toInt()
    }

    override fun saveToJson(): JsonElement = JsonObject().also {
        it.addProperty("side", side)
        it.addProperty("parent", parent)
    }

    override fun getType(): String = "relative"

    object Factory: WidgetPositionFactory<RelativePosition> {
        override fun createFromJson(jsonElement: JsonElement): RelativePosition? {
            if (!jsonElement.isJsonObject) return null

            val jsonObject = jsonElement.asJsonObject
            val parent = jsonObject.get("parent")?.asString ?: return null
            val side = jsonObject.get("side")?.asString ?: return null

            return RelativePosition(parent, side)
        }

        override fun getType(): String = "relative"
    }
}