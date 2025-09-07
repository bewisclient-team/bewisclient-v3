package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.renderables.ColorInfoButton
import net.bewis09.bewisclient.drawable.renderables.Fader
import net.bewis09.bewisclient.drawable.renderables.ResetButton
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.color.ColorSaver
import net.bewis09.bewisclient.logic.within
import net.bewis09.bewisclient.settings.types.FloatSetting
import net.bewis09.bewisclient.settings.types.Setting

class ColorFaderSettingRenderable(val title: Translation, val description: Translation?, val setting: Setting<ColorSaver>, val types: Array<String>, val setting2: FloatSetting, val title2: Translation) : SettingRenderable(description) {
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

    init {
        height = 35u
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.push()
        screenDrawing.translate(0f, getHeight() / 2f - screenDrawing.getTextHeight() / 2f + 0.5f)
        screenDrawing.drawText(title.getTranslatedString(), getX() + 8, getY(), 0.5f within (0xFFFFFF to OptionsMenuSettings.themeColor.get().getColor()), 1.0F)
        screenDrawing.pop()
        screenDrawing.push()
        screenDrawing.translate(0f, getY() + 22.5f)
        screenDrawing.drawRightAlignedText(title2.getTranslatedString(), getX() + getWidth() - fader.getWidth() - 12 - resetButton.getWidth(), 0, 0.5f within (0xFFFFFF to OptionsMenuSettings.themeColor.get().getColor()), 1.0F)
        screenDrawing.pop()
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        super.init()
        addRenderable(resetButton.setPosition(getX() + getWidth() - resetButton.getWidth() - 4, getY() + 4))
        addRenderable(colorInfoButton.setPosition(getX() + getWidth() - colorInfoButton.getWidth() - 8 - resetButton.getWidth(), getY() + 4))
        addRenderable(fader.setPosition(getX() + getWidth() - fader.getWidth() - 8 - resetButton.getWidth(), getY() + 20))
    }
}