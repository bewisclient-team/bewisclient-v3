package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.ScalableWidget
import net.minecraft.component.ComponentMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object InventoryWidget : ScalableWidget() {
    val indicatorText: Text = Text.of("The way I would have to add Enchantments to the ItemStack is too complicated, so I am doing it via a mixin instead.")

    val exampleMap by lazy {
        mapOf(
            0 to Items.ITEM_FRAME.defaultStack.also { it.count = 38 },
            1 to Items.MOJANG_BANNER_PATTERN.defaultStack,
            2 to ItemStack.EMPTY,
            3 to Items.COPPER_SWORD.defaultStack.also { it.set(DataComponentTypes.CUSTOM_NAME, indicatorText) },
            4 to ItemStack.EMPTY,
            5 to ItemStack.EMPTY,
            6 to Items.COOKED_BEEF.defaultStack.also { it.count = 16 },
            7 to Items.ENDER_PEARL.defaultStack.also { it.count = 4 },
            8 to Items.TORCH.defaultStack.also { it.count = 63 },
            9 to Items.WOODEN_PICKAXE.defaultStack.also { it.damage = it.maxDamage - 1 },
            10 to ItemStack.EMPTY,
            11 to Items.OAK_LOG.defaultStack.also { it.count = 42 },
            12 to ItemStack.EMPTY,
            13 to ItemStack.EMPTY,
            14 to Items.COBBLESTONE.defaultStack.also { it.applyComponentsFrom(ComponentMap.builder().add(DataComponentTypes.MAX_STACK_SIZE, 65).build()); it.count = 65 },
            15 to Items.DIAMOND.defaultStack.also { it.count = 7 },
            16 to Items.IRON_INGOT.defaultStack.also { it.count = 23 },
            17 to Items.GOLD_INGOT.defaultStack.also { it.count = 12 },
            18 to Items.SHIELD.defaultStack,
            19 to ItemStack.EMPTY,
            20 to Items.DIAMOND_BLOCK.defaultStack.also { it.count = 64 },
            21 to ItemStack.EMPTY,
            22 to Items.OAK_PLANKS.defaultStack.also { it.count = 32 },
            23 to ItemStack.EMPTY,
            24 to ItemStack.EMPTY,
            25 to Items.BREAD.defaultStack.also { it.count = 16 },
            26 to Items.CRAFTING_TABLE.defaultStack,
        )
    }

    val inventoryWidgetTranslation = Translation("widget.inventory_widget.name", "Inventory Widget")
    val inventoryWidgetDescription = Translation("widget.inventory_widget.description", "Displays your inventory on the screen.")

    val identifier: Identifier = Identifier.of("bewisclient", "textures/gui/widget/inventory_widget.png")

    override fun defaultPosition(): WidgetPosition = SidedPosition(5, 5, SidedPosition.TransformerType.START, SidedPosition.TransformerType.START)

    override fun getId(): Identifier = Identifier.of("bewisclient", "inventory_widget")

    override fun render(screenDrawing: ScreenDrawing) {
        screenDrawing.drawTexture(identifier, 0, 0, getWidth(), getHeight())

        for (y in 0 until 3) {
            for (x in 0 until 9) {
                val itemStack: ItemStack = client.player?.inventory?.getStack(x + y * 9 + 9) ?: client.world?.let { ItemStack.EMPTY } ?: getSampleStack(x, y)
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

    fun getSampleStack(x: Int, y: Int): ItemStack {
        return exampleMap[x + y * 9] ?: ItemStack.EMPTY
    }
}