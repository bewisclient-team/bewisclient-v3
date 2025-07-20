package net.bewis09.bewisclient.impl.screen

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing

class OptionScreen: Renderable() {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.drawContext.drawTextWithShadow(screenDrawing.textRenderer, "Options Screen", x + 10, y + 20, 0xFFFFFFFF.toInt())
        screenDrawing.drawTextWithShadow("Options Screen", x + 10, y + 10, System.currentTimeMillis() % 1000 / 1000f, System.currentTimeMillis() % 500 / 500f, System.currentTimeMillis() % 750 / 750f, 1f)
    }
}