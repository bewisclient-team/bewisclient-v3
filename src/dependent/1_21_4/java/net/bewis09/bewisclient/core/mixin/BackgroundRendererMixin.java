package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.impl.settings.functionalities.BetterVisibilitySettings;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FogShape;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
    @Shadow
    @Nullable
    private static BackgroundRenderer.StatusEffectFogModifier getFogModifier(Entity entity, float tickDelta) {
        return null;
    }

    @Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
    private static void bewisclient$applyFog(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta, CallbackInfoReturnable<Fog> cir) {
        if(!BetterVisibilitySettings.INSTANCE.getEnabled().get()) return;

        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        Entity entity = camera.getFocusedEntity();
        BackgroundRenderer.StatusEffectFogModifier statusEffectFogModifier = getFogModifier(entity, tickDelta);

        BackgroundRenderer.FogData fogData = new BackgroundRenderer.FogData(fogType);

        if (cameraSubmersionType == CameraSubmersionType.LAVA) {
            if (entity.isSpectator() || !BetterVisibilitySettings.INSTANCE.getLava().get()) return;
            fogData.fogStart = -8f;
            fogData.fogEnd = 16f;
            fogData.fogShape = FogShape.SPHERE;
        } else if (cameraSubmersionType == CameraSubmersionType.POWDER_SNOW) {
            if (!BetterVisibilitySettings.INSTANCE.getPowder_snow().get()) return;
            fogData.fogStart = -8f;
            fogData.fogEnd = 8f;
            fogData.fogShape = FogShape.SPHERE;
        } else if (statusEffectFogModifier != null) {
            return;
        } else if (cameraSubmersionType == CameraSubmersionType.WATER) {
            if (!BetterVisibilitySettings.INSTANCE.getWater().get()) return;
            fogData.fogStart = -8f;
            fogData.fogEnd = viewDistance;
            fogData.fogShape = FogShape.SPHERE;
        } else if (thickenFog) {
            if (!BetterVisibilitySettings.INSTANCE.getNether().get()) return;
            fogData.fogStart = viewDistance * 2 - MathHelper.clamp(viewDistance / 10.0f, 4.0f, 64.0f);
            fogData.fogEnd = viewDistance * 2;
            fogData.fogShape = FogShape.SPHERE;
        } else {
            return;
        }

        cir.setReturnValue(new Fog(fogData.fogStart, fogData.fogEnd, fogData.fogShape, color.x, color.y, color.z, color.w));
    }
}
