package net.bewis09.bewisclient.widget.logic

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.WidgetLoader

class RelativePosition(val parent: String, val side: String): WidgetPosition {
    override fun getX(widget: Widget): Float {
        val parentWidget = WidgetLoader.widgets.find { it.getId().toString() == parent } ?: return 0f
        val gap = widget.getSettings().widgetSettings.defaults.gap.get()

        return when (side) {
            "left" -> parentWidget.getX() - widget.getScaledWidth() - gap
            "right" -> parentWidget.getX() + parentWidget.getScaledWidth() + gap
            "top" -> (parentWidget.position ?: parentWidget.defaultPosition()).getX(widget)
            "bottom" -> (parentWidget.position ?: parentWidget.defaultPosition()).getX(widget)
            else -> 0f
        }
    }

    override fun getY(widget: Widget): Float {
        val parentWidget = WidgetLoader.widgets.find { it.getId().toString() == parent } ?: return 0f
        val gap = widget.getSettings().widgetSettings.defaults.gap.get()

        return when (side) {
            "left" -> (parentWidget.position ?: parentWidget.defaultPosition()).getY(widget)
            "right" -> (parentWidget.position ?: parentWidget.defaultPosition()).getY(widget)
            "top" -> parentWidget.getY() - widget.getScaledHeight() - gap
            "bottom" -> parentWidget.getY() + parentWidget.getScaledHeight() + gap
            else -> 0f
        }
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