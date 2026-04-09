package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.impl.functionalities.Zoom;
import net.bewis09.bewisclient.impl.settings.functionalities.ZoomSettings;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class GameRendererMixin {
    @Inject(method = "modifyFovBasedOnDeathOrFluid", at = @At("RETURN"), cancellable = true)
    public void inject(CallbackInfoReturnable<Float> cir) {
        if (ZoomSettings.INSTANCE.isEnabled()) {
            cir.setReturnValue(cir.getReturnValue() * Zoom.INSTANCE.getFactor());
        }
    }
}
