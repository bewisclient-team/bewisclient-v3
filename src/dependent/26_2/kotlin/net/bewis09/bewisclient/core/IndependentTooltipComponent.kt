package net.bewis09.bewisclient.core

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent

interface IndependentTooltipComponent : ClientTooltipComponent {
    fun getWidthDef(): Int
    fun getHeightDef(): Int

    override fun getWidth(textRenderer: Font): Int {
        return getWidthDef()
    }

    override fun getHeight(textRenderer: Font): Int {
        return getHeightDef()
    }

    override fun extractImage(textRenderer: Font, x: Int, y: Int, width: Int, height: Int, context: GuiGraphics) {
        drawItems(x, y, ScreenDrawing(context, textRenderer))
    }

    fun drawItems(x: Int, y: Int, screenDrawing: ScreenDrawing)
}