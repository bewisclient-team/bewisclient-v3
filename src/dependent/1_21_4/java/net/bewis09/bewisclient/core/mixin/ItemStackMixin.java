package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.impl.functionalities.HeldItemTooltip;
import net.bewis09.bewisclient.impl.widget.InventoryWidget;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract @Nullable Component getCustomName();

    @Inject(method = "addToTooltip", at = @At("HEAD"))
    private <T extends TooltipProvider> void bewisclient$appendComponentTooltip(DataComponentType<T> dataComponentType, Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (HeldItemTooltip.INSTANCE.isLookup()) {
            HeldItemTooltip.INSTANCE.getComponentSet().add(dataComponentType);
        }
    }

    @Redirect(method = "method_57370", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    private static <T> void bewisclient$appendAttributeModifiersTooltip(Consumer<T> instance, T o) {
        if (!HeldItemTooltip.INSTANCE.isRendering()) instance.accept(o);
    }

    @Inject(method = "isEnchanted", at = @At("HEAD"), cancellable = true)
    private void bewisclient$hasEnchantments(CallbackInfoReturnable<Boolean> cir) {
        if (this.getCustomName() == InventoryWidget.INSTANCE.getIndicatorText()) cir.setReturnValue(true);
    }
}