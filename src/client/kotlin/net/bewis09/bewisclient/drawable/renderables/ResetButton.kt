package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Translations
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.interfaces.Settable
import net.minecraft.util.Identifier

class ResetButton<T>(val setting: Settable<T?>): TooltipHoverable(Translations.RESET) {
    init {
        width = 14u
        height = 14u
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)

        screenDrawing.fillRounded(getX(), getY(), getWidth(), getHeight(), 5, OptionsMenuSettings.themeColor.get().getColor(), hoverAnimation["hovering"] * 0.15f + 0.15f)

        screenDrawing.drawTexture(Identifier.of("bewisclient", "textures/gui/sprites/reset.png"), getX() + 2, getY() + 2, getWidth() - 4, getHeight() - 4, OptionsMenuSettings.themeColor.get().getColor(), 1.0f)
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        setting.set(null)
        return true
    }
}