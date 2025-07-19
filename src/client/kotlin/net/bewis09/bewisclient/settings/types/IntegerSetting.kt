package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.settings.Settings

class IntegerSetting: Setting<Number> {
    constructor(settings: Settings, default: Number, onChangeListener: ((oldValue: Number?, newValue: Number?) -> Unit)? = null) : super(settings, default, onChangeListener)

    constructor(settings: Settings, default: Number) : super(settings, default)

    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun setFromElement(data: JsonElement?) {
        try { setWithoutSave(data?.asInt) } catch (e: Throwable) {
            info("Failed to deserialize IntegerSetting in setting ${this.settings.getId()}: ${Settings.gson.toJson(data)} (${e.message})")
        }
    }
}