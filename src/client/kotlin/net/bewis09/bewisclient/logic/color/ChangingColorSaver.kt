package net.bewis09.bewisclient.logic.color

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import java.awt.Color

class ChangingColorSaver: ColorSaver {
    val changingSpeed: Int

    constructor(changingSpeed: Int) {
        this.changingSpeed = changingSpeed
    }

    override fun getColor(): Int {
        val time = (System.currentTimeMillis() % changingSpeed) / changingSpeed.toFloat()
        return Color.HSBtoRGB(time, 1f, 1f)
    }

    override fun getType(): String = "changing"

    override fun saveToJson(): JsonElement {
        return JsonPrimitive(changingSpeed.toString())
    }

    object Factory : ColorSaverFactory<ChangingColorSaver> {
        override fun createFromJson(jsonElement: JsonElement): ChangingColorSaver? {
            return if (jsonElement.isJsonPrimitive && jsonElement.asJsonPrimitive.isNumber) {
                ChangingColorSaver(jsonElement.asInt)
            } else {
                null
            }
        }

        override fun getType(): String = "changing"
    }
}