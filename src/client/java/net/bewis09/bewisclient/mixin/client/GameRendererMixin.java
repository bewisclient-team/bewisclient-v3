package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.settings.functionalities.EntityHighlightSettings;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Unique
    float alpha = EntityHighlightSettings.INSTANCE.getAlpha().get();
    @Unique
    int color = EntityHighlightSettings.INSTANCE.getColor().get().getColorInt();
    @Unique
    boolean enabled = EntityHighlightSettings.INSTANCE.isEnabled();

    @Unique
    OverlayTexture overlayTexture = new OverlayTexture();

    @Inject(method = "overlayTexture", at = @At("HEAD"), cancellable = true)
    public void getOverlayTexture(CallbackInfoReturnable<OverlayTexture> cir) {
        if (EntityHighlightSettings.INSTANCE.isEnabled()) {
            if (enabled != EntityHighlightSettings.INSTANCE.isEnabled() || alpha != EntityHighlightSettings.INSTANCE.getAlpha().get() || color != (EntityHighlightSettings.INSTANCE.getColor().get().getColorInt())) {
                alpha = EntityHighlightSettings.INSTANCE.getAlpha().get();
                color = EntityHighlightSettings.INSTANCE.getColor().get().getColorInt();
                enabled = EntityHighlightSettings.INSTANCE.isEnabled();

                overlayTexture = new OverlayTexture();
            }

            cir.setReturnValue(overlayTexture);
        }
    }
}
