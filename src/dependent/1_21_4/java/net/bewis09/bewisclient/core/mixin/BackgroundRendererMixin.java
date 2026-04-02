package net.bewis09.bewisclient.core.mixin;

import com.mojang.blaze3d.shaders.FogShape;
import net.bewis09.bewisclient.impl.settings.functionalities.BetterVisibilitySettings;
import net.bewis09.bewisclient.util.MathHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public abstract class BackgroundRendererMixin {
    @Shadow
    @Nullable
    private static FogRenderer.MobEffectFogFunction getPriorityFogFunction(Entity entity, float f) {
        return null;
    }

    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    private static void bewisclient$applyFog(Camera camera, FogRenderer.FogMode fogMode, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta, CallbackInfoReturnable<FogParameters> cir) {
        if(!BetterVisibilitySettings.INSTANCE.getEnabled().get()) return;

        FogType cameraSubmersionType = camera.getFluidInCamera();
        Entity entity = camera.getEntity();
        FogRenderer.MobEffectFogFunction statusEffectFogModifier = getPriorityFogFunction(entity, tickDelta);

        FogRenderer.FogData fogData = new FogRenderer.FogData(fogMode);

        if (cameraSubmersionType == FogType.LAVA) {
            if (entity.isSpectator() || !BetterVisibilitySettings.INSTANCE.getLava().get()) return;
            fogData.start = -8f;
            fogData.end = 16f;
            fogData.shape = FogShape.SPHERE;
        } else if (cameraSubmersionType == FogType.POWDER_SNOW) {
            if (!BetterVisibilitySettings.INSTANCE.getPowder_snow().get()) return;
            fogData.start = -8f;
            fogData.end = 8f;
            fogData.shape = FogShape.SPHERE;
        } else if (statusEffectFogModifier != null) {
            return;
        } else if (cameraSubmersionType == FogType.WATER) {
            if (!BetterVisibilitySettings.INSTANCE.getWater().get()) return;
            fogData.start = -8f;
            fogData.end = viewDistance;
            fogData.shape = FogShape.SPHERE;
        } else if (thickenFog) {
            if (!BetterVisibilitySettings.INSTANCE.getNether().get()) return;
            fogData.start = viewDistance * 2 - MathHelper.INSTANCE.clamp(viewDistance / 10.0f, 4.0f, 64.0f);
            fogData.end = viewDistance * 2;
            fogData.shape = FogShape.SPHERE;
        } else {
            return;
        }

        cir.setReturnValue(new FogParameters(fogData.start, fogData.end, fogData.shape, color.x, color.y, color.z, color.w));
    }
}
