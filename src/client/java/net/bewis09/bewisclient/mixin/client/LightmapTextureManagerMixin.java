package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.logic.Bewisclient;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightmapTextureManager.class)
class LightmapTextureManagerMixin {
    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
    private float invokeGamma(Double instance) {
        if (!Bewisclient.INSTANCE.getSettings().getFullbright().getEnabled().get()) {
            return instance.floatValue();
        }
        return Bewisclient.INSTANCE.getSettings().getFullbright().getBrightness().get();
    }
}