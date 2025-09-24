package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.logic.then

class ScrollPlane(direction: Direction, val init: (x: Int, y: Int, width: Int, height: Int, scroll: Float) -> List<Renderable>) : Scrollable(direction) {
    var lastScroll = scrollAnimation["scrollY"]
    var lastSize = innerSize

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        if (lastScroll != scrollAnimation["scrollY"] || lastSize != innerSize) {
            resize()
            lastScroll = scrollAnimation["scrollY"]
            lastSize = innerSize
        }
        screenDrawing.enableScissors(x, y, width, height)
        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.disableScissors()
    }

    override fun init() {
        init.invoke(x, y, ((direction == Direction.HORIZONTAL) then innerSize.toInt()) ?: width, ((direction == Direction.VERTICAL) then innerSize.toInt()) ?: height, scrollAnimation["scrollY"]).forEach {
            addRenderable(it)
        }
    }
}