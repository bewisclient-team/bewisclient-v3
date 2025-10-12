package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.Animator.Companion.LINEAR
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing

open class Hoverable(duration: Long = 200) : Renderable() {
    val hoverAnimation = Animator(duration, LINEAR, "hovering" to 0f)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        hoverAnimation["hovering"] = if (isMouseOver(mouseX.toDouble(), mouseY.toDouble()) && screenDrawing.scissorContains(mouseX, mouseY)) 1f else 0f
    }

    override fun init() {
        hoverAnimation.pauseForOnce()
    }
}