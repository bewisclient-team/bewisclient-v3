package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.color.alpha
import net.minecraft.text.Text

class Button(val text: Text, val onClick: (Button) -> Unit, tooltip: Translation? = null, val selected: (() -> Boolean)? = null, var dark: Boolean = false) : TooltipHoverable(tooltip) {
    constructor(text: Text, onClick: (Button) -> Unit) : this(text, onClick, null)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.fillWithBorderRounded(x, y, width, height, 5, OptionsMenuSettings.themeColor.get().getColor() alpha (hoverAnimation["hovering"] * 1 + 1) * if (dark) 0.05f else 0.15f, 0xAAAAAA alpha if (selected?.invoke() == true) 0.5f else 0f)
        screenDrawing.translate(0f, height / 2f - screenDrawing.getTextHeight() / 2f) {
            screenDrawing.drawCenteredText(text, centerX, y, OptionsMenuSettings.themeColor.get().getColor())
        }
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        onClick(this)
        return true
    }
}