package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.drawable.renderables.settings.BooleanSettingRenderable
import net.bewis09.bewisclient.drawable.renderables.settings.MultipleBooleanSettingsRenderable
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.logic.RenderableCreator
import net.bewis09.bewisclient.settings.structure.Feature
import net.bewis09.bewisclient.util.boolean

class BooleanSetting(default: () -> Boolean) : Setting<Boolean>(default), RenderableCreator<BooleanSettingRenderable> {
    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun convertFromElement(data: JsonElement?): Boolean? = data?.boolean()

    override fun createRenderable(feature: Feature, id: String, title: String, description: String?): BooleanSettingRenderable {
        return BooleanSettingRenderable(feature.createTranslation(id, title), description?.let { feature.createTranslation("$id.description", it) }, this)
    }

    fun createRenderablePart(feature: Feature, id: String, title: String, description: String? = null): MultipleBooleanSettingsRenderable.Part {
        return MultipleBooleanSettingsRenderable.Part(feature.createTranslation(id, title).invoke(), description?.let { Translation("menu.$id.description", it) }(), this)
    }

    fun cloneWithDefault(): BooleanSetting = BooleanSetting(::get)

    operator fun not(): Boolean = !get()
}