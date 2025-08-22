package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.functionalities.held_item_info.HeldItemTooltip;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "appendComponentTooltip", at = @At("HEAD"))
    private <T extends TooltipAppender> void bewisclient$appendComponentTooltip(ComponentType<T> componentType, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type, CallbackInfo ci) {
        if (HeldItemTooltip.INSTANCE.isLookup()) {
            HeldItemTooltip.INSTANCE.getComponentSet().add(componentType);
        }
    }

    @Redirect(method = "appendAttributeModifiersTooltip", at = @At(value = "NEW", target = "Lorg/apache/commons/lang3/mutable/MutableBoolean;"))
    private MutableBoolean bewisclient$appendAttributeModifiersTooltip(boolean value) {
        return HeldItemTooltip.INSTANCE.isRendering() ? new MutableBoolean(false) : new MutableBoolean(value);
    }
}