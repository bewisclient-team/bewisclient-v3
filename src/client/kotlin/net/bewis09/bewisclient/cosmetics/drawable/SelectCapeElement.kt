package net.bewis09.bewisclient.cosmetics.drawable

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.color.Color

class SelectCapeElement: Renderable() {
    init {
        this.internalWidth = 100
        this.internalHeight = 100
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.fill(x, y, width, height, Color.BLACK alpha 0.5f)
    }
}