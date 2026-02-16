package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Translations
import net.bewis09.bewisclient.drawable.renderables.popup.ColorChangePopup
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.interfaces.Gettable
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.color.ColorSaver

class ColorInfoButton(val state: Gettable<ColorSaver>, val onChange: (ColorSaver) -> Unit, val types: Array<String>) : TooltipHoverable(Translations.CHANGE_COLOR()) {
    init {
        internalWidth = 160
        internalHeight = 14
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        val colorSaver = state.get()
        screenDrawing.fillWithBorderRounded(x, y, width, height, 5, colorSaver.getColor() alpha hoverFactor * 0.3f + 0.3f, colorSaver.getColor() alpha hoverFactor * 0.5f + 0.5f)
        screenDrawing.translate(0f, height / 2f - screenDrawing.getTextHeight() / 2f + 0.5f) {
            screenDrawing.drawCenteredText(colorSaver.toInfoString(), centerX, y, Color.WHITE)
        }
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        OptionScreen.currentInstance?.openPopup(ColorChangePopup(state, onChange, types))
        return true
    }
}