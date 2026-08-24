package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.drawable.renderables.settings.BooleanSettingRenderable
import net.bewis09.bewisclient.drawable.renderables.settings.MultipleBooleanSettingsRenderable
import net.bewis09.bewisclient.settings.logic.RenderableCreator
import net.bewis09.bewisclient.settings.structure.Feature
import net.bewis09.bewisclient.util.boolean

class BooleanSetting(default: () -> Boolean) : Setting<Boolean>(default), RenderableCreator<BooleanSettingRenderable> {
    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun convertFromElement(data: JsonElement?): Boolean? = data?.boolean()

    override fun createRenderable(feature: Feature, id: String, title: String, description: String?): BooleanSettingRenderable {
        return BooleanSettingRenderable {
            this.title = feature.createTranslation(id, title)
            tooltip = description?.let { feature.createTranslation("$id.description", it)() }
            setting = this@BooleanSetting
        }
}

    fun createRenderablePart(feature: Feature, id: String, title: String, description: String? = null): MultipleBooleanSettingsRenderable.Part {
        return MultipleBooleanSettingsRenderable.Part {
            this.title = feature.createTranslation(id, title).invoke()
            tooltip = description?.let { feature.createTranslation("$id.description", it)() }
            setting = this@BooleanSetting
        }
    }

    fun cloneWithDefault(): BooleanSetting = BooleanSetting(::get)

    operator fun not(): Boolean = !get()
}