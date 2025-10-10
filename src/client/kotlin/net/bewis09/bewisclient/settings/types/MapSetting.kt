package net.bewis09.bewisclient.settings.types

import com.google.gson.*
import net.bewis09.bewisclient.util.*

open class MapSetting<T>(val from: (JsonElement) -> T?, val to: (T) -> JsonElement) : Setting<HashMap<String, T>>(hashMapOf()) {
    override fun convertToElement(): JsonElement {
        return get().let { map ->
            JsonObject().also {
                map.forEach { (key, value) ->
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

    override fun convertFromElement(data: JsonElement?): HashMap<String, T>? = data?.jsonObject()?.asMap()?.mapValues { from(it.value) }?.filter { it.value != null }?.map { it.key to it.value!! }?.toTypedArray()?.let { hashMapOf(*it) }
}

open class BooleanMapSetting : MapSetting<Boolean>(from = { it.boolean() }, to = { JsonPrimitive(it) })

open class IntegerMapSetting : MapSetting<Int>(from = { it.int() }, to = { JsonPrimitive(it) })

open class StringMapSetting : MapSetting<String>(from = { it.string() }, to = { JsonPrimitive(it) })

open class FloatMapSetting : MapSetting<Float>(from = { it.float() }, to = { JsonPrimitive(it) })