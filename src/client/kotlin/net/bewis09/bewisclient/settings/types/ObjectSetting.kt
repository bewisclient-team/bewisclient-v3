package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.bewis09.bewisclient.settings.Settings

open class ObjectSetting: Setting<JsonObject> {
    /**
     * A map of all settings in this object setting.
     * Changing this map will not result in a change of the json object, and therefore it will not be saved.
     */
    val map: HashMap<String, Setting<*>> = hashMapOf()

    constructor(
        settings: Settings
    ) : super(settings, JsonObject())

    override fun convertToElement(): JsonElement {
        val jsonObject = JsonObject()
        map.forEach { (key, setting) ->
            setting.convertToElement()?.let { jsonObject.add(key, it) }
        }
        setWithoutSave(jsonObject)
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
                    error("Failed to set value for setting ${setting.settings.getId()}.${it.key}: ${Settings.gson.toJson(newValue.get(it.key))} (${e.message})")
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
        map.put(key, setting)
        get().get(key)?.let {
            setting.setFromElement(it)
        }

        return setting
    }

    override fun setFromElement(data: JsonElement?) {
        try { setWithoutSave(data?.asJsonObject) } catch (e: Exception) {
            error("Failed to deserialize ObjectSetting in setting ${this.settings.getId()}: ${Settings.gson.toJson(data)} (${e.message})")
        }
    }
}