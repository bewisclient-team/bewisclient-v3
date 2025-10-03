package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.core.BewisclientID
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.logic.color.Color

open class Image(val image: BewisclientID, val color: Color) : Renderable() {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.drawTexture(image, x, y, width, height, color)
    }
}