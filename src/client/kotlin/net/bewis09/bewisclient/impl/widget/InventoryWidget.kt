package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.widget.ArmorWidget.componentsLoaded
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.util.then
import net.bewis09.bewisclient.version.Identifier
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.ScalableWidget
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

object InventoryWidget : ScalableWidget(createIdentifier("bewisclient", "inventory_widget")) {
    val indicatorText: Component = Component.literal("The way I would have to add Enchantments to the ItemStack is too complicated, so I am doing it via a mixin instead.")

    val exampleMap by lazy {
        mapOf(
            0 to Items.ITEM_FRAME.defaultInstance.also { it.count = 38 },
            1 to Items.MOJANG_BANNER_PATTERN.defaultInstance,
            2 to ItemStack.EMPTY,
            3 to Items.WOODEN_SWORD.defaultInstance.also { it.set(DataComponents.CUSTOM_NAME, indicatorText) },
            4 to ItemStack.EMPTY,
            5 to ItemStack.EMPTY,
            6 to Items.COOKED_BEEF.defaultInstance.also { it.count = 16 },
            7 to Items.ENDER_PEARL.defaultInstance.also { it.count = 4 },
            8 to Items.TORCH.defaultInstance.also { it.count = 63 },
            9 to Items.WOODEN_PICKAXE.defaultInstance.also { it.damageValue = it.maxDamage - 1 },
            10 to ItemStack.EMPTY,
            11 to Items.OAK_LOG.defaultInstance.also { it.count = 42 },
            12 to ItemStack.EMPTY,
            13 to ItemStack.EMPTY,
            14 to Items.COBBLESTONE.defaultInstance.also { it.applyComponents(DataComponentMap.builder().set(DataComponents.MAX_STACK_SIZE, 65).build()); it.count = 65 },
            15 to Items.DIAMOND.defaultInstance.also { it.count = 7 },
            16 to Items.IRON_INGOT.defaultInstance.also { it.count = 23 },
            17 to Items.GOLD_INGOT.defaultInstance.also { it.count = 12 },
            18 to Items.SHIELD.defaultInstance,
            19 to ItemStack.EMPTY,
            20 to Items.DIAMOND_BLOCK.defaultInstance.also { it.count = 64 },
            21 to ItemStack.EMPTY,
            22 to Items.OAK_PLANKS.defaultInstance.also { it.count = 32 },
            23 to ItemStack.EMPTY,
            24 to ItemStack.EMPTY,
            25 to Items.BREAD.defaultInstance.also { it.count = 16 },
            26 to Items.CRAFTING_TABLE.defaultInstance,
        )
    }

    val identifier: Identifier = createIdentifier("bewisclient", "textures/gui/widget/inventory_widget.png")

    override fun defaultPosition(): WidgetPosition = SidedPosition(5, 5, SidedPosition.END, SidedPosition.END)

    override fun render(screenDrawing: ScreenDrawing) {
        screenDrawing.drawTexture(identifier, 0, 0, getWidth(), getHeight())

        for (y in 0 until 3) {
            for (x in 0 until 9) {
                val itemStack: ItemStack = client.player?.inventory?.getItem(x + y * 9 + 9)?.apply { componentsLoaded = true } ?: (util.isInWorld() then { ItemStack.EMPTY }) ?: (if(componentsLoaded) getSampleStack(x, y) else ItemStack.EMPTY)
                drawSlot(screenDrawing, x * 20 + 2, y * 20 + 2, itemStack)
            }
        }
    }

    fun drawSlot(screenDrawing: ScreenDrawing, x: Int, y: Int, itemStack: ItemStack) {
        screenDrawing.drawItemStackWithOverlay(itemStack, x, y)
    }

    override fun getWidth(): Int = 180

    override fun getHeight(): Int = 60

    override val title = "Inventory Widget"
    override val description = "Displays your inventory on the screen."

    override fun isEnabledByDefault(): Boolean = false

    fun getSampleStack(x: Int, y: Int): ItemStack {
        return exampleMap[x + y * 9] ?: ItemStack.EMPTY
    }
}