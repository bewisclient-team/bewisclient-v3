package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing

open class Plane(val init: (x: Int, y: Int, width: Int, height: Int) -> List<Renderable>) : Renderable() {
    init {
        init(0, 0, 480, 270)
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        this.renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        init.invoke(x, y, width, height).forEach {
            addRenderable(it)
        }
    }
}