package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import kotlin.math.floor

class VerticalScrollGrid(val init: (Int) -> List<Renderable>, val gap: Int, val minWidth: Int): Scrollable(Direction.VERTICAL) {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        if (renderables.isEmpty()) return

        val elementsInRow = floor(((getWidth() + gap) / (minWidth + gap)).toDouble())
        val elementWidth = (getWidth() + gap) / elementsInRow - gap
        val columnHeights = Array(elementsInRow.toInt()) { 0 }

        screenDrawing.enableScissors(getX(), getY(), getWidth(), getHeight())
        for (it in renderables) {
            val min = columnHeights.minOrNull() ?: 0
            val columnIndex = columnHeights.indexOf(min)

            it.setWidth(elementWidth.toInt())
            it.setPosition(getX() + (columnIndex * (elementWidth + gap)).toInt(), getY() + min + scrollAnimation["scrollY"].toInt())
            columnHeights[columnIndex] += it.getHeight() + gap
        }
        innerSize = (columnHeights.max() - gap).toFloat()
        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.disableScissors()
    }

    override fun init() {
        val elementsInRow = floor(((getWidth() + gap) / (minWidth + gap)).toDouble())
        val elementWidth = (getWidth() + gap) / elementsInRow - gap

        init.invoke(elementWidth.toInt()).forEach {
            addRenderable(it)
        }
    }
}