package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.settings.functionalities.EntityHighlightSettings;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OverlayTexture.class)
public class OverlayTextureMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImage;setColorArgb(III)V"))
    private void bewisclient$init(NativeImage instance, int j, int i, int color) {
        if (!EntityHighlightSettings.INSTANCE.getEnabled().get() || i >= 8) {
            instance.setColorArgb(j, i, color);
            return;
        }

        instance.setColorArgb(j, i, ColorHelper.withAlpha(1 - EntityHighlightSettings.INSTANCE.getAlpha().get(), EntityHighlightSettings.INSTANCE.getColor().get().getColor().getRGB()));
    }
}
