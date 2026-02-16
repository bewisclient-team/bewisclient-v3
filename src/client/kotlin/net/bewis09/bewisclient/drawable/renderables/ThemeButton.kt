package net.bewis09.bewisclient.drawable.renderables

import kotlinx.atomicfu.AtomicRef
import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.scale
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.minecraft.text.Text

class ThemeButton : TooltipHoverable {
    val text: Text
    val selected: () -> Boolean
    val onClick: (ThemeButton) -> Unit

    constructor(text: Text, selected: () -> Boolean, onClick: (ThemeButton) -> Unit, tooltip: Text? = null) : super(tooltip) {
        this.text = text
        this.selected = selected
        this.onClick = onClick
    }

    constructor(id: String, text: Text, selectedButtonRef: AtomicRef<String>, onClick: (ThemeButton) -> Unit, tooltip: Text? = null) : super(tooltip) {
        this.text = text
        this.selected = { selectedButtonRef.value == id }
        this.onClick = {
            selectedButtonRef.value = id
            onClick(it)
        }
    }

    constructor(id: String, text: Text, clickButton: AtomicRef<String>, onClick: (ThemeButton) -> Unit) : this(id, text, clickButton, onClick, null)

    constructor(text: Text, onClick: (ThemeButton) -> Unit) {
        this.text = text
        this.selected = { clickAnimation.get() < 1f && clickAnimation.getWithoutInterpolation() == 0f }
        this.onClick = onClick
    }

    val clickAnimation: Animator = Animator(200, Animator.EASE_IN_OUT, 1f)
    val colorAnimation: Animator = Animator(200, Animator.EASE_IN_OUT, 0f)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        colorAnimation.set(if (selected()) 1f else 0f)
        val click = clickAnimation.get()
        screenDrawing.translate(centerX.toFloat(), centerY.toFloat()) {
            screenDrawing.scale(0.9f + 0.1f * click, 0.9f + 0.1f * click) {
                screenDrawing.translate(-width / 2f, -height / 2f)
                screenDrawing.fillWithBorderRounded(0, 0, width, height, 5, OptionsMenuSettings.getThemeColor(alpha = (hoverFactor.coerceAtLeast(colorAnimation.get()) + 1) * 0.15f), OptionsMenuSettings.getThemeColor(alpha = colorAnimation.get() * 0.5f))
            }

            screenDrawing.scale(0.95f + 0.05f * click, 0.95f + 0.05f * click) {
                screenDrawing.translate(0f, -screenDrawing.getTextHeight() / 2f)
                screenDrawing.drawCenteredText(text, 0, 0, OptionsMenuSettings.getTextThemeColor())
            }
        }
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        colorAnimation.set(1f)
        onClick(this)
        clickAnimation.set(0f) { set(1f) }
        return true
    }
}