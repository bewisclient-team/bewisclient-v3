package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.bewis09.bewisclient.util.jsonObject
import net.bewis09.bewisclient.widget.logic.WidgetPosition

class WidgetPositionSetting(defaultPos: WidgetPosition) : Setting<WidgetPosition>(defaultPos) {
    override fun convertToElement(): JsonElement? {
        if (getWithoutDefault() == null) return null

        val jsonObject = JsonObject()

        jsonObject.addProperty("positionType", getWithoutDefault()!!.getType())
        jsonObject.add("positionData", getWithoutDefault()!!.saveToJson())

        return jsonObject
    }

    override fun convertFromElement(data: JsonElement?): WidgetPosition? {
        val json = (data ?: return null).jsonObject() ?: return null

        if (!data.asJsonObject.has("positionType")) return null
        var value: WidgetPosition? = null

        WidgetPosition.types.forEach {
            if (json.get("positionType")?.asString == it.getType()) {
                it.createFromJson(json.get("positionData"))?.let { a ->
                    value = a
                }
            }
        }

        return value
    }
}