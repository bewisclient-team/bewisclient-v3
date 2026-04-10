package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.Animator.Companion.LINEAR
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing

open class Hoverable(
    minWidth: Int = 0,
    minHeight: Int = 0,
    widthProvider: (Renderable.() -> Int)? = null,
    heightProvider: (Renderable.() -> Int)? = null
) : Renderable(minWidth, minHeight, widthProvider, heightProvider) {
    val hoverAnimation = Animator(200, LINEAR, 0f)

    val hoverFactor
        get() = hoverAnimation.get()

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        hoverAnimation.set(if (isMouseOver(mouseX.toDouble(), mouseY.toDouble()) && screenDrawing.scissorContains(mouseX, mouseY)) 1f else 0f)
    }

    override fun init() {
        hoverAnimation.pauseForOnce()
    }
}