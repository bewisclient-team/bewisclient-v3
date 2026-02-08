package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.util.MathHelper
import kotlin.math.abs

abstract class Scrollable(val direction: Direction) : Renderable() {
    var scrollAnimation = Animator(200, Animator.EASE_OUT, "scrollY" to 0f)
    var innerSize = 0f

    var lastDragX = null as Double?
    var lastDragY = null as Double?

    var hasScrollStartedVertical = false
    var hasScrollStartedHorizontal = false

    override fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        scrollAnimation["scrollY"] = MathHelper.clamp(scrollAnimation.getWithoutInterpolation("scrollY") + (verticalAmount.toFloat() * 30f) + (horizontalAmount.toFloat() * 30f), 0f.coerceAtMost((if (direction == Direction.HORIZONTAL) width else height) - innerSize), 0f)
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

        val deltaX = if(hasScrollStartedHorizontal) (lastDragX ?: startX) - mouseX else 0.0
        val deltaY = if(hasScrollStartedVertical) (lastDragY ?: startY) - mouseY else 0.0

        if (direction == Direction.VERTICAL) {
            scrollAnimation["scrollY"] = MathHelper.clamp(scrollAnimation.getWithoutInterpolation("scrollY") - deltaY.toFloat(), 0f.coerceAtMost((height - innerSize)), 0f)
        } else {
            scrollAnimation["scrollY"] = MathHelper.clamp(scrollAnimation.getWithoutInterpolation("scrollY") - deltaX.toFloat(), 0f.coerceAtMost((width - innerSize)), 0f)
        }

        lastDragX = mouseX
        lastDragY = mouseY

        return true
    }

    enum class Direction {
        VERTICAL, HORIZONTAL
    }
}