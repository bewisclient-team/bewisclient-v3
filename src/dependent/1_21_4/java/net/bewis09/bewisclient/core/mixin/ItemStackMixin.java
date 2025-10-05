package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.impl.functionalities.HeldItemTooltip;
import net.bewis09.bewisclient.impl.settings.functionalities.HeldItemTooltipSettings;
import net.bewis09.bewisclient.impl.widget.InventoryWidget;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Text getName();

    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
    private <T extends TooltipAppender> void bewisclient$appendComponentTooltip(ComponentType<T> componentType, Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, CallbackInfo ci) {
        if (HeldItemTooltip.INSTANCE.isLookup()) {
            HeldItemTooltip.INSTANCE.getComponentSet().add(componentType);
        }

        if (!HeldItemTooltip.INSTANCE.isRendering()) return;

        var id = Registries.DATA_COMPONENT_TYPE.getEntry(componentType).getIdAsString();
        if (!HeldItemTooltipSettings.INSTANCE.getShowMap().get(id, Arrays.stream(HeldItemTooltip.INSTANCE.getDefaultOff()).noneMatch(a -> a == componentType))) {
            ci.cancel();
        }
    }

    @Inject(method = "appendAttributeModifiersTooltip", at = @At("HEAD"), cancellable = true)
    private void bewisclient$appendAttributeModifiersTooltip(Consumer<Text> textConsumer, PlayerEntity player, CallbackInfo ci) {
        if (HeldItemTooltip.INSTANCE.isLookup()) {
            HeldItemTooltip.INSTANCE.getComponentSet().add(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        }

        if (!HeldItemTooltip.INSTANCE.isRendering()) return;

        var id = Registries.DATA_COMPONENT_TYPE.getEntry(DataComponentTypes.ATTRIBUTE_MODIFIERS).getIdAsString();
        if (!HeldItemTooltipSettings.INSTANCE.getShowMap().get(id, Arrays.stream(HeldItemTooltip.INSTANCE.getDefaultOff()).noneMatch(a -> a == DataComponentTypes.ATTRIBUTE_MODIFIERS))) {
            ci.cancel();
        }
    }

    @Redirect(method = "method_57370", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    private static <T> void bewisclient$appendAttributeModifiersTooltip(Consumer<T> instance, T o) {
        if (!HeldItemTooltip.INSTANCE.isRendering()) instance.accept(o);
    }

    @Inject(method = "hasEnchantments", at = @At("HEAD"), cancellable = true)
    private void bewisclient$hasEnchantments(CallbackInfoReturnable<Boolean> cir) {
        if (this.getName() == InventoryWidget.INSTANCE.getIndicatorText()) cir.setReturnValue(true);
    }
}