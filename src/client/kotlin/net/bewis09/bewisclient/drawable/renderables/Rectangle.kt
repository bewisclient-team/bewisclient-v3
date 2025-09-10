package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import java.awt.Color

class Rectangle(var color: () -> Color) : Renderable() {
    constructor(color: Color) : this({ color })

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.fill(getX(), getY(), getWidth(), getHeight(), color())
    }
}