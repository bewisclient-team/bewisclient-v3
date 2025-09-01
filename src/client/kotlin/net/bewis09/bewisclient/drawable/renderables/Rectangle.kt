package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing

class Rectangle(var color: () -> Int) : Renderable() {
    constructor(color: Int) : this({ color })

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.fill(getX(), getY(), getWidth(), getHeight(), color())
    }
}