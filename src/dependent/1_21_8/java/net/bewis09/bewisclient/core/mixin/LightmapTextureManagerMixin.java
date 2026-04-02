package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.impl.settings.functionalities.FullbrightSettings;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightTexture.class)
abstract class LightmapTextureManagerMixin {
    @Redirect(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/dimension/DimensionType;ambientLight()F"))
    private float redirectAmbientLight(DimensionType instance) {
        if (!FullbrightSettings.INSTANCE.getNightVision().get() || !FullbrightSettings.INSTANCE.isEnabled()) {
            return instance.ambientLight();
        }
        return 1.0f;
    }

    @Redirect(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
    private float invokeGamma(Double instance) {
        if (!FullbrightSettings.INSTANCE.isEnabled()) {
            return instance.floatValue();
        }
        return FullbrightSettings.INSTANCE.getBrightness().get();
    }
}