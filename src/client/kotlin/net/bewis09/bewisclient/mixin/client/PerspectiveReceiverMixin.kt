package net.bewis09.bewisclient.mixin.client

import net.bewis09.bewisclient.impl.functionalities.Perspective
import net.bewis09.bewisclient.impl.settings.functionalities.PerspectiveSettings
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.Entity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Entity::class)
class PerspectiveReceiverMixin {
    @Inject(method = ["turn"], at = [At("HEAD")], cancellable = true)
    fun inject(cursorDeltaX: Double, cursorDeltaY: Double, ci: CallbackInfo) {
        if (!Minecraft.getInstance().options.cameraType.isFirstPerson && Perspective.EnablePerspective.isPressed() && PerspectiveSettings.isEnabled()) {
            Perspective.cameraAddPitch += (cursorDeltaY * 0.15f).toFloat()
            Perspective.cameraAddYaw += (cursorDeltaX * 0.15f).toFloat()
            ci.cancel()
        }
    }
}