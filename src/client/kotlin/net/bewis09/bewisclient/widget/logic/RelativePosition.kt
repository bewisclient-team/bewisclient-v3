package net.bewis09.bewisclient.widget.logic

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.bewis09.bewisclient.impl.settings.DefaultWidgetSettings
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.WidgetLoader

class RelativePosition(val parent: String, val side: String) : WidgetPosition {
    val parentWidget by lazy { WidgetLoader.widgets.find { it.getId().toString() == parent } }

    override fun getX(widget: Widget): Float {
        val parent = parentWidget ?: return 0f

        if (isInDependencyStack(widget)) return 0f

        if (!parent.isShowing()) {
            return parent.position.get().getX(widget)
        }

        val gap = DefaultWidgetSettings.gap.get()

        return when (side) {
            "left" -> parent.getX() - widget.getScaledWidth() - gap
            "right" -> parent.getX() + parent.getScaledWidth() + gap
            "top" -> parent.position.get().getX(widget)
            "bottom" -> parent.position.get().getX(widget)
            else -> 0f
        }
    }

    override fun getY(widget: Widget): Float {
        val parent = parentWidget ?: return 0f

        if (isInDependencyStack(widget)) return 0f

        if (!parent.isShowing()) {
            return parent.position.get().getY(widget)
        }

        val gap = DefaultWidgetSettings.gap.get()

        return when (side) {
            "left" -> parent.position.get().getY(widget)
            "right" -> parent.position.get().getY(widget)
            "top" -> parent.getY() - widget.getScaledHeight() - gap
            "bottom" -> parent.getY() + parent.getScaledHeight() + gap
            else -> 0f
        }
    }

    fun isInDependencyStack(widget: Widget): Boolean {
        var latest = parentWidget

        while (latest != null && latest != widget) {
            latest = (latest.position.get() as? RelativePosition)?.parentWidget
        }

        return latest == widget
    }

    override fun saveToJson(): JsonElement = JsonObject().also {
        it.addProperty("side", side)
        it.addProperty("parent", parent)
    }

    override fun getType(): String = "relative"

    object Factory : WidgetPositionFactory<RelativePosition> {
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