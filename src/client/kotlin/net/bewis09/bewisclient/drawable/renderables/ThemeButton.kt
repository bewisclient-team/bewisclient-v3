package net.bewis09.bewisclient.drawable.renderables

import kotlinx.atomicfu.AtomicRef
import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.animate
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.then
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.color.alpha
import net.bewis09.bewisclient.logic.color.within
import java.awt.Color

class ThemeButton : TooltipHoverable {
    val text: String
    val selected: () -> Boolean
    val onClick: (ThemeButton) -> Unit

    constructor(text: String, selected: () -> Boolean, onClick: (ThemeButton) -> Unit, tooltip: Translation? = null) : super(tooltip) {
        this.text = text
        this.selected = selected
        this.onClick = onClick
    }

    constructor(text: String, selectedButtonRef: AtomicRef<ThemeButton?>, onClick: (ThemeButton) -> Unit, tooltip: Translation? = null) : super(tooltip) {
        this.text = text
        this.selected = { selectedButtonRef.value == this }
        this.onClick = {
            selectedButtonRef.value = this
            onClick(it)
        }
    }

    constructor(text: String, clickButton: AtomicRef<ThemeButton?>, onClick: (ThemeButton) -> Unit) : this(text, clickButton, onClick, null)

    constructor(text: String, onClick: (ThemeButton) -> Unit) {
        this.text = text
        this.selected = { clickAnimation["click"] < 1f && clickAnimation.getWithoutInterpolation("click") == 0f }
        this.onClick = onClick
    }

    val clickAnimation: Animator = animate(200, Animator.EASE_IN_OUT, "click" to 1f, "color" to 0f)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        clickAnimation["color"] = if (selected()) 1f else 0f
        screenDrawing.push()
        screenDrawing.translate(getX() + getWidth() / 2f, getHeight() / 2f + getY())
        screenDrawing.push()
        screenDrawing.scale(0.9f + 0.1f * clickAnimation["click"], 0.9f + 0.1f * clickAnimation["click"])
        screenDrawing.translate(-getWidth() / 2f, -getHeight() / 2f)
        val color = OptionsMenuSettings.themeColor.get().getColor()
        screenDrawing.fillWithBorderRounded(0, 0, getWidth(), getHeight(), 5, color alpha (hoverAnimation["hovering"].coerceAtLeast(clickAnimation["color"]) + 1) * 0.15f, color alpha clickAnimation["color"] * 0.5f)
        screenDrawing.pop()
        screenDrawing.scale(0.95f + 0.05f * clickAnimation["click"], 0.95f + 0.05f * clickAnimation["click"])
        screenDrawing.translate(0f, -screenDrawing.getTextHeight() / 2f)
        screenDrawing.drawCenteredText(text, 0, 0, 0.5f within (Color.WHITE to OptionsMenuSettings.themeColor.get().getColor()))
        screenDrawing.pop()
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        clickAnimation["color"] = 1f
        onClick(this)
        clickAnimation["click"] = 0f then {
            clickAnimation["click"] = 1f
        }
        return true
    }
}