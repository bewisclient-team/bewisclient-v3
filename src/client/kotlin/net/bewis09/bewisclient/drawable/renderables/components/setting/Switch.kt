package net.bewis09.bewisclient.drawable.renderables.components.setting

import net.bewis09.bewisclient.drawable.Init
import net.bewis09.renderite.logic.Animator
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.renderite.components.Hoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.util.interfaces.Gettable
import net.bewis09.renderite.RenderiteElement

class Switch(p: Props<Switch>) : Hoverable<Switch>(p + {
    width = 24
    height = 12
}) {
    lateinit var state: Gettable<Boolean>
    var onChange: (new: Boolean) -> Unit = {}

    init { props() }

    val stateAnimation = Animator({ General.animationDuration }, Animator.EASE_IN_OUT, if (state.get()) 1f else 0f)

    override fun renderLogic(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.renderLogic(screenDrawing, mouseX, mouseY)
        stateAnimation.set(if (state.get()) 1f else 0f)
    }

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderSwitch(screenDrawing, x, y, width, height, hoverFactor, stateAnimation.get(), mouseX, mouseY)
    }

    override fun Init.init() {
        stateAnimation.pauseForOnce()
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean = onChange(!state.get()).let { true }
}

fun Init.Switch(p: RenderiteElement.Props<Switch>) = net.bewis09.bewisclient.drawable.renderables.components.setting.Switch(p).add()