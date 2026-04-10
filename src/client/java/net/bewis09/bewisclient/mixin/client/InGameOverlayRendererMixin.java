// @VersionReplacement

package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.settings.functionalities.FireHeightSettings;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ScreenEffectRenderer.class)
public class InGameOverlayRendererMixin {
    // @[26.1] "renderFire" @[] "lambda$submitFire$0"
    @ModifyArg(method = /*[@]*/"lambda$submitFire$0"/*[!@]*/, at = @At(
            value = "INVOKE",
            // @[26.1] "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V" @[] "Lorg/joml/Matrix4f;translate(FFF)Lorg/joml/Matrix4f;"
            target = /*[@]*/"Lorg/joml/Matrix4f;translate(FFF)Lorg/joml/Matrix4f;"/*[!@]*/
    ), index = 1)
    private static float modifyTranslateY(float y) {
        if (!FireHeightSettings.INSTANCE.isEnabled()) return y;
        return y - (1 - FireHeightSettings.INSTANCE.getHeight().get()) * 0.4f;
    }
}