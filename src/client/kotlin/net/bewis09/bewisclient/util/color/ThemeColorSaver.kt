package net.bewis09.bewisclient.util.color

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.renderite.logic.Color
import net.bewis09.bewisclient.common.then
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.SimpleRenderable
import net.bewis09.bewisclient.drawable.renderables.components.button.ResetButton
import net.bewis09.bewisclient.drawable.renderables.components.element.Text
import net.bewis09.bewisclient.drawable.renderables.components.logic.TextAlign
import net.bewis09.bewisclient.drawable.renderables.components.setting.Fader
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.util.float
import net.bewis09.bewisclient.util.number.Precision

class ThemeColorSaver : ColorSaver {
    val brightness: Float?

    companion object {
        val infoTranslation = Translation("color.theme.info", "Theme Color (Brightness: %s)")
    }

    constructor(brightness: Float? = null) {
        this.brightness = brightness
    }

    override fun getColor(): Color {
        return General.themeColor.get().getColor().withBrightness(getBrightness())
    }

    override fun getType(): String = "theme"

    override fun saveToJson(): JsonElement {
        return if (brightness == null) JsonPrimitive(-1) else JsonPrimitive(brightness)
    }

    fun getDefault() = Precision(0f, 1f, 0.01f, 2).parse(General.themeColor.get().getColor().brightness)

    fun getBrightness() = brightness ?: getDefault()

    object Factory : ColorSaverFactory<ThemeColorSaver> {
        private val translation = Translation("color.theme", "Theme Color")
        private val description = Translation("color.theme.description", "A color that uses the client's theme color with the brightness set.")

        override fun createFromJson(jsonElement: JsonElement): ThemeColorSaver? {
            return jsonElement.float()?.let { ThemeColorSaver((it != -1f) then { it }) }
        }

        override fun getType(): String = "theme"

        override fun getTranslation(): Translation = translation

        override fun getDefault(): ThemeColorSaver = ThemeColorSaver()

        override fun getDescription(): Translation = description

        override fun getSettingsRenderable(get: () -> ThemeColorSaver, set: (ColorSaver) -> Unit): Renderable = SettingRenderable(get, set)
    }

    override fun toInfoString(): String = infoTranslation(getBrightness()).string

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ThemeColorSaver) return false
        return brightness == other.brightness
    }

    override fun hashCode(): Int {
        return brightness.hashCode()
    }

    class SettingRenderable(val get: () -> ThemeColorSaver, val set: (ColorSaver) -> Unit) : SimpleRenderable() {
        val fader = Fader {
            value = { get().getBrightness() }
            precision = Precision(0f, 1f, 0.01f, 2)
            onChange = { brightness ->
                set(ThemeColorSaver(brightness))
            }
        }
        val text = Text { textProvider = { StaticColorSaver.changeBrightnessText() }; textAlign = TextAlign.CENTER }

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            addRenderable(text(x, y + 2, width, 9))
            addRenderable(fader(x, y + 11, width - 18, 14))
            addRenderable(ResetButton<Nothing> {
                settable = ({ set(ThemeColorSaver()) })
                isDefault = { get().brightness == get().getDefault() }
            }.updatePosition(x2 - 14, y + 11))
        }
    }
}