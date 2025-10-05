package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.drawable.renderables.settings.BooleanSettingRenderable
import net.bewis09.bewisclient.drawable.renderables.settings.MultipleBooleanSettingsRenderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.util.boolean

class BooleanSetting : Setting<Boolean> {
    constructor(default: () -> Boolean, onChangeListener: (Setting<Boolean>.(oldValue: Boolean?, newValue: Boolean?) -> Unit)? = null) : super(default, onChangeListener)

    constructor(default: () -> Boolean) : super(default)

    constructor(default: Boolean, onChangeListener: (Setting<Boolean>.(oldValue: Boolean?, newValue: Boolean?) -> Unit)? = null) : super({ default }, onChangeListener)

    constructor(default: Boolean) : super({ default })

    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun convertFromElement(data: JsonElement?): Boolean? = data?.boolean()

    fun toggle() {
        set(!get())
    }

    fun createRenderable(id: String, title: String, description: String? = null): BooleanSettingRenderable {
        return BooleanSettingRenderable(Translation("menu.$id", title), description?.let { Translation("menu.$id.description", it) }, this)
    }

    fun createRenderablePart(id: String, title: String, description: String? = null): MultipleBooleanSettingsRenderable.Part {
        return MultipleBooleanSettingsRenderable.Part(Translation("menu.$id", title), description?.let { Translation("menu.$id.description", it) }, this)
    }

    fun cloneWithDefault(): BooleanSetting {
        return BooleanSetting { get() }
    }
}