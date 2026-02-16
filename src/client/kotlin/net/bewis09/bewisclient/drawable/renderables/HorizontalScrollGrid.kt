package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import kotlin.math.floor

class HorizontalScrollGrid(val init: (Int) -> List<Renderable>, val gap: Int, val minHeight: Int) : Scrollable(Direction.HORIZONTAL) {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val elementsInColumn = floor(((height + gap) / (minHeight + gap)).toDouble())
        val elementHeight = (height + gap) / elementsInColumn - gap
        val rowWidths = Array(elementsInColumn.toInt()) { 0 }

        screenDrawing.enableScissors(x, y, width, height)
        for (it in renderables) {
            val min = rowWidths.minOrNull() ?: 0
            val rowIndex = rowWidths.indexOf(min)

            it.setHeight(elementHeight.toInt())
            it.setPosition(x + min + scrollAnimation.get().toInt(), y + (rowIndex * (elementHeight + gap)).toInt())
            rowWidths[rowIndex] += it.width + gap
        }
        innerSize = (rowWidths.max() - gap).toFloat()
        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.disableScissors()
    }

    override fun init() {
        val elementsInColumn = floor(((height + gap) / (minHeight + gap)).toDouble())
        val elementHeight = (height + gap) / elementsInColumn - gap

        init.invoke(elementHeight.toInt()).forEach {
            addRenderable(it)
        }
    }
}