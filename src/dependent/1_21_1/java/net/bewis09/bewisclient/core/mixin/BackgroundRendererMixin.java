package net.bewis09.bewisclient.core.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bewis09.bewisclient.impl.settings.functionalities.BetterVisibilitySettings;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FogShape;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
    @Shadow
    @Nullable
    private static BackgroundRenderer.StatusEffectFogModifier getFogModifier(Entity entity, float tickDelta) {
        return null;
    }

    @Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
    private static void bewisclient$applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        if(!BetterVisibilitySettings.INSTANCE.getEnabled().get()) return;

        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        Entity entity = camera.getFocusedEntity();
        BackgroundRenderer.StatusEffectFogModifier statusEffectFogModifier = getFogModifier(entity, tickDelta);

        if (cameraSubmersionType == CameraSubmersionType.LAVA) {
            if (entity.isSpectator() || !BetterVisibilitySettings.INSTANCE.getLava().get()) return;
            RenderSystem.setShaderFogStart(-8f);
            RenderSystem.setShaderFogEnd(16f);
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
        } else if (cameraSubmersionType == CameraSubmersionType.POWDER_SNOW) {
            if (!BetterVisibilitySettings.INSTANCE.getPowder_snow().get()) return;
            RenderSystem.setShaderFogStart(-8f);
            RenderSystem.setShaderFogEnd(8f);
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
        } else if (statusEffectFogModifier != null) {
            return;
        } else if (cameraSubmersionType == CameraSubmersionType.WATER) {
            if (!BetterVisibilitySettings.INSTANCE.getWater().get()) return;
            RenderSystem.setShaderFogStart(-8f);
            RenderSystem.setShaderFogEnd(viewDistance);
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
        } else if (thickFog) {
            if (!BetterVisibilitySettings.INSTANCE.getNether().get()) return;
            RenderSystem.setShaderFogStart(viewDistance * 2 - MathHelper.clamp(viewDistance / 10.0f, 4.0f, 64.0f));
            RenderSystem.setShaderFogEnd(viewDistance * 2);
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
        } else {
            return;
        }

        ci.cancel();
    }
}
