// @VersionReplacement

package net.bewis09.bewisclient.game

import net.bewis09.bewisclient.version.TooltipComponentCallback
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.version.GuiGraphics
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ItemStack
import kotlin.collections.all

class ShulkerBoxTooltipComponent(val data: Data) : ClientTooltipComponent {
    // @[1.21.1] () @[] (textRenderer: Font)
    override fun getHeight/*[@]*/(textRenderer: Font)/*[!@]*/: Int {
        return getHeightDef()
    }

    override fun getWidth(textRenderer: Font): Int {
        return 180
    }

    fun getHeightDef(): Int = 77

    // @[1.21.1] renderImage(textRenderer: Font, x: Int, y: Int @[1.21.11] renderImage(textRenderer: Font, x: Int, y: Int, width: Int, height: Int @[] extractImage(textRenderer: Font, x: Int, y: Int, width: Int, height: Int
    override fun /*[@]*/extractImage(textRenderer: Font, x: Int, y: Int, width: Int, height: Int/*[!@]*/, context: GuiGraphics) {
        val screenDrawing = ScreenDrawing(context, textRenderer)

        screenDrawing.fill(x + 1, y + 1, getWidth(screenDrawing.textRenderer) - 2, getHeightDef() - 7, data.color * 0xC3C3C3)
        screenDrawing.fill(x + 4, y + 4, getWidth(screenDrawing.textRenderer) - 8, getHeightDef() - 13, data.color)
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

    class Data(val color: Color, val container: Array<ItemStack>) : TooltipComponent

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