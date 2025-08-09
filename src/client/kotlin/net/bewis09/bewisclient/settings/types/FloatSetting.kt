package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.drawable.renderables.settings.FloatSettingRenderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.number.Precision
import net.bewis09.bewisclient.settings.Settings

class FloatSetting : Setting<Float> {
    val precision: Precision

    constructor(default: Float, precision: Precision, onChangeListener: ((oldValue: Float?, newValue: Float?) -> Unit)? = null) : super(default, onChangeListener) {
        this.precision = precision
    }

    constructor(default: Float, precision: Precision) : super(default) {
        this.precision = precision
    }

    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun setFromElement(data: JsonElement?) {
        try {
            setWithoutSave(processChange(data?.asFloat))
        } catch (e: Throwable) {
            info("Failed to deserialize FloatSetting: ${Settings.gson.toJson(data)} (${e.message})")
        }
    }

    fun createRenderable(
        id: String,
        title: String,
        description: String? = null
    ) = FloatSettingRenderable(
        Translation("menu.$id", title),
        description?.let { Translation("menu.$id.description", it) },
        this,
        precision
    )

    override fun processChange(value: Float?): Float? = value?.let {
        precision.parse(it)
    }
}