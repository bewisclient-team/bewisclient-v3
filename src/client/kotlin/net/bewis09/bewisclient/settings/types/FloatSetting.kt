package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.settings.Settings

class FloatSetting : Setting<Float> {
    constructor(default: Float, onChangeListener: ((oldValue: Float?, newValue: Float?) -> Unit)? = null) : super(default, onChangeListener)

    constructor(default: Float) : super(default)

    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun setFromElement(data: JsonElement?) {
        try {
            setWithoutSave(data?.asFloat)
        } catch (e: Throwable) {
            info("Failed to deserialize FloatSetting: ${Settings.gson.toJson(data)} (${e.message})")
        }
    }
}