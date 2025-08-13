package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import kotlin.math.floor

class HorizontalScrollGrid(val init: (Int) -> List<Renderable>, val gap: Int, val minHeight: Int): Scrollable(Direction.HORIZONTAL) {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val elementsInColumn = floor(((getHeight() + gap) / (minHeight + gap)).toDouble())
        val elementHeight = (getHeight() + gap) / elementsInColumn - gap
        val rowWidths = Array(elementsInColumn.toInt()) { 0 }

        screenDrawing.enableScissors(getX(), getY(), getWidth(), getHeight())
        for (it in renderables) {
            val min = rowWidths.minOrNull() ?: 0
            val rowIndex = rowWidths.indexOf(min)

            it.setHeight(elementHeight.toInt())
            it.setPosition(getX() + min + scrollAnimation["scrollY"].toInt(), getY() + (rowIndex * (elementHeight + gap)).toInt())
            rowWidths[rowIndex] += it.getWidth() + gap
        }
        innerSize = (rowWidths.max() - gap).toFloat()
        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.disableScissors()
    }

    override fun init() {
        val elementsInColumn = floor(((getHeight() + gap) / (minHeight + gap)).toDouble())
        val elementHeight = (getHeight() + gap) / elementsInColumn - gap

        init.invoke(elementHeight.toInt()).forEach {
            addRenderable(it)
        }
    }
}