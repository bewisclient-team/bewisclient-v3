package net.bewis09.bewisclient.settings.types

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.logic.catch
import net.bewis09.bewisclient.settings.Settings

open class MapSetting<T>(val from: (JsonElement) -> T, val to: (T) -> JsonElement) : Setting<HashMap<String, T>>(hashMapOf()) {
    override fun convertToElement(): JsonElement {
        return get().let { map ->
            JsonObject().also {
                map.forEach { key, value ->
                    it.add(key, to(value))
                }
            }
        }
    }

    operator fun get(key: String): T? {
        return get()[key]
    }

    operator fun get(key: String, default: T): T {
        return this[key] ?: default
    }

    operator fun set(key: String, value: T?) {
        val currentMap = get()
        if (value == null) {
            currentMap.remove(key)
        } else {
            currentMap[key] = value
        }
        save()
    }

    override fun setFromElement(data: JsonElement?) {
        try {
            setWithoutSave(data?.asJsonObject?.asMap()?.mapValues { catch { from(it.value) } }?.filter { it.value != null }?.map { it.key to it.value!! }?.toTypedArray()?.let { hashMapOf(*it) })
        } catch (e: Exception) {
            error("Failed to deserialize ObjectSetting: ${Settings.gson.toJson(data)} (${e.message})")
        }
    }
}

class BooleanMapSetting : MapSetting<Boolean>(
    from = { it.asBoolean },
    to = { JsonPrimitive(it) }
)

class IntegerMapSetting : MapSetting<Int>(
    from = { it.asInt },
    to = { JsonPrimitive(it) }
)

class StringMapSetting : MapSetting<String>(
    from = { it.asString },
    to = { JsonPrimitive(it) }
)

class FloatMapSetting : MapSetting<Float>(
    from = { it.asFloat },
    to = { JsonPrimitive(it) }
)