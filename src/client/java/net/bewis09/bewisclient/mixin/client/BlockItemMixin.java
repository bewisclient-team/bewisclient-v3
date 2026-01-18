package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.game.ShulkerBoxTooltipComponent;
import net.bewis09.bewisclient.impl.settings.functionalities.ShulkerBoxTooltipSettings;
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
        if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock block && ShulkerBoxTooltipSettings.INSTANCE.isEnabled()) {
            ContainerComponent component = stack.get(DataComponentTypes.CONTAINER);

            assert component != null;
            ItemStack[] array = component.stream().toArray(ItemStack[]::new);

            if (block.getColor() == null)
                return Optional.ofNullable(ShulkerBoxTooltipComponent.Companion.of(null, array));

            return Optional.ofNullable(ShulkerBoxTooltipComponent.Companion.of(block.getColor().getEntityColor(), array));
        }
        return super.getTooltipData(stack);
    }
}
