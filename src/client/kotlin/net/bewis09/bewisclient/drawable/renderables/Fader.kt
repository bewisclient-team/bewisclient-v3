package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.drawable.interpolateColor
import net.bewis09.bewisclient.interfaces.Gettable
import net.bewis09.bewisclient.logic.number.Precision

class Fader(val value: Gettable<Float>, val precision: Precision, val onChange: (new: Float) -> Unit) : Hoverable() {
    init {
        width = 100u
        height = 14u
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        val normalizedValue = precision.normalize(value.get())
        screenDrawing.fillRounded(
            getX(), getY() + 5, getWidth(), 4,
            2, 0xAAAAAA, hoverAnimation["hovering"] * 0.15f + 0.15f
        )
        screenDrawing.push()
        screenDrawing.translate(getX() + normalizedValue * (getWidth() - 8) + 4, getY() + 2f)
        screenDrawing.scale(0.1f, 0.1f)
        screenDrawing.fillRounded(
            -20, 0, 40, 100, 20, interpolateColor(0xAAAAAA, 0xDDDDDD, hoverAnimation["hovering"]), 1f
        )
        screenDrawing.pop()
    }

    override fun onMouseDrag(mouseX: Double, mouseY: Double, startX: Double, startY: Double, button: Int): Boolean {
        return onMouseClick(mouseX, mouseY, button)
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val relativeX = mouseX - getX() - 4
        var newValue = precision.denormalize(
            (relativeX / (getWidth() - 8)).coerceIn(0.0, 1.0).toFloat()
        )
        newValue = precision.getNearestStep(newValue)
        newValue = precision.round(newValue)
        if (newValue == value.get()) return true
        onChange(newValue)
        return true
    }
}