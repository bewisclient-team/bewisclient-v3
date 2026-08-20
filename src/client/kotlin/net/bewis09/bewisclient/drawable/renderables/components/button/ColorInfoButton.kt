package net.bewis09.bewisclient.drawable.renderables.components.button

import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.renderables.popup.ColorChangePopup
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.util.color.ColorSaver
import net.bewis09.bewisclient.util.interfaces.Gettable
import net.bewis09.renderite.logic.Color

class ColorInfoButton(val state: Gettable<ColorSaver>, val onChange: (ColorSaver) -> Unit, val types: Array<String>) : TooltipHoverable(changeColorTranslation(), 160, 14) {
    companion object {
        val changeColorTranslation = Translation("menu.color.change_color", "Change Color")
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        val colorSaver = state.get()
        usePointer(screenDrawing, mouseX, mouseY)
        screenDrawing.fillWithBorderRounded(x, y, width, height, if (General.isMinecrafty) 0 else 5, colorSaver.getColor() alpha hoverFactor * 0.3f + 0.3f, colorSaver.getColor() alpha hoverFactor * 0.5f + 0.5f)
        screenDrawing.drawCenteredText(colorSaver.toInfoString(), exactCenterX, screenDrawing.getTextYCenter(this) + 0.5f, Color.WHITE)
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        OptionScreen.currentInstance?.openPopup(ColorChangePopup(state, onChange, types))
        return true
    }
}