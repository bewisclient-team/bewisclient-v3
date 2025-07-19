package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.settings.Settings

class FloatSetting: Setting<Float> {
    constructor(settings: Settings, default: Float, onChangeListener: ((oldValue: Float?, newValue: Float?) -> Unit)? = null) : super(settings, default, onChangeListener)

    constructor(settings: Settings, default: Float) : super(settings, default)

    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun setFromElement(data: JsonElement?) {
        try { setWithoutSave(data?.asFloat) } catch (e: Throwable) {
            info("Failed to deserialize FloatSetting in setting ${this.settings.getId()}: ${Settings.gson.toJson(data)} (${e.message})")
        }
    }
}