package net.bewis09.bewisclient.logic.color

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.game.Translation

open class StaticColorSaver: ColorSaver {
    private val color: Int

    companion object {
        val infoTranslation = Translation("color.static.info", "Static Color (Color: %s, Alpha: %s)")

        fun fromColorString(colorString: String): StaticColorSaver? {
            if (colorString.startsWith("#")) {
                return StaticColorSaver(colorString.substring(1).toIntOrNull(16) ?: 0xFFFFFFFF.toInt())
            }
            return null
        }
    }

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

    fun getColorString(): String {
        return String.format("#%08X", color)
    }

    object Factory : ColorSaverFactory<StaticColorSaver> {
        private val translation = Translation("color.static", "Static Color")
        private val description = Translation("color.static.description", "A static color that does not change.")

        override fun createFromJson(jsonElement: JsonElement): StaticColorSaver? {
            return if (jsonElement.isJsonPrimitive) {
                fromColorString(jsonElement.asString)
            } else {
                null
            }
        }

        override fun getType(): String = "static"

        override fun getTranslation(): Translation = translation

        override fun getDefault(): StaticColorSaver = StaticColorSaver(0xFFFFFFFF.toInt())

        override fun getDescription(): Translation = description

        override fun getSettingsRenderable(get: () -> StaticColorSaver, set: (ColorSaver) -> Unit) = SettingRenderable(get, set)
    }

    override fun toInfoString(): String {
        return infoTranslation(String.format("#%06X",(getColor() and 0xFFFFFF)), (color shr 24) and 0xFF).string
    }

    class SettingRenderable(get: () -> StaticColorSaver, set: (ColorSaver) -> Unit): Renderable() {
        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {

        }
    }
}