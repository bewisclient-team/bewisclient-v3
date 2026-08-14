package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.features.utilities.BetterVisibility;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.BlindnessFogEnvironment;
import net.minecraft.client.renderer.fog.environment.DarknessFogEnvironment;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    @Shadow
    protected abstract FogType getFogType(Camera camera);

    @Inject(method = "setupFog", at = @At("RETURN"))
    private void bewisclient$applyFog(Camera camera, int renderDistanceInChunks, DeltaTracker deltaTracker, float darkenWorldAmount, ClientLevel level, CallbackInfoReturnable<FogData> cir) {
        if(!BetterVisibility.INSTANCE.getEnabledSetting().get()) return;

        FogType cameraSubmersionType = camera.getFluidInCamera();
        FogData fogData = cir.getReturnValue();
        float viewDistance = (float)(renderDistanceInChunks * 16);

        if (cameraSubmersionType == FogType.LAVA) {
            BetterVisibility.INSTANCE.applyFogModifier("lava", fogData, viewDistance);
            return;
        } else if (cameraSubmersionType == FogType.POWDER_SNOW) {
            BetterVisibility.INSTANCE.applyFogModifier("powder_snow", fogData, viewDistance);
            return;
        }

        //noinspection DataFlowIssue
        if (new BlindnessFogEnvironment().isApplicable(this.getFogType(camera), camera.entity()) || new DarknessFogEnvironment().isApplicable(this.getFogType(camera), camera.entity())) {
            return;
        }

        if (cameraSubmersionType == FogType.WATER) {
            BetterVisibility.INSTANCE.applyFogModifier("water", fogData, viewDistance);
        } else if (getFogType(camera) == FogType.ATMOSPHERIC) {
            BetterVisibility.INSTANCE.applyFogModifier("atmospheric", fogData, viewDistance);
        }
    }
}
