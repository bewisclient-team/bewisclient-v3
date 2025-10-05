package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.impl.settings.functionalities.EntityHighlightSettings;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OverlayTexture.class)
public class OverlayTextureMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImage;setColor(III)V"))
    private void bewisclient$init(NativeImage instance, int j, int i, int color) {
        if (!EntityHighlightSettings.INSTANCE.getEnabled().get() || i >= 8) {
            instance.setColor(j, i, color);
            return;
        }

        int c = EntityHighlightSettings.INSTANCE.getColor().get().getColorInt();

        instance.setColor(j, i, ColorHelper.Abgr.getAbgr(
                (int)(( 1 - EntityHighlightSettings.INSTANCE.getAlpha().get()) * 255),
                ColorHelper.Argb.getBlue(c),
                ColorHelper.Argb.getGreen(c),
                ColorHelper.Argb.getRed(c)
        ));
    }
}
