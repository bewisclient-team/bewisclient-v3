package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.bewis09.bewisclient.drawable.Translations
import net.bewis09.bewisclient.drawable.renderables.settings.ColorFaderSettingRenderable
import net.bewis09.bewisclient.drawable.renderables.settings.ColorSettingRenderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.color.ColorSaver

/**
 * A setting that allows the user to select a color.
 *
 * @param types the types of colors that can be selected. If not specified, all types are allowed.
 */
class ColorSetting(default: () -> ColorSaver, vararg val types: String = ALL): Setting<ColorSaver>(default) {
    constructor(default: ColorSaver): this({ default }, *ALL)
    constructor(default: ColorSaver, vararg types: String): this({ default }, *types)

    companion object {
        val ALL = ColorSaver.types.map { it.getType() }.toTypedArray()
        const val STATIC = "static"
        const val CHANGING = "changing"

        fun without(vararg types: String): Array<String> {
            return ALL.filterNot { it in types }.toTypedArray()
        }
    }

    override fun convertToElement(): JsonElement? {
        if (getWithoutDefault() == null) {
            return null
        }

        val jsonObject = JsonObject()

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

    fun createRenderableWithFader(id: String, title: String, description: String? = null, faderSetting: FloatSetting): ColorFaderSettingRenderable {
        return ColorFaderSettingRenderable(Translation("menu.$id", title), description?.let { Translation("menu.$id.description", it) }, this, types.map { it }.toTypedArray(), faderSetting, Translations.OPACITY)
    }

    override fun processChange(value: ColorSaver?): ColorSaver? {
        return if(value?.getType() in types) value else null
    }

    fun cloneWithDefault(): ColorSetting {
        return ColorSetting({ get() }, *types)
    }
}