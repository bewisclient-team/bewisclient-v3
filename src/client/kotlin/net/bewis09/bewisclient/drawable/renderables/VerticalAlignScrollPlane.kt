package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing

class VerticalAlignScrollPlane(val init: (Int) -> List<Renderable>, val gap: Int) : Scrollable(Direction.VERTICAL) {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.enableScissors(getX(), getY(), getWidth(), getHeight())
        var y = scrollAnimation["scrollY"]
        for (it in renderables) {
            it.setPosition(getX(), getY() + y.toInt())
            it.setWidth(getWidth())
            y += it.getHeight() + gap
        }
        innerSize = y - scrollAnimation["scrollY"] - gap
        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.disableScissors()
    }

    override fun init() {
        init.invoke(getWidth()).forEach {
            addRenderable(it)
        }
    }
}