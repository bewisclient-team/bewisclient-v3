package net.bewis09.bewisclient.core.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import net.bewis09.bewisclient.impl.settings.functionalities.EntityHighlightSettings;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OverlayTexture.class)
public class OverlayTextureMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/NativeImage;setPixel(III)V"))
    private void bewisclient$init(NativeImage instance, int j, int i, int color) {
        if (!EntityHighlightSettings.INSTANCE.isEnabled() || i >= 8) {
            instance.setPixel(j, i, color);
            return;
        }

        instance.setPixel(j, i, ARGB.color(((int) ((1 - EntityHighlightSettings.INSTANCE.getAlpha().get()) * 255)) << 24, EntityHighlightSettings.INSTANCE.getColor().get().getColorInt()));
    }
}
