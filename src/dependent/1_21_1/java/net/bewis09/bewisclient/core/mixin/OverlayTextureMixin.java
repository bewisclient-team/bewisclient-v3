package net.bewis09.bewisclient.core.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import net.bewis09.bewisclient.impl.settings.functionalities.EntityHighlightSettings;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.FastColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OverlayTexture.class)
public class OverlayTextureMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/NativeImage;setPixelRGBA(III)V"))
    private void bewisclient$init(NativeImage instance, int j, int i, int color) {
        if (!EntityHighlightSettings.INSTANCE.isEnabled() || i >= 8) {
            instance.setPixelRGBA(j, i, color);
            return;
        }

        int c = EntityHighlightSettings.INSTANCE.getColor().get().getColorInt();

        instance.setPixelRGBA(j, i, FastColor.ABGR32.color(
                (int)(( 1 - EntityHighlightSettings.INSTANCE.getAlpha().get()) * 255),
                FastColor.ARGB32.blue(c),
                FastColor.ARGB32.green(c),
                FastColor.ARGB32.red(c)
        ));
    }
}
