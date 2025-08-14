package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.drawable.renderables.settings.BooleanSettingRenderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.settings.Settings

class BooleanSetting : Setting<Boolean> {
    constructor(default: () -> Boolean, onChangeListener: ((oldValue: Boolean?, newValue: Boolean?) -> Unit)? = null) : super(default, onChangeListener)

    constructor(default: () -> Boolean) : super(default)

    constructor(default: Boolean, onChangeListener: ((oldValue: Boolean?, newValue: Boolean?) -> Unit)? = null) : super({ default }, onChangeListener)

    constructor(default: Boolean) : super({ default })

    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun setFromElement(data: JsonElement?) {
        try {
            setWithoutSave(data?.asBoolean)
        } catch (e: Throwable) {
            info("Failed to deserialize BooleanSetting: ${Settings.gson.toJson(data)} (${e.message})")
        }
    }

    fun toggle() {
        set(!get())
    }

    fun createRenderable(id: String, title: String, description: String? = null): BooleanSettingRenderable {
        return BooleanSettingRenderable(Translation("menu.$id", title), description?.let { Translation("menu.$id.description", it) }, this)
    }

    fun cloneWithDefault(): BooleanSetting {
        return BooleanSetting({ get() })
    }
}