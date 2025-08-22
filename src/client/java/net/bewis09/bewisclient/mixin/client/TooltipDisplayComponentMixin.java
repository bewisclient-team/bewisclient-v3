package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.functionalities.held_item_info.HeldItemTooltip;
import net.bewis09.bewisclient.impl.settings.functionalities.HeldItemTooltipSettings;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(TooltipDisplayComponent.class)
public class TooltipDisplayComponentMixin {
    @Inject(method = "shouldDisplay", at = @At("RETURN"), cancellable = true)
    public void bewisclient$shouldDisplay(ComponentType<?> componentType, CallbackInfoReturnable<Boolean> cir) {
        if (HeldItemTooltip.INSTANCE.isLookup()) {
            HeldItemTooltip.INSTANCE.getComponentSet().add(componentType);
            return;
        }

        if (!HeldItemTooltip.INSTANCE.isRendering() || !cir.getReturnValue()) return;

        var id = Registries.DATA_COMPONENT_TYPE.getEntry(componentType).getIdAsString();
        cir.setReturnValue(HeldItemTooltipSettings.INSTANCE.getShowMap().get(id, Arrays.stream(HeldItemTooltip.INSTANCE.getDefaultOff()).noneMatch(a -> a == componentType)));
    }
}
