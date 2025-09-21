package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing

class VerticalAlignScrollPlane(val init: (Int) -> List<Renderable>, val gap: Int) : Scrollable(Direction.VERTICAL) {
    constructor(list: List<Renderable>, gap: Int) : this({ list }, gap)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.enableScissors(x, y, width, height)
        var scrollY = scrollAnimation["scrollY"].toInt()
        for (it in renderables) {
            it.setPosition(x, y + scrollY)
            it.setWidth(width)
            scrollY += it.height + gap
        }
        innerSize = scrollY - scrollAnimation["scrollY"] - gap
        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.disableScissors()
    }

    override fun init() {
        init.invoke(width).forEach {
            addRenderable(it)
        }
    }
}