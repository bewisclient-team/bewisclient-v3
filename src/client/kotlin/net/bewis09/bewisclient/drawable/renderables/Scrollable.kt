package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.util.MathHelper

abstract class Scrollable(val direction: Direction) : Renderable() {
    var scrollAnimation = Animator(200, Animator.EASE_OUT, "scrollY" to 0f)
    var innerSize = 0f

    override fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        scrollAnimation["scrollY"] = MathHelper.clamp(scrollAnimation.getWithoutInterpolation("scrollY") + (verticalAmount.toFloat() * 30f) + (horizontalAmount.toFloat() * 30f), 0f.coerceAtMost((if (direction == Direction.HORIZONTAL) width else height) - innerSize), 0f)
        return true
    }

    enum class Direction {
        VERTICAL, HORIZONTAL
    }
}