package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.Renderable
import net.minecraft.util.math.MathHelper

abstract class Scrollable(val direction: Direction) : Renderable() {
    var scrollAnimation = Animator(200, Animator.EASE_OUT, "scrollY" to 0f, "scrollX" to 0f)
    var innerSize = 0f

    override fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        scrollAnimation["scrollY"] = MathHelper.clamp(scrollAnimation.getWithoutInterpolation("scrollY") + (verticalAmount.toFloat() * 20f) + (horizontalAmount.toFloat() * 20f), 0f.coerceAtMost((if (direction == Direction.HORIZONTAL) getWidth() else getHeight()) - innerSize), 0f)
        return super.onMouseScroll(mouseX, mouseY, horizontalAmount, verticalAmount)
    }

    enum class Direction {
        VERTICAL, HORIZONTAL
    }
}