package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.renderables.components.button.ColorInfoButton
import net.bewis09.bewisclient.drawable.renderables.components.button.ResetButton
import net.bewis09.bewisclient.drawable.renderables.components.setting.Fader
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.settings.types.FloatSetting
import net.bewis09.bewisclient.settings.types.Setting
import net.bewis09.bewisclient.util.color.ColorSaver

class ColorFaderSettingRenderable(val title: Translation, val description: Translation?, val setting: Setting<ColorSaver>, val types: Array<String>, val setting2: FloatSetting, val title2: Translation) : SettingRenderable(description, 35) {
    val colorInfoButton = ColorInfoButton(
        state = setting::get, onChange = setting::set, types = types
    )

    val fader = Fader(
        value = { setting2.get() }, onChange = { value ->
            setting2.set(value)
        }, precision = setting2.precision
    )

    val resetButton = ResetButton<Nothing> ({
        setting.set(null)
        setting2.set(null)
    }) {
        setting.isDefault() && setting2.isDefault()
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        drawVerticalCenteredText(screenDrawing, title)
        screenDrawing.drawRightAlignedText(title2.getTranslatedString() + ": " + setting2.get(), x2 - fader.width - 12 - resetButton.width, y + 22.5f, General.getTextThemeColor())
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        super.init()
        addRenderable(resetButton.setPosition(x2 - resetButton.width - 4, y + 4))
        addRenderable(colorInfoButton.setPosition(x2 - colorInfoButton.width - 8 - resetButton.width, y + 4))
        addRenderable(fader.setPosition(x2 - fader.width - 8 - resetButton.width, y + 20 - if (General.isMinecrafty) 1 else 0))
    }
}