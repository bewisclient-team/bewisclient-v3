package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.pushColor
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.features.sidebar.General

abstract class SettingRenderable(tooltip: () -> Translation?, height: Int) : TooltipHoverable({ tooltip()?.invoke() }, minHeight = height) {
    constructor(tooltip: Translation? = null, height: Int) : this({ tooltip }, height)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.pushColor(0.7f, 0.7f, 0.7f, 1f) {
            SelectiveScreenDrawer.renderSettingRenderableBackground(screenDrawing, hoverAnimation.get(), x, y, width, height, mouseX, mouseY)
        }
    }

    fun drawVerticalCenteredText(screenDrawing: ScreenDrawing, title: Translation) {
        screenDrawing.drawText(title(), x + 8, fontYCenter + if(isMinecrafty) 0f else 0.5f, General.getTextThemeColor())
    }
}