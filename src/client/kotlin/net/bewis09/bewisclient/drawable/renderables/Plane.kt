package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing

class Plane(val init: (x: Int, y: Int, width: Int, height: Int) -> List<Renderable>) : Renderable() {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        this.renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        init.invoke(getX(), getY(), getWidth(), getHeight()).forEach {
            addRenderable(it)
        }
    }
}