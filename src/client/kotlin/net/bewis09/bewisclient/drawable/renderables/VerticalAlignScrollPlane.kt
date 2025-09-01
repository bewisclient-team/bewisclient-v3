package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing

class VerticalAlignScrollPlane(val init: (Int) -> List<Renderable>, val gap: Int) : Scrollable(Direction.VERTICAL) {
    constructor(list: List<Renderable>, gap: Int) : this({ list }, gap)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.enableScissors(getX(), getY(), getWidth(), getHeight())
        var y = scrollAnimation["scrollY"].toInt()
        for (it in renderables) {
            it.setPosition(getX(), getY() + y)
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