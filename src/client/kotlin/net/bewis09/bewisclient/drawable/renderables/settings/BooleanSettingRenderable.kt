package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.renderables.ResetButton
import net.bewis09.bewisclient.drawable.renderables.Switch
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.within
import net.bewis09.bewisclient.settings.types.Setting

class BooleanSettingRenderable(val title: Translation, val description: Translation?, val setting: Setting<Boolean>) : SettingRenderable(description) {
    val switch = Switch(
        state = setting::get,
        onChange = setting::set,
    )

    val resetButton = ResetButton(setting)

    init {
        height = 22u
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.push()
        screenDrawing.translate(0f, getHeight() / 2f - screenDrawing.getTextHeight() / 2f + 0.5f)
        screenDrawing.drawText(title.getTranslatedString(), getX() + 8, getY(), 0.5f within (0xFFFFFF to OptionsMenuSettings.themeColor.get().getColor()), 1.0F)
        screenDrawing.pop()
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        super.init()
        addRenderable(resetButton.setPosition(getX() + getWidth() - resetButton.getWidth() - 4, getY() + 4))
        addRenderable(switch.setPosition(getX() + getWidth() - switch.getWidth() - 8 - resetButton.getWidth(), getY() + 5))
    }
}