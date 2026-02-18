package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing

class VerticalAlignPlane(val elements: List<Renderable>, val gap: Int = 5): Renderable() {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        var currentY = y
        for (it in renderables) {
            it.setPosition(x, currentY)
            it.setWidth(width)
            currentY += it.height + gap
        }
        internalHeight = currentY - y - gap
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        elements.forEach { addRenderable(it.setWidth(this.width)) }
    }
}