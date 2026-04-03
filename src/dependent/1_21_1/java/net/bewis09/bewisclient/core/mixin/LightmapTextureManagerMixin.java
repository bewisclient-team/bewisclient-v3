package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.impl.settings.functionalities.FullbrightSettings;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightTexture.class)
abstract class LightmapTextureManagerMixin {
    @Redirect(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
    private float invokeGamma(Double instance) {
        if (!FullbrightSettings.INSTANCE.isEnabled()) {
            return instance.floatValue();
        }
        return FullbrightSettings.INSTANCE.getBrightness().get();
    }
}