package net.bewis09.bewisclient.drawable.renderables.components.structure

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.components.logic.Scrollable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing

class VerticalAlignScrollPlane(val init: (Int) -> List<Renderable>, val gap: Int, val paddingBottom: Int = 0) : Scrollable(Direction.VERTICAL) {
    constructor(list: List<Renderable>, gap: Int, paddingBottom: Int = 0) : this({ list }, gap, paddingBottom)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.enableScissors(x, y, width, height)
        var scrollY = scrollAnimation.get().toInt()
        for (it in renderables) {
            it.setPosition(x, y + scrollY)
            it.setWidth(width)
            scrollY += it.height + gap
        }
        innerSize = scrollY - scrollAnimation.get() - gap + paddingBottom
        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.disableScissors()
    }

    override fun init() = init.invoke(width).forEach {
        addRenderable(it)
    }
}