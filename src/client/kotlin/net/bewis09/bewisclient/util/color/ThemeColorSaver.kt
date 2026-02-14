package net.bewis09.bewisclient.util.color

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.Translations
import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.util.float
import net.bewis09.bewisclient.util.number.Precision
import net.bewis09.bewisclient.util.then

class ThemeColorSaver : ColorSaver {
    val brightness: Float?

    companion object {
        val infoTranslation = Translation("color.theme.info", "Theme Color (Brightness: %s)")
    }

    constructor(brightness: Float? = null) {
        this.brightness = brightness
    }

    override fun getColor(): Color {
        return OptionsMenuSettings.getThemeColor().withBrightness(getBrightness())
    }

    override fun getType(): String = "theme"

    override fun saveToJson(): JsonElement {
        return if (brightness == null) JsonPrimitive(-1) else JsonPrimitive(brightness)
    }

    fun getBrightness() = brightness ?: Precision(0f, 1f, 0.01f, 2).parse(OptionsMenuSettings.getThemeColor().brightness)

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

    class SettingRenderable(val get: () -> ThemeColorSaver, val set: (ColorSaver) -> Unit) : Renderable() {
        val fader = Fader({ get().getBrightness() }, Precision(0f, 1f, 0.01f, 2)) { brightness ->
            set(ThemeColorSaver(brightness))
        }
        val text = TextElement({ Translations.CHANGE_BRIGHTNESS() }, centered = true)

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            addRenderable(text(x, y + 2, width, 9))
            addRenderable(fader(x, y + 11, width - 18, 14))
            addRenderable(ResetButton<Nothing> { set(ThemeColorSaver()) }.setPosition(x2 - 14, y + 11))
        }
    }
}