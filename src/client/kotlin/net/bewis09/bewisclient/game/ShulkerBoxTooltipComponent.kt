package net.bewis09.bewisclient.game

import net.bewis09.bewisclient.core.IndependentTooltipComponent
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.createIdentifier
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipData

class ShulkerBoxTooltipComponent(val data: Data) : IndependentTooltipComponent {
    override fun getHeightDef(): Int {
        return 77
    }

    override fun getWidthDef(): Int {
        return 180
    }

    override fun drawItems(x: Int, y: Int, screenDrawing: ScreenDrawing) {
        screenDrawing.fill(x + 1, y + 1, getWidthDef() - 2, getHeightDef() - 7, data.color * 0xC3C3C3)
        screenDrawing.fill(x + 4, y + 4, getWidthDef() - 8, getHeightDef() - 13, data.color)
        var i = 0
        for (k in 0..2) {
            for (l in 0..8) {
                val n = x + l * 18 + 9
                val o = y + k * 18 + 9
                this.drawSlot(n, o, i, screenDrawing)
                i++
            }
        }
    }

    private fun drawSlot(x: Int, y: Int, index: Int, screenDrawing: ScreenDrawing) {
        val itemStack: ItemStack = data.container.getOrNull(index) ?: ItemStack.EMPTY
        screenDrawing.drawTexture(createIdentifier("bewisclient", "textures/gui/shulker_box/slot.png"), x, y, 0f, 0f, 18, 18, 18, 18, data.color)
        screenDrawing.drawItemStackWithOverlay(itemStack, x + 1, y + 1)
    }

    class Data(val color: Color, val container: Array<ItemStack>) : TooltipData

    companion object {
        fun of(color: Int?, array: Array<ItemStack>): Data? {
            return Data(Color(color ?: 0x956896, 1f), array.also { if(it.all { a -> a.isEmpty }) return null } )
        }
    }

    object Entrypoint : EventEntrypoint {
        override fun onInitializeClient() {
            TooltipComponentCallback.EVENT.register { (it as? Data)?.let { a -> ShulkerBoxTooltipComponent(a) } }
        }
    }
}