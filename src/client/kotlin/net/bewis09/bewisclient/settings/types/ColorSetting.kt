package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.bewis09.bewisclient.drawable.renderables.settings.ColorSettingRenderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.color.ColorSaver

/**
 * A setting that allows the user to select a color.
 *
 * @param types the types of colors that can be selected. If not specified, all types are allowed.
 */
class ColorSetting(default: ColorSaver, vararg val types: String = ALL): Setting<ColorSaver>(default) {
    companion object {
        val ALL = ColorSaver.types.map { it.getType() }.toTypedArray()
        const val STATIC = "static"
        const val OPAQUE_STATIC = "opaque_static"
        const val CHANGING = "changing"

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

    fun createRenderable(id: String, title: String, description: String? = null): ColorSettingRenderable {
        return ColorSettingRenderable(Translation("menu.$id", title), description?.let { Translation("menu.$id.description", it) }, this, types.map { it }.toTypedArray())
    }

    override fun processChange(value: ColorSaver?): ColorSaver? {
        return if(value?.getType() in types) value else null
    }
}