package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.renderables.Fader
import net.bewis09.bewisclient.drawable.renderables.ResetButton
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.color.Color
import net.bewis09.bewisclient.logic.color.within
import net.bewis09.bewisclient.logic.number.Precision
import net.bewis09.bewisclient.settings.types.Setting

open class FaderSettingRenderable<T : Number>(val title: Translation, val description: Translation?, val setting: Setting<T>, val precision: Precision, val parser: (original: Float) -> T) : SettingRenderable(description) {
    val fader = Fader(
        value = { setting.get().toFloat() }, onChange = { value ->
            setting.set(parser(value))
        }, precision = precision
    )

    val resetButton = ResetButton(setting)

    init {
        height = 22u
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        drawVerticalCenteredText(screenDrawing, title)
        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.translate(0f, getHeight() / 2f - screenDrawing.getTextHeight() / 2f) {
            screenDrawing.drawRightAlignedText(precision.roundToString(setting.get().toFloat()), getX() + getWidth() - fader.getWidth() - 12 - resetButton.getWidth(), getY(), 0.5f within (Color.WHITE to OptionsMenuSettings.themeColor.get().getColor()))
        }
    }

    override fun init() {
        super.init()
        addRenderable(resetButton.setPosition(getX() + getWidth() - resetButton.getWidth() - 4, getY() + 4))
        addRenderable(fader.setPosition(getX() + getWidth() - fader.getWidth() - 8 - resetButton.getWidth(), getY() + 4))
    }
}