package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Translations
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.impl.settings.functionalities.EntityHighlightSettings.alpha
import net.bewis09.bewisclient.interfaces.Settable
import net.bewis09.bewisclient.util.createIdentifier

class ResetButton<T>(val setting: Settable<T?>) : TooltipHoverable(Translations.RESET()) {
    init {
        internalWidth = 14
        internalHeight = 14
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)

        screenDrawing.fillRounded(x, y, width, height, 5, OptionsMenuSettings.getThemeColor(alpha = hoverAnimation["hovering"] * 0.15f + 0.15f))

        screenDrawing.drawTexture(createIdentifier("bewisclient", "textures/gui/sprites/reset.png"), x + 2, y + 2, width - 4, height - 4, OptionsMenuSettings.getThemeColor())
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        setting.set(null)
        return true
    }
}