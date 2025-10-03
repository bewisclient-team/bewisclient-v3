package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.core.Core;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;

@Mixin(BlockItem.class)
public class BlockItemMixin extends Item {
    public BlockItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock block) {
            ContainerComponent component = stack.get(DataComponentTypes.CONTAINER);

            if (Core.mixinFunctions == null || component == null) return super.getTooltipData(stack);

            ItemStack[] array = component.stream().toArray((i) -> new ItemStack[27]);

            if (block.getColor() == null)
                return Optional.ofNullable(Core.mixinFunctions.getShulkerBoxTooltipData(null, array));

            return Optional.ofNullable(Core.mixinFunctions.getShulkerBoxTooltipData(block.getColor().getEntityColor(), array));
        }
        return super.getTooltipData(stack);
    }
}
