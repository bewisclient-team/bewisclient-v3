package net.bewis09.bewisclient.core

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.tooltip.TooltipComponent

interface IndependentTooltipComponent : TooltipComponent {
    fun getWidthDef(): Int
    fun getHeightDef(): Int

    override fun getWidth(textRenderer: TextRenderer?): Int {
        return getWidthDef()
    }

    override fun getHeight(textRenderer: TextRenderer?): Int {
        return getHeightDef()
    }

    override fun drawItems(textRenderer: TextRenderer, x: Int, y: Int, width: Int, height: Int, context: DrawContext) {
        drawItems(x, y, ScreenDrawing(context, textRenderer))
    }

    fun drawItems(x: Int, y: Int, screenDrawing: ScreenDrawing)
}