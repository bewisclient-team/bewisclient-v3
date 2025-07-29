package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.bewis09.bewisclient.logic.color.ColorSaver

class ColorSetting(default: ColorSaver): Setting<ColorSaver>(default) {
    override fun convertToElement(): JsonElement? {
        val jsonObject = JsonObject()

        if (getWithoutDefault() == null) {
            return null
        }

        jsonObject.addProperty("type", get().getType())
        jsonObject.add("data", get().saveToJson())

        return jsonObject
    }

    override fun setFromElement(data: JsonElement?) {
        setWithoutSave(ColorSaver.fromJson(data))
    }
}