package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.drawable.renderables.settings.FloatSettingRenderable
import net.bewis09.bewisclient.drawable.renderables.settings.IntegerSettingRenderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.interfaces.SettingInterface
import net.bewis09.bewisclient.util.float
import net.bewis09.bewisclient.util.number.Precision

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

    override fun convertFromElement(data: JsonElement?): Float? = processChange(data?.float())

    fun createRenderable(
        id: String, title: String, description: String? = null
    ) = FloatSettingRenderable(
        Translation("menu.$id", title), description?.let { Translation("menu.$id.description", it) }, this, precision
    )

    fun createIntRenderable(
        id: String, title: String, description: String? = null
    ): IntegerSettingRenderable = IntegerSettingRenderable(
        Translation("menu.$id", title), description?.let { Translation("menu.$id.description", it) }, object : SettingInterface<Int> {
            override fun set(value: Int?) {
                this@FloatSetting.set(value?.toFloat())
            }

            override fun get(): Int {
                return this@FloatSetting.get().toInt()
            }
        }, precision.min.toInt(), precision.max.toInt()
    )

    override fun processChange(value: Float?): Float? = value?.let {
        precision.parse(it)
    }

    fun cloneWithDefault(): FloatSetting {
        return FloatSetting({ get() }, precision)
    }
}