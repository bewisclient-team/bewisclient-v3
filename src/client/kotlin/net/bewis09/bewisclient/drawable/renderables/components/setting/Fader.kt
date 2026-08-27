package net.bewis09.bewisclient.drawable.renderables.components.setting

import net.bewis09.bewisclient.drawable.Init
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.renderite.components.Hoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.interfaces.Gettable
import net.bewis09.bewisclient.util.number.Precision
import net.bewis09.renderite.RenderiteElement

class Fader(p: Props<Fader>) : Hoverable<Fader>(p + {
    width = 100
    height = 14
}) {
    lateinit var value: Gettable<Float>
    lateinit var precision: Precision
    var onChange: (new: Float) -> Unit = {}

    init { props() }

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderFader(screenDrawing, x, y, width, height, hoverAnimation.get(), precision.normalize(value.get()), mouseX, mouseY)
    }

    override fun onMouseDrag(mouseX: Double, mouseY: Double, startX: Double, startY: Double, button: Int): Boolean {
        return onMouseClick(mouseX, mouseY, button)
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val relativeX = mouseX - x - 4
        var newValue = precision.denormalize(
            (relativeX / (width - 8)).coerceIn(0.0, 1.0).toFloat()
        )
        newValue = precision.getNearestStep(newValue)
        newValue = precision.round(newValue)
        if (newValue == value.get()) return true
        onChange(newValue)
        return true
    }
}

fun Init.Fader(p: RenderiteElement.Props<Fader>) = net.bewis09.bewisclient.drawable.renderables.components.setting.Fader(p).add()