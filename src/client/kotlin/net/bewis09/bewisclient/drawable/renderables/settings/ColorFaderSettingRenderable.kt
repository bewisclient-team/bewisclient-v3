package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.util.color.*
import net.bewis09.bewisclient.settings.types.FloatSetting
import net.bewis09.bewisclient.settings.types.Setting

class ColorFaderSettingRenderable(val title: Translation, val description: Translation?, val setting: Setting<ColorSaver>, val types: Array<String>, val setting2: FloatSetting, val title2: Translation) : SettingRenderable(description, 35) {
    val colorInfoButton = ColorInfoButton(
        state = setting::get, onChange = setting::set, types = types
    )

    val fader = Fader(
        value = { setting2.get() }, onChange = { value ->
            setting2.set(value)
        }, precision = setting2.precision
    )

    val resetButton = ResetButton<Nothing> {
        setting.set(null)
        setting2.set(null)
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        drawVerticalCenteredText(screenDrawing, title)
        screenDrawing.translate(0f, y + 22.5f) {
            screenDrawing.drawRightAlignedText(title2.getTranslatedString() + ": " + setting2.get(), x2 - fader.width - 12 - resetButton.width, 0, 0.5f within (Color.WHITE to OptionsMenuSettings.themeColor.get().getColor()))
        }
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        super.init()
        addRenderable(resetButton.setPosition(x2 - resetButton.width - 4, y + 4))
        addRenderable(colorInfoButton.setPosition(x2 - colorInfoButton.width - 8 - resetButton.width, y + 4))
        addRenderable(fader.setPosition(x2 - fader.width - 8 - resetButton.width, y + 20))
    }
}