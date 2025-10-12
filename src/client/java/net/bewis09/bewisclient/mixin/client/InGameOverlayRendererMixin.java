package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.settings.functionalities.FireHeightSettings;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    @ModifyArg(method = "renderFireOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"), index = 1)
    private static float modifyTranslateY(float y) {
        if (!FireHeightSettings.INSTANCE.getEnabled().get()) return y;
        return y - (1 - FireHeightSettings.INSTANCE.getHeight().get()) * 0.4f;
    }
}
