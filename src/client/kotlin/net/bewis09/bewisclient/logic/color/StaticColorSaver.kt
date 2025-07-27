package net.bewis09.bewisclient.logic.color

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

class StaticColorSaver: ColorSaver {
    private val color: Int

    constructor(color: Int) {
        this.color = color
    }

    constructor(r: Float, g: Float, b: Float, a: Float) {
        this.color = ((a * 255).toInt() shl 24) or ((r * 255).toInt() shl 16) or ((g * 255).toInt() shl 8) or (b * 255).toInt()
    }

    constructor(r: Int, g: Int, b: Int, a: Int) {
        this.color = (a shl 24) or (r shl 16) or (g shl 8) or b
    }

    override fun getColor(): Int {
        return color
    }

    override fun getType(): String = "static"

    override fun saveToJson(): JsonElement {
        return JsonPrimitive(getColorString())
    }

    companion object {
        fun fromColorString(colorString: String): StaticColorSaver? {
            if (colorString.startsWith("#")) {
                return StaticColorSaver(colorString.substring(1))
            }
            return null
        }

        fun StaticColorSaver(color: String): StaticColorSaver? {
            return StaticColorSaver(color.toIntOrNull(16) ?: 0xFFFFFFFF.toInt())
        }
    }

    fun getColorString(): String {
        return String.format("#%08X", color)
    }

    object Factory : ColorSaverFactory<StaticColorSaver> {
        override fun createFromJson(jsonElement: JsonElement): StaticColorSaver? {
            return if (jsonElement.isJsonPrimitive) {
                fromColorString(jsonElement.asString)
            } else {
                null
            }
        }

        override fun getType(): String = "static"
    }
}