package net.bewis09.bewisclient.drawable.renderables.components.element

import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.logic.Color

class Rectangle(p: Props<Rectangle>) : PropedRenderable<Rectangle>(p) {
    var colorProvider = { require { color } }
    var color: Color? = null

    init { props() }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) = screenDrawing.fill(x, y, width, height, colorProvider())
}