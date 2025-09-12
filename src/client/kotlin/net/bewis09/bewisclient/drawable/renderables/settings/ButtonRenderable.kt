package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.color.Color
import net.bewis09.bewisclient.logic.color.within

class ButtonRenderable(val text: Translation, val description: Translation) : SettingRenderable(description) {
    init {
        height = 22u
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.push()
        screenDrawing.translate(0f, getHeight() / 2f - screenDrawing.getTextHeight() / 2f + 0.5f)
        screenDrawing.drawText(text(), getX() + 8, getY(), 0.5f within (Color.WHITE to OptionsMenuSettings.themeColor.get().getColor()))
        screenDrawing.pop()
        renderRenderables(screenDrawing, mouseX, mouseY)
    }
}