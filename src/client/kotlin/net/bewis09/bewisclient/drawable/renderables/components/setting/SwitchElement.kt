package net.bewis09.bewisclient.drawable.renderables.components.setting

import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.renderite.logic.Animator
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.util.interfaces.Gettable
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.style.RenderiteChild

class SwitchElement(p: Props<SwitchElement>) : PropedRenderable<SwitchElement>(p + {
    width = 24
    height = 12
}) {
    lateinit var state: Gettable<Boolean>
    var onChange: (new: Boolean) -> Unit = {}

    init { props() }

    val stateAnimation = Animator({ General.animationDuration }, Animator.EASE_IN_OUT, { if (state.get()) 1f else 0f })

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderSwitch(screenDrawing, x, y, width, height, hoverFactor, stateAnimation.get(), mouseX, mouseY)
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean = onChange(!state.get()).let { true }
}

@RenderiteChild
fun Renderable.Switch(p: RenderiteElement.Props<SwitchElement>) = SwitchElement(p).add()