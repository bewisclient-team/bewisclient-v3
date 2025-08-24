package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.ScalableWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

object InventoryWidget: ScalableWidget() {
    val inventoryWidgetTranslation = Translation("widget.inventory_widget.name", "Inventory Widget")
    val inventoryWidgetDescription = Translation("widget.inventory_widget.description", "Displays your inventory on the screen.")

    val identifier: Identifier = Identifier.of("bewisclient", "textures/gui/widget/inventory_widget.png")

    override fun defaultPosition(): WidgetPosition = SidedPosition(5,5, SidedPosition.TransformerType.START, SidedPosition.TransformerType.START)

    override fun getId(): Identifier = Identifier.of("bewisclient", "inventory_widget")

    override fun render(screenDrawing: ScreenDrawing) {
        screenDrawing.drawTexture(identifier, 0, 0, getWidth(), getHeight())

        for (y in 0 until 3) {
            for (x in 0 until 9) {
                val itemStack: ItemStack = MinecraftClient.getInstance().player?.inventory?.getStack(x + y * 9 + 9) ?: ItemStack.EMPTY
                drawSlot(screenDrawing, x * 20 + 2, y * 20 + 2, itemStack)
            }
        }
    }

    fun drawSlot(screenDrawing: ScreenDrawing, x: Int, y: Int, itemStack: ItemStack) {
        screenDrawing.drawItemStackWithOverlay(itemStack, x, y)
    }

    override fun getWidth(): Int = 180

    override fun getHeight(): Int = 60

    override fun getTranslation(): Translation = inventoryWidgetTranslation

    override fun getDescription(): Translation = inventoryWidgetDescription

    override fun isEnabledByDefault(): Boolean = false
}