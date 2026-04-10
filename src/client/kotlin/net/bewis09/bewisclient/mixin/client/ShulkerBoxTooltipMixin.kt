// @VersionReplacement

package net.bewis09.bewisclient.mixin.client

import net.bewis09.bewisclient.game.ShulkerBoxTooltipComponent
import net.bewis09.bewisclient.impl.settings.functionalities.ShulkerBoxTooltipSettings
import net.minecraft.core.component.DataComponents
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents
import net.minecraft.world.level.block.ShulkerBoxBlock
import org.spongepowered.asm.mixin.Mixin
import java.util.*

@Mixin(BlockItem::class)
class ShulkerBoxTooltipMixin(settings: Properties) : Item(settings) {
    override fun getTooltipImage(stack: ItemStack): Optional<TooltipComponent> {
        val blockItem: BlockItem = stack.item as? BlockItem ?: return super.getTooltipImage(stack)
        val block: ShulkerBoxBlock = blockItem.block as? ShulkerBoxBlock ?: return super.getTooltipImage(stack)
        if (!ShulkerBoxTooltipSettings.isEnabled()) return super.getTooltipImage(stack)

        val component: ItemContainerContents = stack.get<ItemContainerContents>(DataComponents.CONTAINER) ?: return super.getTooltipImage(stack)

        // @[1.21.11] stream @[] allItemsCopyStream
        val array = component./*[@]*/allItemsCopyStream/*[!@]*/().toArray { arrayOfNulls<ItemStack>(it) }.mapNotNull { it }.toTypedArray()

        val color = block.color ?: return Optional.ofNullable<TooltipComponent?>(ShulkerBoxTooltipComponent.of(null, array))

        return Optional.ofNullable<TooltipComponent?>(ShulkerBoxTooltipComponent.of(color.textureDiffuseColor, array))
    }
}