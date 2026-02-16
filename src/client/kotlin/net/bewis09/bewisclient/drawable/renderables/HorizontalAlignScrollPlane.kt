package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing

class HorizontalAlignScrollPlane(val init: (Int) -> List<Renderable>, val gap: Int) : Scrollable(Direction.HORIZONTAL) {
    constructor(list: List<Renderable>, gap: Int) : this({ list }, gap)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.enableScissors(x, y, width, height)
        var scrollY = scrollAnimation.get().toInt()
        for (it in renderables) {
            it.setPosition(x + scrollY, y)
            it.setHeight(height)
            scrollY += it.width + gap
        }
        innerSize = scrollY - scrollAnimation.get() - gap
        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.disableScissors()
    }

    override fun init() {
        init.invoke(height).forEach {
            addRenderable(it)
        }
    }
}