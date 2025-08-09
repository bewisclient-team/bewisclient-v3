package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.Animator.Companion.LINEAR
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing

open class Hoverable: Renderable() {
    val hoverAnimation = Animator(200, LINEAR, "hovering" to 0f)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        hoverAnimation["hovering"] = if(isMouseOver(mouseX.toDouble(), mouseY.toDouble())) 1f else 0f
    }

    override fun init() {
        hoverAnimation.pauseForOnce()
    }
}