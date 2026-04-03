package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.game.ShulkerBoxTooltipComponent;
import net.bewis09.bewisclient.impl.settings.functionalities.ShulkerBoxTooltipSettings;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;

@Mixin(BlockItem.class)
public class BlockItemMixin extends Item {
    public BlockItemMixin(Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock block && ShulkerBoxTooltipSettings.INSTANCE.isEnabled()) {
            ItemContainerContents component = stack.get(DataComponents.CONTAINER);

            assert component != null;
            ItemStack[] array = component.stream().toArray(ItemStack[]::new);

            if (block.getColor() == null)
                return Optional.ofNullable(ShulkerBoxTooltipComponent.Companion.of(null, array));

            return Optional.ofNullable(ShulkerBoxTooltipComponent.Companion.of(block.getColor().getTextureDiffuseColor(), array));
        }
        return super.getTooltipImage(stack);
    }
}
