package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.minecraft.util.math.MathHelper

open class Scrollable: Renderable() {
    var scrollAnimation = Animator(200, Animator.EASE_OUT, "scrollY" to 0f)
    var innerHeight = 0f

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        TODO("Not yet implemented")
    }

    override fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        scrollAnimation["scrollY"] = MathHelper.clamp(scrollAnimation.getWithoutInterpolation("scrollY") + verticalAmount.toFloat() * 20f, 0f.coerceAtMost(getHeight() - innerHeight), 0f)
        return super.onMouseScroll(mouseX, mouseY, horizontalAmount, verticalAmount)
    }
}