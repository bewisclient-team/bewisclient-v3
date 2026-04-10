// @VersionReplacement

package net.bewis09.bewisclient.mixin.client

import net.bewis09.bewisclient.impl.functionalities.Perspective
import net.minecraft.client.Camera
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.ModifyArg

@Mixin(Camera::class)
abstract class PerspectiveInjectorMixin {
    @Shadow
    private val detached = false

    // @[1.21.11] "setup" @[] "alignWithEntity"
    @ModifyArg(method = [ /*[@]*/"alignWithEntity"/*[!@]*/ ], at = At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V"), index = 0)
    private fun injectYaw(yaw: Float): Float {
        if (!detached) return yaw
        return yaw + Perspective.cameraAddYaw
    }

    // @[1.21.11] "setup" @[] "alignWithEntity"
    @ModifyArg(method = [ /*[@]*/"alignWithEntity"/*[!@]*/ ], at = At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V"), index = 1)
    private fun injectPitch(pitch: Float): Float {
        if (!detached) return pitch
        return pitch + Perspective.cameraAddPitch
    }
}