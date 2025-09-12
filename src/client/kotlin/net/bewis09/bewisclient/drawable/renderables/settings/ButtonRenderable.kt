package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation

class ButtonRenderable(val title: Translation, val description: Translation) : SettingRenderable(description) {
    init {
        height = 22u
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        drawVerticalCenteredText(screenDrawing, title)
        renderRenderables(screenDrawing, mouseX, mouseY)
    }
}