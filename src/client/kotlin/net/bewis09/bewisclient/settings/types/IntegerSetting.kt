package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.drawable.renderables.settings.IntegerSettingRenderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.settings.Settings

class IntegerSetting(default: () -> Int, val min: Int, val max: Int, onChangeListener: ((oldValue: Int?, newValue: Int?) -> Unit)? = null) : Setting<Int>(default, onChangeListener) {
    constructor(default: Int, min: Int, max: Int, onChangeListener: ((oldValue: Int?, newValue: Int?) -> Unit)? = null) : this({ default }, min, max, onChangeListener)

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

    fun createRenderable(id: String, title: String, description: String? = null) =
        IntegerSettingRenderable(
            Translation("menu.$id", title),
            description?.let { Translation("menu.$id.description", it) },
            this,
            min,
            max
        )

    fun cloneWithDefault(): IntegerSetting {
        return IntegerSetting({ get() }, min, max)
    }
}