package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.drawable.renderables.settings.FloatSettingRenderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.number.Precision

class FloatSetting : Setting<Float> {
    val precision: Precision

    constructor(default: () -> Float, precision: Precision, onChangeListener: (Setting<Float>.(oldValue: Float?, newValue: Float?) -> Unit)? = null) : super(default, onChangeListener) {
        this.precision = precision
    }

    constructor(default: () -> Float, precision: Precision) : super(default) {
        this.precision = precision
    }

    constructor(default: Float, precision: Precision, onChangeListener: (Setting<Float>.(oldValue: Float?, newValue: Float?) -> Unit)? = null) : super({ default }, onChangeListener) {
        this.precision = precision
    }

    constructor(default: Float, precision: Precision) : super({ default }) {
        this.precision = precision
    }

    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun convertFromElement(data: JsonElement?): Float? = processChange(data?.asFloat)

    fun createRenderable(
        id: String, title: String, description: String? = null
    ) = FloatSettingRenderable(
        Translation("menu.$id", title), description?.let { Translation("menu.$id.description", it) }, this, precision
    )

    override fun processChange(value: Float?): Float? = value?.let {
        precision.parse(it)
    }

    fun cloneWithDefault(): FloatSetting {
        return FloatSetting({ get() }, precision)
    }
}