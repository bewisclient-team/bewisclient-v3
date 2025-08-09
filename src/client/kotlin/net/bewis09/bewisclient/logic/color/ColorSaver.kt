package net.bewis09.bewisclient.logic.color

import com.google.gson.JsonElement
import com.google.gson.JsonObject

interface ColorSaver {
    fun getColor(): Int
    fun getType(): String
    fun saveToJson(): JsonElement

    companion object {
        val types = listOf<ColorSaverFactory<*>>(
            StaticColorSaver.Factory,
            ChangingColorSaver.Factory,
            OpaqueStaticColorSaver.Factory
        )

        fun fromJson(jsonElement: JsonElement?): ColorSaver? {
            if (jsonElement?.isJsonObject == true) {
                val jsonObject = jsonElement.asJsonObject
                val type = jsonObject.get("type")?.asString ?: return null
                val factory = types.firstOrNull { it.getType() == type } ?: return null
                val data = jsonObject.get("data") ?: return null

                return factory.createFromJson(data)
            }
            return null
        }
    }

    fun convertToElement(): JsonElement? {
        val jsonObject = JsonObject()

        jsonObject.addProperty("type", getType())
        jsonObject.add("data", saveToJson())

        return jsonObject
    }
}

interface ColorSaverFactory<T: ColorSaver> {
    fun createFromJson(jsonElement: JsonElement): T?
    fun getType(): String
}