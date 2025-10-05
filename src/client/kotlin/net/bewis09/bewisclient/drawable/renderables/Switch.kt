package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.animate
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.transform
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.interfaces.Gettable
import net.bewis09.bewisclient.util.color.color
import net.bewis09.bewisclient.util.color.within
import kotlin.math.abs

class Switch(var state: Gettable<Boolean>, val onChange: (new: Boolean) -> Unit) : Hoverable() {
    val stateAnimation = animate(OptionsMenuSettings.animationTime.get().toLong(), Animator.EASE_IN_OUT, "state" to if (state.get()) 1f else 0f)

    init {
        internalWidth = 24
        internalHeight = 12
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)

        stateAnimation["state"] = if (state.get()) 1f else 0f

        screenDrawing.fillWithBorderRounded(
            x, y, width, height, 6, stateAnimation["state"] within (0x333333.color to OptionsMenuSettings.themeColor.get().getColor()) alpha hoverAnimation["hovering"].coerceAtLeast(stateAnimation["state"]) * 0.15f + 0.15f, stateAnimation["state"] within (0x888888.color to OptionsMenuSettings.themeColor.get().getColor()) alpha hoverAnimation["hovering"] * 0.5f + 0.5f
        )
        val scaleFactor = 0.5f
        screenDrawing.transform(x + ((width - 12) * stateAnimation["state"]) + 6f, y + 6f, 1 - scaleFactor + abs(stateAnimation["state"] - 0.5f) * 2 * scaleFactor, 1f) {
            screenDrawing.fillRounded(
                -4, -4, 8, 8, 4, stateAnimation["state"] within (0x888888.color to OptionsMenuSettings.themeColor.get().getColor()) alpha hoverAnimation["hovering"] * 0.5f + 0.5f
            )
        }
    }

    override fun init() {
        stateAnimation.pauseForOnce()
        super.init()
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        onChange(!state.get())
        return true
    }
}