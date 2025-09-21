package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.renderables.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.color.Color
import net.bewis09.bewisclient.logic.color.within

abstract class SettingRenderable(tooltip: () -> Translation?, height: Int) : TooltipHoverable(tooltip) {
    constructor(tooltip: Translation? = null, height: Int) : this({ tooltip }, height)

    init {
        internalHeight = height
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.fillRounded(x, y, width, height, 5, OptionsMenuSettings.themeColor.get().getColor() alpha hoverAnimation["hovering"] * 0.15f + 0.1f)
    }

    fun drawVerticalCenteredText(screenDrawing: ScreenDrawing, title: Translation) {
        screenDrawing.translate(0f, height / 2f - screenDrawing.getTextHeight() / 2f + 0.5f) {
            screenDrawing.drawText(title(), x + 8, y, 0.5f within (Color.WHITE to OptionsMenuSettings.themeColor.get().getColor()))
        }
    }
}