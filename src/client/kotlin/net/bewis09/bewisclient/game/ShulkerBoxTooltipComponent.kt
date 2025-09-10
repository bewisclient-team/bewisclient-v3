package net.bewis09.bewisclient.game

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.bewis09.bewisclient.logic.color.Color
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback
import net.minecraft.block.ShulkerBoxBlock
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipData
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList

class ShulkerBoxTooltipComponent(val data: Data) : TooltipComponent {
    override fun getHeight(textRenderer: TextRenderer?): Int {
        return 77
    }

    override fun getWidth(textRenderer: TextRenderer?): Int {
        return 180
    }

    override fun drawItems(textRenderer: TextRenderer, x: Int, y: Int, width: Int, height: Int, context: DrawContext) {
        val screenDrawing = ScreenDrawing(context, textRenderer)
        screenDrawing.fill(x + 1, y + 1, getWidth(textRenderer) - 2, getHeight(textRenderer) - 7, data.color * 0xC3C3C3)
        screenDrawing.fill(x + 4, y + 4, getWidth(textRenderer) - 8, getHeight(textRenderer) - 13, data.color)
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
        if (index >= this.data.container.size) {
            return
        }
        val itemStack: ItemStack = data.container[index]
        screenDrawing.drawTexture(Identifier.of("bewisclient", "textures/gui/shulker_box/slot.png"), x, y, 0f, 0f, 18, 18, 18, 18, data.color)
        screenDrawing.drawItemStackWithOverlay(itemStack, x + 1, y + 1)
    }

    data class Data(val color: Color, val container: DefaultedList<ItemStack>) : TooltipData

    companion object {
        fun of(stack: ItemStack, block: ShulkerBoxBlock): Data? {
            return Data(Color(block.color?.entityColor ?: return null, 1f), DefaultedList.ofSize(27, ItemStack.EMPTY).also { (stack.get(DataComponentTypes.CONTAINER) ?: return null).copyTo(it); if (it.all(ItemStack::isEmpty) ) return null } )
        }
    }

    object Entrypoint : EventEntrypoint {
        override fun onInitializeClient() {
            TooltipComponentCallback.EVENT.register { (it as? Data)?.let { a -> ShulkerBoxTooltipComponent(a) } }
        }
    }
}