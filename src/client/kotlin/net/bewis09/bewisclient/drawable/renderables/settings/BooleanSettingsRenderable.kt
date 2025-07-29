package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing

class BooleanSettingsRenderable: Renderable() {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.fillRounded(getX(), getY(), getWidth(), getHeight(), 5, 0xFFFFFF, 1.0f)
        screenDrawing.drawCenteredText("Boolean Setting", getX() + getWidth() / 2, getY() + getHeight() / 2, 0x000000, 1.0F)
    }
}