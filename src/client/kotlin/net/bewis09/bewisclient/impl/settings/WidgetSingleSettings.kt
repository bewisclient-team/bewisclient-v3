package net.bewis09.bewisclient.impl.settings

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.bewis09.bewisclient.settings.types.Setting
import net.bewis09.bewisclient.widget.WidgetLoader

object WidgetSingleSettings: Setting<JsonObject>(JsonObject()) {
    override fun convertToElement(): JsonElement? {
        val jsonObject = JsonObject()

        WidgetLoader.widgets.forEach {
            jsonObject.add(it.getId().toString(), it.convertToElement())
        }

        return jsonObject
    }

    override fun setFromElement(data: JsonElement?) {
        if (data == null || !data.isJsonObject) {
            return
        }

        val jsonObject = data.asJsonObject

        jsonObject.entrySet().forEach { entry ->
            val widgetId = entry.key
            val widgetData = entry.value

            if (widgetData.isJsonObject) {
                WidgetLoader.widgets.find { it.getId().toString() == widgetId }?.setFromElement(widgetData.asJsonObject)
            }
        }
    }
}