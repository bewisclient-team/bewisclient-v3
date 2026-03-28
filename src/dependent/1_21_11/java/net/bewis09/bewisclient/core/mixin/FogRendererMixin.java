package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.core.features.BetterVisibility;
import net.bewis09.bewisclient.impl.settings.functionalities.BetterVisibilitySettings;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;setupFog(Lnet/minecraft/client/renderer/fog/FogData;Lnet/minecraft/client/Camera;Lnet/minecraft/client/multiplayer/ClientLevel;FLnet/minecraft/client/DeltaTracker;)V"))
    private static void bewisclient$applyFog(FogEnvironment instance, FogData fogData, Camera camera, ClientLevel clientWorld, float v, DeltaTracker renderTickCounter) {
        if (BetterVisibilitySettings.INSTANCE.isEnabled())
            BetterVisibility.INSTANCE.applyFogModifier(instance, fogData, camera, clientWorld, v, renderTickCounter);
        else
            instance.setupFog(fogData, camera, clientWorld, v, renderTickCounter);
    }
}
