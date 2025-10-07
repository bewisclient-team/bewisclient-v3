package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.impl.settings.functionalities.PumpkinOverlaySettings;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EquippableComponent.class)
public class EquippableComponentMixin {
    @Inject(method = "cameraOverlay", at = @At("RETURN"), cancellable = true)
    private void bewisclient$cameraOverlay(CallbackInfoReturnable<Optional<Identifier>> cir) {
        if (!PumpkinOverlaySettings.INSTANCE.getEnabled().get()) return;

        if (cir.getReturnValue().isEmpty()) return;

        if (cir.getReturnValue().get().toString().equals("minecraft:misc/pumpkinblur")) cir.setReturnValue(Optional.empty());
    }
}
