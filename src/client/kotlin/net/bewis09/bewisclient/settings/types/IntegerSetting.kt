package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.settings.Settings

class IntegerSetting : Setting<Int> {
    constructor(default: Int, onChangeListener: ((oldValue: Int?, newValue: Int?) -> Unit)? = null) : super(default, onChangeListener)

    constructor(default: Int) : super(default)

    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun setFromElement(data: JsonElement?) {
        try {
            setWithoutSave(data?.asInt)
        } catch (e: Throwable) {
            info("Failed to deserialize IntegerSetting: ${Settings.gson.toJson(data)} (${e.message})")
        }
    }
}