package net.bewis09.renderite.components

import net.bewis09.renderite.logic.Animator
import net.bewis09.renderite.logic.Direction
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.drawer.RenderiteDrawer
import kotlin.math.abs

abstract class Scrollable<S: RenderiteDrawer<I, A, F>, T: Scrollable<S, T, A, F, I>, A: Any, F, I: Any>(p: Props<T>) : RenderiteElement<S, T, A, F, I>(p) {
    lateinit var direction: Direction

    val scrollAnimation = Animator(200, Animator.EASE_OUT, 0f)
    var innerSize = 0f

    private var lastDragX = null as Double?
    private var lastDragY = null as Double?

    private var hasScrollStartedVertical = false
    private var hasScrollStartedHorizontal = false

    override fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        scrollAnimation.set((scrollAnimation.getWithoutInterpolation() + (verticalAmount.toFloat() * 30f) + (horizontalAmount.toFloat() * 30f)).coerceIn(0f.coerceAtMost((if (direction == Direction.HORIZONTAL) width else height) - innerSize), 0f))
        return true
    }

    override fun onMouseRelease(mouseX: Double, mouseY: Double, button: Int) {
        if (button != 0) return

        lastDragX = null
        lastDragY = null

        hasScrollStartedVertical = false
        hasScrollStartedHorizontal = false

        return
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button != 0) return false

        lastDragX = null
        lastDragY = null

        hasScrollStartedVertical = false
        hasScrollStartedHorizontal = false

        return false
    }

    override fun onMouseDrag(mouseX: Double, mouseY: Double, startX: Double, startY: Double, button: Int): Boolean {
        if (button != 0) return false

        if (abs(startX - mouseX) > 5 && direction == Direction.HORIZONTAL) hasScrollStartedHorizontal = true
        if (abs(startY - mouseY) > 5 && direction == Direction.VERTICAL) hasScrollStartedVertical = true

        val deltaX = if (hasScrollStartedHorizontal) (lastDragX ?: startX) - mouseX else 0.0
        val deltaY = if (hasScrollStartedVertical) (lastDragY ?: startY) - mouseY else 0.0

        if (direction == Direction.VERTICAL) {
            scrollAnimation.set((scrollAnimation.getWithoutInterpolation() - deltaY.toFloat()).coerceIn(0f.coerceAtMost((height - innerSize)), 0f))
        } else {
            scrollAnimation.set((scrollAnimation.getWithoutInterpolation() - deltaX.toFloat()).coerceIn(0f.coerceAtMost((width - innerSize)), 0f))
        }

        lastDragX = mouseX
        lastDragY = mouseY

        return true
    }

}