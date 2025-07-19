package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.settings.Settings

class StringSetting: Setting<String> {
    constructor(settings: Settings, default: String, onChangeListener: ((oldValue: String?, newValue: String?) -> Unit)? = null) : super(settings, default, onChangeListener)

    constructor(settings: Settings, default: String) : super(settings, default)

    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun setFromElement(data: JsonElement?) {
        try { setWithoutSave(data?.asString) } catch (e: Throwable) {
            info("Failed to deserialize StringSetting in setting ${this.settings.getId()}: ${Settings.gson.toJson(data)} (${e.message})")
        }
    }
}