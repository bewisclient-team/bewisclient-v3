package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.Text
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.drawer.pushColor
import net.minecraft.network.chat.Component

abstract class SettingRenderable<P: SettingRenderable<P>>(p: Props<P>) : TooltipHoverable<P>(p) {
    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.pushColor(0.7f, 0.7f, 0.7f, 1f) {
            SelectiveScreenDrawer.renderSettingRenderableBackground(screenDrawing, hoverAnimation.get(), x, y, width, height, mouseX, mouseY)
        }
    }

    fun Init.SettingText(p: Props<Text>) {
        Text(p + {
            paddingLeft = 8
        })
    }
}