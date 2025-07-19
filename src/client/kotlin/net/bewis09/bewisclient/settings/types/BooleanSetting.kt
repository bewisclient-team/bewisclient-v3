package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.settings.Settings

class BooleanSetting: Setting<Boolean> {
    constructor(settings: Settings, default: Boolean, onChangeListener: ((oldValue: Boolean?, newValue: Boolean?) -> Unit)? = null) : super(settings, default, onChangeListener)

    constructor(settings: Settings, default: Boolean) : super(settings, default)

    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun setFromElement(data: JsonElement?) {
        try { setWithoutSave(data?.asBoolean) } catch (e: Throwable) {
            info("Failed to deserialize BooleanSetting in setting ${this.settings.getId()}: ${Settings.gson.toJson(data)} (${e.message})")
        }
    }
}