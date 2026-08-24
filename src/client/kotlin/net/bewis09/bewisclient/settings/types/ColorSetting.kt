package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.bewis09.bewisclient.drawable.renderables.settings.ColorFaderSettingRenderable
import net.bewis09.bewisclient.drawable.renderables.settings.ColorSettingRenderable
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.logic.RenderableCreator
import net.bewis09.bewisclient.settings.structure.Feature
import net.bewis09.bewisclient.util.color.ColorSaver

class ColorSetting(default: () -> ColorSaver, vararg types: String) : Setting<ColorSaver>(default), RenderableCreator<ColorSettingRenderable> {
    val types: Array<out String> = types.ifEmpty { ALL }

    companion object {
        val ALL = ColorSaver.types.map { it.getType() }.toTypedArray()
        const val STATIC = "static"
        const val CHANGING = "changing"
        const val THEME = "theme"

        val opacityTranslation = Translation("menu.color.opacity", "Opacity")
    }

    override fun convertToElement(): JsonElement? {
        if (getWithoutDefault() == null) return null

        return JsonObject().apply {
            addProperty("type", get().getType())
            add("data", get().saveToJson())
        }
    }

    override fun convertFromElement(data: JsonElement?): ColorSaver? = ColorSaver.fromJson(data)

    override fun createRenderable(feature: Feature, id: String, title: String, description: String?): ColorSettingRenderable {
        return ColorSettingRenderable {
            this.title = feature.createTranslation(id, title)
            tooltip = description?.let { feature.createTranslation("$id.description", it)() }
            setting = this@ColorSetting
            types = this@ColorSetting.types.map { it }.toTypedArray()
        }
    }

    fun createRenderableWithFader(feature: Feature, id: String, title: String, description: String? = null, faderSetting: FloatSetting): ColorFaderSettingRenderable {
        return ColorFaderSettingRenderable {
            this.title = feature.createTranslation(id, title)
            tooltip = description?.let { feature.createTranslation("$id.description", it)() }
            setting = this@ColorSetting
            types = this@ColorSetting.types.map { it }.toTypedArray()
            setting2 = faderSetting
            title2 = opacityTranslation
        }
    }

    override fun processChange(value: ColorSaver?): ColorSaver? {
        return if (value?.getType() in types) value else null
    }

    fun cloneWithDefault(): ColorSetting = ColorSetting(::get, *types)
}