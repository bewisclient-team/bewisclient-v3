package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.settings.Settings

class StringSetting : Setting<String> {
    constructor(default: String, onChangeListener: ((oldValue: String?, newValue: String?) -> Unit)? = null) : super(default, onChangeListener)

    constructor(default: String) : super(default)

    constructor(default: () -> String, onChangeListener: ((oldValue: String?, newValue: String?) -> Unit)? = null) : super(default, onChangeListener)

    constructor(default: () -> String) : super(default)

    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun setFromElement(data: JsonElement?) {
        try {
            setWithoutSave(data?.asString)
        } catch (e: Throwable) {
            info("Failed to deserialize StringSetting: ${Settings.gson.toJson(data)} (${e.message})")
        }
    }
}