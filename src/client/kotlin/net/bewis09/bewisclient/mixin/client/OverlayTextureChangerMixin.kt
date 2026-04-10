package net.bewis09.bewisclient.mixin.client

import net.bewis09.bewisclient.impl.settings.functionalities.EntityHighlightSettings
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Unique
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(GameRenderer::class)
abstract class OverlayTextureChangerMixin {
    @Unique
    var alpha: Float = EntityHighlightSettings.alpha.get()

    @Unique
    var color: Int = EntityHighlightSettings.color.get().getColorInt()

    @Unique
    var enabled: Boolean = EntityHighlightSettings.isEnabled()

    @Unique
    var overlayTexture: OverlayTexture = OverlayTexture()

    @Inject(method = ["overlayTexture"], at = [At("HEAD")], cancellable = true)
    fun getOverlayTexture(cir: CallbackInfoReturnable<OverlayTexture?>) {
        if (enabled != EntityHighlightSettings.isEnabled() || enabled && (alpha != EntityHighlightSettings.alpha.get() || color != (EntityHighlightSettings.color.get().getColorInt()))) {
            alpha = EntityHighlightSettings.alpha.get()
            color = EntityHighlightSettings.color.get().getColorInt()
            enabled = EntityHighlightSettings.isEnabled()

            overlayTexture = OverlayTexture()
        }

        cir.returnValue = overlayTexture
    }
}