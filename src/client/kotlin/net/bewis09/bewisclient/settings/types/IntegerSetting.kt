package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.drawable.renderables.settings.IntegerSettingRenderable
import net.bewis09.bewisclient.game.Translation
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

    fun createRenderable(id: String, title: String, description: String, min: Int, max: Int) =
        IntegerSettingRenderable(
            Translation("menu.$id", title),
            Translation("menu.$id.description", description),
            this,
            min,
            max
        )
}