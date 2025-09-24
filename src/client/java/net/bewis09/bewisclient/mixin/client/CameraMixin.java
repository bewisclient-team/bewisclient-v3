package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.functionalities.Perspective;
import net.bewis09.bewisclient.impl.settings.functionalities.PerspectiveSettings;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    private boolean thirdPerson;

    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"), index = 0)
    private float injectYaw(float yaw) {
        if (thirdPerson && PerspectiveSettings.INSTANCE.getEnabled().get())
            return yaw + Perspective.cameraAddYaw;
        return yaw;
    }

    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"), index = 1)
    private float injectPitch(float pitch) {
        if (thirdPerson && PerspectiveSettings.INSTANCE.getEnabled().get())
            return pitch + Perspective.cameraAddPitch;
        return pitch;
    }
}