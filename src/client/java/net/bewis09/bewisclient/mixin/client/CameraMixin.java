package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.core.MixinMethods;
import net.bewis09.bewisclient.impl.functionalities.Perspective;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    private boolean detached;

    @ModifyArg(method = MixinMethods.CameraMixinSetup, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V"), index = 0)
    private float injectYaw(float yaw) {
        if (!detached) return yaw;
        return yaw + Perspective.cameraAddYaw;
    }

    @ModifyArg(method = MixinMethods.CameraMixinSetup, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V"), index = 1)
    private float injectPitch(float pitch) {
        if (!detached) return pitch;
        return pitch + Perspective.cameraAddPitch;
    }
}