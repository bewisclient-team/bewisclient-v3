package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.drawable.renderables.settings.IntegerSettingRenderable
import net.bewis09.bewisclient.game.Translation

class IntegerSetting(default: () -> Int, val min: Int, val max: Int, onChangeListener: (Setting<Int>.(oldValue: Int?, newValue: Int?) -> Unit)? = null) : Setting<Int>(default, onChangeListener) {
    constructor(default: Int, min: Int, max: Int, onChangeListener: (Setting<Int>.(oldValue: Int?, newValue: Int?) -> Unit)? = null) : this({ default }, min, max, onChangeListener)

    override fun convertToElement(): JsonElement? {
        return getWithoutDefault()?.let { JsonPrimitive(it) }
    }

    override fun convertFromElement(data: JsonElement?): Int? = data?.asInt

    fun createRenderable(id: String, title: String, description: String? = null) = IntegerSettingRenderable(
        Translation("menu.$id", title), description?.let { Translation("menu.$id.description", it) }, this, min, max
    )

    fun cloneWithDefault(): IntegerSetting {
        return IntegerSetting({ get() }, min, max)
    }
}