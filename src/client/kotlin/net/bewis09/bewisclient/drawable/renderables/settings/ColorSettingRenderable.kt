package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.renderables.ColorInfoButton
import net.bewis09.bewisclient.drawable.renderables.ResetButton
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.color.Color
import net.bewis09.bewisclient.logic.color.ColorSaver
import net.bewis09.bewisclient.logic.color.within
import net.bewis09.bewisclient.settings.types.Setting

class ColorSettingRenderable(val title: Translation, val description: Translation?, val setting: Setting<ColorSaver>, val types: Array<String>) : SettingRenderable(description) {
    val colorInfoButton = ColorInfoButton(
        state = setting::get, onChange = setting::set, types = types
    )

    val resetButton = ResetButton(setting)

    init {
        height = 22u
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.push()
        screenDrawing.translate(0f, getHeight() / 2f - screenDrawing.getTextHeight() / 2f + 0.5f)
        screenDrawing.drawText(title.getTranslatedString(), getX() + 8, getY(), 0.5f within (Color.WHITE to OptionsMenuSettings.themeColor.get().getColor()))
        screenDrawing.pop()
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        super.init()
        addRenderable(resetButton.setPosition(getX() + getWidth() - resetButton.getWidth() - 4, getY() + 4))
        addRenderable(colorInfoButton.setPosition(getX() + getWidth() - colorInfoButton.getWidth() - 8 - resetButton.getWidth(), getY() + 4))
    }
}