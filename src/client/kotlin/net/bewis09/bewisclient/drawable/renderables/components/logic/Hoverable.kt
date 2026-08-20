package net.bewis09.bewisclient.drawable.renderables.components.logic

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.Animator.Companion.LINEAR
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.RenderiteElement

open class Hoverable(
    minWidth: Int = 0,
    minHeight: Int = 0,
    widthProvider: (RenderiteElement<ScreenDrawing>.() -> Int)? = null,
    heightProvider: (RenderiteElement<ScreenDrawing>.() -> Int)? = null
) : Renderable(minWidth, minHeight, widthProvider, heightProvider) {
    val hoverAnimation = Animator({ General.animationDuration }, LINEAR, 0f)

    val hoverFactor
        get() = hoverAnimation.get()

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        hoverAnimation.set(if (isMouseOver(mouseX, mouseY) && screenDrawing.scissorContains(mouseX, mouseY)) 1f else 0f)
    }

    override fun init() {
        hoverAnimation.pauseForOnce()
    }
}