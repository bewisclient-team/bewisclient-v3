package net.bewis09.bewisclient.widget.logic

import com.google.gson.JsonElement
import net.bewis09.bewisclient.widget.Widget

interface WidgetPosition {
    fun getX(widget: Widget): Int
    fun getY(widget: Widget): Int

    fun saveToJson(): JsonElement

    fun getType(): String

    companion object {
        val types = listOf<WidgetPositionFactory<*>>(
            SidedPosition.Factory,
            RelativePosition.Factory
        )
    }
}

interface WidgetPositionFactory<T: WidgetPosition> {
    fun createFromJson(jsonElement: JsonElement): T?
    fun getType(): String
}