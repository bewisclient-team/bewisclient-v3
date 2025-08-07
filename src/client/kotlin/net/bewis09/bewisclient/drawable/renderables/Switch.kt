package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.drawable.animate
import net.bewis09.bewisclient.drawable.interpolateColor
import kotlin.math.abs

class Switch(var state: Boolean, val onChange: (new: Boolean) -> Unit): Hoverable() {
    val stateAnimation = animate(200, Animator.EASE_IN_OUT, "state" to if (state) 1f else 0f)

    init {
        width = 28u
        height = 14u
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.fillWithBorderRounded(getX(), getY(), getWidth(), getHeight(), 7, interpolateColor(0xAAAAAA, 0x00FF00, stateAnimation["state"]), hoverAnimation["hovering"] * 0.15f + 0.15f, interpolateColor(0xAAAAAA, 0x00AA00, stateAnimation["state"]), hoverAnimation["hovering"] * 0.5f + 0.5f)
        screenDrawing.push()
        screenDrawing.translate(getX() + ((getWidth() - 14) * stateAnimation["state"]) + 7f, getY() + 7f)

        val scaleFactor = 0.5f
        screenDrawing.scale(1 - scaleFactor + abs(stateAnimation["state"] - 0.5f) * 2 * scaleFactor,1f)
        screenDrawing.fillRounded(
            -5,
            -5,
            10,
            10,
            5,
            interpolateColor(0xAAAAAA, 0x00AA00, stateAnimation["state"]),
            hoverAnimation["hovering"] * 0.5f + 0.5f
        )
        screenDrawing.pop()
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        state = !state
        onChange(state)
        stateAnimation["state"] = if (state) 1f else 0f
        return true
    }
}