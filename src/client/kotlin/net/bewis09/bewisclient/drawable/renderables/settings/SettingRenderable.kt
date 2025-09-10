package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.renderables.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.color.alpha

abstract class SettingRenderable(tooltip: () -> Translation?) : TooltipHoverable(tooltip) {
    constructor(tooltip: Translation? = null) : this({ tooltip })

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.fillRounded(getX(), getY(), getWidth(), getHeight(), 5, OptionsMenuSettings.themeColor.get().getColor() alpha hoverAnimation["hovering"] * 0.15f + 0.1f)
    }
}