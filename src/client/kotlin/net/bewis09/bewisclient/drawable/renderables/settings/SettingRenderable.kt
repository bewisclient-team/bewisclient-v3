package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipElement
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.components.TextElement
import net.bewis09.renderite.drawer.pushColor
import net.minecraft.network.chat.Component

abstract class SettingRenderable<P: SettingRenderable<P>>(p: Props<P>) : TooltipElement<P>(p) {
    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.pushColor(0.7f, 0.7f, 0.7f, 1f) {
            SelectiveScreenDrawer.renderSettingRenderableBackground(screenDrawing, hoverAnimation.get(), x, y, width, height)
        }
    }

    fun SettingText(p: Props<TextElement<ScreenDrawing, Component, Identifier, Identifier>>) = Text(p + { paddingLeft = 8 })
}