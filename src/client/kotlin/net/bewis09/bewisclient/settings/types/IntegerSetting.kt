package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.drawable.renderables.settings.IntegerSettingRenderable
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.logic.RenderableCreator
import net.bewis09.bewisclient.settings.structure.Feature
import net.bewis09.bewisclient.util.int

class IntegerSetting(default: () -> Int, val min: Int, val max: Int) : Setting<Int>(default), RenderableCreator<IntegerSettingRenderable> {
    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun convertFromElement(data: JsonElement?): Int? = data?.int()

    override fun createRenderable(feature: Feature, id: String, title: String, description: String?) = IntegerSettingRenderable {
        this.title = feature.createTranslation(id, title)
        tooltip = description?.let { feature.createTranslation("$id.description", it)() }
        setting = this@IntegerSetting
        this.min = this@IntegerSetting.min
        this.max = this@IntegerSetting.max
    }

    fun cloneWithDefault(): IntegerSetting = IntegerSetting(::get, min, max)
}