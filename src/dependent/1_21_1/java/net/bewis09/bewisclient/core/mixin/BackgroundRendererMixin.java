package net.bewis09.bewisclient.core.mixin;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.bewis09.bewisclient.impl.settings.functionalities.BetterVisibilitySettings;
import net.bewis09.bewisclient.util.MathHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public abstract class BackgroundRendererMixin {
    @Shadow
    @Nullable
    private static FogRenderer.MobEffectFogFunction getPriorityFogFunction(Entity entity, float f) {
        return null;
    }

    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    private static void bewisclient$applyFog(Camera camera, FogRenderer.FogMode fogMode, float viewDistance, boolean thickenFog, float tickDelta, CallbackInfo cir) {
        if(!BetterVisibilitySettings.INSTANCE.getEnabled().get()) return;

        FogType cameraSubmersionType = camera.getFluidInCamera();
        Entity entity = camera.getEntity();
        FogRenderer.MobEffectFogFunction statusEffectFogModifier = getPriorityFogFunction(entity, tickDelta);

        if (cameraSubmersionType == FogType.LAVA) {
            if (entity.isSpectator() || !BetterVisibilitySettings.INSTANCE.getLava().get()) return;
            RenderSystem.setShaderFogStart(-8f);
            RenderSystem.setShaderFogEnd(16f);
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
        } else if (cameraSubmersionType == FogType.POWDER_SNOW) {
            if (!BetterVisibilitySettings.INSTANCE.getPowder_snow().get()) return;
            RenderSystem.setShaderFogStart(-8f);
            RenderSystem.setShaderFogEnd(8f);
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
        } else if (statusEffectFogModifier != null) {
            return;
        } else if (cameraSubmersionType == FogType.WATER) {
            if (!BetterVisibilitySettings.INSTANCE.getWater().get()) return;
            RenderSystem.setShaderFogStart(-8f);
            RenderSystem.setShaderFogEnd(viewDistance);
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
        } else if (thickenFog) {
            if (!BetterVisibilitySettings.INSTANCE.getNether().get()) return;
            RenderSystem.setShaderFogStart(viewDistance * 2 - MathHelper.INSTANCE.clamp(viewDistance / 10.0f, 4.0f, 64.0f));
            RenderSystem.setShaderFogEnd(viewDistance * 2);
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
        } else {
            return;
        }

        cir.cancel();
    }
}
