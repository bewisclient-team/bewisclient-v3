package net.bewis09.bewisclient.util

import com.google.gson.*

fun JsonElement.number(): Number? {
    return (this.isJsonPrimitive && this.asJsonPrimitive.isNumber) then this.asNumber
}

fun JsonElement.int(): Int? {
    return (this.isJsonPrimitive && this.asJsonPrimitive.isNumber) then this.asInt
}

fun JsonElement.float(): Float? {
    return (this.isJsonPrimitive && this.asJsonPrimitive.isNumber) then this.asFloat
}

fun JsonElement.double(): Double? {
    return (this.isJsonPrimitive && this.asJsonPrimitive.isNumber) then this.asDouble
}

fun JsonElement.long(): Long? {
    return (this.isJsonPrimitive && this.asJsonPrimitive.isNumber) then this.asLong
}

fun JsonElement.short(): Short? {
    return (this.isJsonPrimitive && this.asJsonPrimitive.isNumber) then this.asShort
}

fun JsonElement.byte(): Byte? {
    return (this.isJsonPrimitive && this.asJsonPrimitive.isNumber) then this.asByte
}

fun JsonElement.string(): String? {
    return (this.isJsonPrimitive) then this.asString
}

fun JsonElement.boolean(): Boolean? {
    return (this.isJsonPrimitive && this.asJsonPrimitive.isBoolean) then this.asBoolean
}

fun JsonElement.jsonObject(): JsonObject? {
    return (this.isJsonObject) then this.asJsonObject
}

fun JsonElement.jsonArray(): JsonArray? {
    return (this.isJsonArray) then this.asJsonArray
}