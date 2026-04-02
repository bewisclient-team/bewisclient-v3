package net.bewis09.bewisclient.core

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent

interface IndependentTooltipComponent : ClientTooltipComponent {
    fun getWidthDef(): Int
    fun getHeightDef(): Int

    override fun getWidth(textRenderer: Font): Int {
        return getWidthDef()
    }

    override fun getHeight(): Int {
        return getHeightDef()
    }

    override fun renderImage(textRenderer: Font, x: Int, y: Int, context: GuiGraphics) {
        drawItems(x, y, ScreenDrawing(context, textRenderer))
    }

    fun drawItems(x: Int, y: Int, screenDrawing: ScreenDrawing)
}