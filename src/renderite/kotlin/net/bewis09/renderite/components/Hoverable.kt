package net.bewis09.renderite.components

import net.bewis09.renderite.logic.Animator
import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General

abstract class Hoverable<P: Hoverable<P>>(p: Props<P>) : PropedRenderable<P>(p) {
    val hoverAnimation = Animator({ General.animationDuration }, Animator.LINEAR, 0f)

    val hoverFactor
        get() = hoverAnimation.get()

    override fun renderLogic(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        hoverAnimation.set(if (isMouseOver(mouseX, mouseY) && screenDrawing.scissorContains(mouseX, mouseY)) 1f else 0f)
    }

    override fun initLogic() {
        hoverAnimation.pauseForOnce()
    }
}