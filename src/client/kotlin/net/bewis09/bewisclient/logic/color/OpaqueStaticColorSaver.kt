package net.bewis09.bewisclient.logic.color

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

open class OpaqueStaticColorSaver: ColorSaver {
    private val color: Int

    constructor(color: Int) {
        this.color = color and 0xFFFFFF
    }

    constructor(r: Float, g: Float, b: Float): this((r * 255).toInt() shl 16 or ((g * 255).toInt() shl 8) or (b * 255).toInt())

    constructor(r: Int, g: Int, b: Int): this((r shl 16) or (g shl 8) or b)

    override fun getColor(): Int {
        return color
    }

    override fun getType(): String = "opaque_static"

    override fun saveToJson(): JsonElement {
        return JsonPrimitive(getColorString())
    }

    companion object {
        fun fromColorString(colorString: String): StaticColorSaver? {
            if (colorString.startsWith("#")) {
                return StaticColorSaver(colorString.substring(1).toIntOrNull(16) ?: 0xFFFFFF)
            }
            return null
        }
    }

    fun getColorString(): String {
        return String.format("#%06X", color)
    }

    object Factory : ColorSaverFactory<StaticColorSaver> {
        override fun createFromJson(jsonElement: JsonElement): StaticColorSaver? {
            return if (jsonElement.isJsonPrimitive) {
                fromColorString(jsonElement.asString)
            } else {
                null
            }
        }

        override fun getType(): String = "opaque_static"
    }
}