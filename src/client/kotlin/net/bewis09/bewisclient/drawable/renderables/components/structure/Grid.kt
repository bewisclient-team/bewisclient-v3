package net.bewis09.bewisclient.drawable.renderables.components.structure

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.components.logic.Direction
import net.bewis09.bewisclient.drawable.renderables.components.logic.Scrollable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import kotlin.math.roundToInt

open class Grid(p: Props<Grid>) : Scrollable<Grid>(fun Grid.() { direction = Direction.VERTICAL } + p) {
    var init: (Int) -> List<Renderable> = { children }
    var children: List<Renderable> = emptyList()
    var gap: Int = 0
    var minElementSize: Int = 100
    var paddingBottom: Int = 0
    var lines = 1
    var lineType: LineType = LineType.DEFINITE
    var fitType: FitType = FitType.ENLARGE
    var elementsPerLine = 1

    init {
        props()
    }

    override fun renderLogic(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        updateSizeAndPosition()
    }

    fun updateSizeAndPosition() {
        val elementSize = getElementSize()
        val startScroll = scrollAnimation.get().toInt()
        val linePosition = Array(getElementsInLine()) { startScroll.toFloat() }

        val fitHeight = (getOtherSpan() + gap) / elementsPerLine.toDouble() - gap

        for (it in renderables) {
            val min = linePosition.minOrNull()?.toInt() ?: 0
            val lineIndex = linePosition.indexOf(min.toFloat())

            if (direction == Direction.VERTICAL) {
                it.updateWidth(elementSize.toInt())
                it.updatePosition(x + (lineIndex * (elementSize + gap)).roundToInt(), y + min)

                if (fitType == FitType.FIT) {
                    it.updateHeight(fitHeight.toInt())
                    linePosition[lineIndex] += fitHeight.toFloat() + gap
                } else {
                    linePosition[lineIndex] += it.height + gap
                }
            } else {
                it.updateHeight(elementSize.toInt())
                it.updatePosition(x + min, y + (lineIndex * (elementSize + gap)).roundToInt())

                if (fitType == FitType.FIT) {
                    it.updateWidth(fitHeight.toInt())
                    linePosition[lineIndex] += fitHeight.toFloat() + gap
                } else {
                    linePosition[lineIndex] += it.width + gap
                }
            }
        }

        if (fitType == FitType.SCROLL) {
            innerSize = (linePosition.max() - gap + paddingBottom) - startScroll
        } else if (fitType == FitType.ENLARGE) {
            if (direction == Direction.HORIZONTAL)
                width = linePosition.max().toInt() - gap + paddingBottom
            else
                height = linePosition.max().toInt() - gap + paddingBottom
        }
    }

    fun getTotalLinesSpan() = if (direction == Direction.HORIZONTAL) height else width

    fun getOtherSpan() = if (direction == Direction.HORIZONTAL) width else height

    fun getElementSize(): Double = (getTotalLinesSpan() + gap) / getElementsInLine().toDouble() - gap

    fun getElementsInLine(): Int = if (lineType == LineType.DEFINITE) lines else (getTotalLinesSpan() / (minElementSize + gap)).coerceAtLeast(1)

    override fun init() {
        this.init.invoke(getElementSize().toInt()).forEach(::addRenderable)
        updateSizeAndPosition()
    }

    override fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        if (fitType != FitType.SCROLL) return false
        return super.onMouseScroll(mouseX, mouseY, horizontalAmount, verticalAmount)
    }

    override fun onMouseRelease(mouseX: Double, mouseY: Double, button: Int) {
        if (fitType != FitType.SCROLL) return
        super.onMouseRelease(mouseX, mouseY, button)
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (fitType != FitType.SCROLL) return false
        return super.onMouseClick(mouseX, mouseY, button)
    }

    override fun onMouseDrag(mouseX: Double, mouseY: Double, startX: Double, startY: Double, button: Int): Boolean {
        if (fitType != FitType.SCROLL) return false
        return super.onMouseDrag(mouseX, mouseY, startX, startY, button)
    }

    enum class LineType {
        DEFINITE,
        SIZED
    }

    enum class FitType {
        ENLARGE,
        FIT,
        SCROLL
    }
}