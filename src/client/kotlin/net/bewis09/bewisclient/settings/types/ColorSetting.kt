package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.bewis09.bewisclient.logic.color.ColorSaver

/**
 * A setting that allows the user to select a color.
 *
 * @param types the types of colors that can be selected. If not specified, all types are allowed.
 */
class ColorSetting(default: ColorSaver, vararg val types: String = ALL): Setting<ColorSaver>(default) {
    companion object {
        val ALL = ColorSaver.types.map { it.getType() }.toTypedArray()
        val STATIC = "static"
        val OPAQUE_STATIC = "opaque_static"
        val CHANGING = "changing"

        fun without(vararg types: String): Array<String> {
            return ALL.filterNot { it in types }.toTypedArray()
        }
    }

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