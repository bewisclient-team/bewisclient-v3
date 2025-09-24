package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.functionalities.Perspective;
import net.bewis09.bewisclient.impl.settings.functionalities.PerspectiveSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "changeLookDirection",at=@At("HEAD"),cancellable = true)
    public void inject(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if(!MinecraftClient.getInstance().options.getPerspective().isFirstPerson() && Perspective.EnablePerspective.INSTANCE.isPressed() && PerspectiveSettings.INSTANCE.getEnabled().get()) {
            Perspective.cameraAddPitch += (float) (cursorDeltaY * 0.15f);
            Perspective.cameraAddYaw += (float) (cursorDeltaX * 0.15f);
            ci.cancel();
        }
    }
}
