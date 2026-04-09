package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.core.MixinMethods;
import net.bewis09.bewisclient.impl.settings.functionalities.FireHeightSettings;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ScreenEffectRenderer.class)
public class InGameOverlayRendererMixin {
    @ModifyArg(method = MixinMethods.ScreenEffectRendererMixinFire, at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;translate(FFF)Lorg/joml/Matrix4f;"), index = 1)
    private static float modifyTranslateY(float y) {
        if (!FireHeightSettings.INSTANCE.isEnabled()) return y;
        return y - (1 - FireHeightSettings.INSTANCE.getHeight().get()) * 0.4f;
    }
}
