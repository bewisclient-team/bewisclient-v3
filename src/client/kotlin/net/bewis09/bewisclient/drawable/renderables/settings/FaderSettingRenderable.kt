package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.renderables.Fader
import net.bewis09.bewisclient.drawable.renderables.ResetButton
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.interfaces.SettingInterface
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.color.within
import net.bewis09.bewisclient.util.number.Precision

open class FaderSettingRenderable<T : Number>(val title: Translation, val description: Translation?, val setting: SettingInterface<T>, val precision: Precision, val parser: (original: Float) -> T) : SettingRenderable(description, 22) {
    val fader = Fader(
        value = { setting.get().toFloat() }, onChange = { value ->
            setting.set(parser(value))
        }, precision = precision
    )

    val resetButton = ResetButton(setting)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        drawVerticalCenteredText(screenDrawing, title)
        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.translate(0f, height / 2f - screenDrawing.getTextHeight() / 2f) {
            screenDrawing.drawRightAlignedText(precision.roundToString(setting.get().toFloat()), x2 - fader.width - 12 - resetButton.width, y, OptionsMenuSettings.getTextThemeColor())
        }
    }

    override fun init() {
        super.init()
        addRenderable(resetButton.setPosition(x2 - resetButton.width - 4, y + 4))
        addRenderable(fader.setPosition(x2 - fader.width - 8 - resetButton.width, y + 4))
    }
}