package net.bewis09.renderite.components

import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.drawer.RenderiteDrawer
import net.bewis09.renderite.logic.Color

class Rectangle<S: RenderiteDrawer<I, T, F>, T: Any, F, I: Any>(p: Props<Rectangle<S, T, F, I>>) : RenderiteElement<S, Rectangle<S, T, F, I>, T, F, I>(p) {
    var colorProvider = { require { color } }
    var color: Color = Color.TRANSPARENT

    init { props() }

    override fun renderElement(screenDrawing: S, mouseX: Int, mouseY: Int) = screenDrawing.fill(x, y, width, height, colorProvider())
}