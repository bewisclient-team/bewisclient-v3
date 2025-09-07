package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.bewis09.bewisclient.logic.color.ColorSaver
import net.bewis09.bewisclient.logic.number.Precision
import net.bewis09.bewisclient.settings.Settings

open class ObjectSetting() : Setting<JsonObject>(JsonObject()) {
    val map: HashMap<String, Setting<*>> = hashMapOf()

    override fun convertToElement(): JsonElement {
        val jsonObject = JsonObject()
        map.forEach { (key, setting) ->
            setting.convertToElement()?.let { jsonObject.add(key, it) }
        }
        return jsonObject
    }

    fun get(key: String): Setting<*>? {
        return map[key]
    }

    override fun onChange(oldValue: JsonObject?, newValue: JsonObject?) {
        map.forEach {
            val setting = it.value
            if (newValue != null && newValue.has(it.key)) {
                try {
                    setting.setFromElement(newValue.get(it.key))
                } catch (e: Exception) {
                    error("Failed to set value for setting ${it.key}: ${Settings.gson.toJson(newValue.get(it.key))} (${e.message})")
                }
            } else {
                setting.setFromElement(null)
            }
        }
    }

    /**
     * Creates a new setting with the given key and adds it to the map.
     * This should only be used statically and no dynamic settings should be created.
     *
     * @param key The key of the setting.
     * @param setting The setting to add.
     */
    fun <T : Setting<*>> create(key: String, setting: T): T {
        map[key] = setting
        get().get(key)?.let {
            setting.setFromElement(it)
        }

        return setting
    }

    override fun setFromElement(data: JsonElement?) {
        try {
            setWithoutSave(data?.asJsonObject)
        } catch (e: Exception) {
            error("Failed to deserialize ObjectSetting: ${Settings.gson.toJson(data)} (${e.message})")
        }
    }

    fun boolean(key: String, default: Boolean, onChangeListener: (Setting<Boolean>.(oldValue: Boolean?, newValue: Boolean?) -> Unit)? = null): BooleanSetting {
        return create(key, BooleanSetting(default, onChangeListener))
    }

    fun float(key: String, default: Float, min: Float, max: Float, step: Float, precision: Int): FloatSetting {
        return create(key, FloatSetting(default, Precision(min, max, step, precision)))
    }

    fun int(key: String, default: Int, min: Int, max: Int): IntegerSetting {
        return create(key, IntegerSetting(default, min, max))
    }

    fun color(key: String, default: ColorSaver, vararg types: String): ColorSetting {
        return create(key, ColorSetting(default, *types))
    }
}