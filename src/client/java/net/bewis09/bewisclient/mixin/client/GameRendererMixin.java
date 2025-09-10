package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.functionalities.Zoom;
import net.bewis09.bewisclient.impl.settings.functionalities.EntityHighlightSettings;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Unique
    float alpha = EntityHighlightSettings.INSTANCE.getAlpha().get();
    @Unique
    int color = EntityHighlightSettings.INSTANCE.getColor().get().getColorInt();
    @Unique
    boolean enabled = EntityHighlightSettings.INSTANCE.getEnabled().get();

    @Unique
    OverlayTexture overlayTexture = new OverlayTexture();

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    public void inject(Camera camera, float tickProgress, boolean changingFov, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(cir.getReturnValue() * Zoom.INSTANCE.getFactor());
    }

    @Inject(method = "getOverlayTexture", at = @At("HEAD"), cancellable = true)
    public void getOverlayTexture(CallbackInfoReturnable<OverlayTexture> cir) {
        if (EntityHighlightSettings.INSTANCE.getEnabled().get()) {
            if (enabled != EntityHighlightSettings.INSTANCE.getEnabled().get() || alpha != EntityHighlightSettings.INSTANCE.getAlpha().get() || color != (EntityHighlightSettings.INSTANCE.getColor().get().getColorInt())) {
                alpha = EntityHighlightSettings.INSTANCE.getAlpha().get();
                color = EntityHighlightSettings.INSTANCE.getColor().get().getColorInt();
                enabled = EntityHighlightSettings.INSTANCE.getEnabled().get();

                overlayTexture = new OverlayTexture();
            }

            cir.setReturnValue(overlayTexture);
        }
    }
}
