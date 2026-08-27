package net.bewis09.renderite.components

import net.bewis09.bewisclient.util.BewisclientDataGenerator
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.drawer.RenderiteDrawer
import net.bewis09.renderite.logic.Direction
import net.bewis09.renderite.logic.FitType
import net.bewis09.renderite.logic.LineType
import kotlin.math.roundToInt

open class Div<S: RenderiteDrawer<I, A, F>, A: Any, F, I: Any>(p: Props<Div<S, A, F, I>>) : Scrollable<S, Div<S, A, F, I>, A, F, I>(fun Div<S, A, F, I>.() { direction = Direction.VERTICAL } + p) {
    var onInit: Init.(Int) -> Unit = {}
    var gap: Int = 0
    var minElementSize: Int = 100
    var paddingBottom: Int = 0
    var lines = 1
    var lineType: LineType = LineType.DEFINITE
    var fitType: FitType = FitType.ENLARGE
    var elementsPerLine = 1
    var cacheChildren = false

    private var elementCache: List<RenderiteElement<S, *, A, F, I>>? = null

    init {
        props()
        if (BewisclientDataGenerator.datagenEnabled) {
            width = 1000
            height = 1000
            resize()
        }
    }

    companion object {
        fun <S: RenderiteDrawer<I, A, F>, A: Any, F, I: Any> create(p: Props<Div<S, A, F, I>>): Div<S, A, F, I> = Div(p)
    }

    override fun renderLogic(screenDrawing: S, mouseX: Int, mouseY: Int) {
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
            innerSize = ((linePosition.maxOrNull() ?: 0f) - gap + paddingBottom) - startScroll
        } else if (fitType == FitType.ENLARGE) {
            if (direction == Direction.HORIZONTAL)
                width = (linePosition.maxOrNull() ?: 0f).toInt() - gap + paddingBottom
            else
                height = (linePosition.maxOrNull() ?: 0f).toInt() - gap + paddingBottom
        }
    }

    fun getTotalLinesSpan() = if (direction == Direction.HORIZONTAL) height else width

    fun getOtherSpan() = if (direction == Direction.HORIZONTAL) width else height

    fun getElementSize(): Double = (getTotalLinesSpan() + gap) / getElementsInLine().toDouble() - gap

    fun getElementsInLine(): Int = if (lineType == LineType.DEFINITE) lines else (getTotalLinesSpan() / (minElementSize + gap)).coerceAtLeast(1)

    override fun Init.init() {
        val cache = elementCache

        if (cacheChildren && cache != null) {
            addRenderables(cache)
        } else {
            onInit.invoke(this, getElementSize().toInt())
            elementCache = ArrayList(renderables)
        }

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

    fun <T> initForEach(collection: Collection<T>?, func: Init.(item: T) -> Unit) {
        onInit = { collection?.forEach { this.func(it) } }
    }

    fun <T, L> initForEach(map: Map<T, L>?, func: Init.(item: Map.Entry<T, L>) -> Unit) {
        onInit = { map?.forEach { this.func(it) } }
    }

    fun <T> initForEachIndexed(collection: Collection<T>?, func: Init.(i: Int, item: T) -> Unit) {
        onInit = { collection?.forEachIndexed { i, item -> this.func(i, item) } }
    }
}