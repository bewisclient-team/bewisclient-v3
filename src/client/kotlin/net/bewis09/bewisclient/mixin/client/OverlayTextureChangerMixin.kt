package net.bewis09.bewisclient.mixin.client

import net.bewis09.bewisclient.features.utilities.EntityHighlight
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
    var alpha: Float = EntityHighlight.alpha

    @Unique
    var color: Int = EntityHighlight.color.getColorInt()

    @Unique
    var enabled: Boolean = EntityHighlight.enabled

    @Unique
    var overlayTexture: OverlayTexture = OverlayTexture()

    @Inject(method = ["overlayTexture"], at = [At("HEAD")], cancellable = true)
    fun getOverlayTexture(cir: CallbackInfoReturnable<OverlayTexture?>) {
        if (enabled != EntityHighlight.enabled || enabled && (alpha != EntityHighlight.alpha || color != (EntityHighlight.color.getColorInt()))) {
            alpha = EntityHighlight.alpha
            color = EntityHighlight.color.getColorInt()
            enabled = EntityHighlight.enabled

            overlayTexture = OverlayTexture()
        }

        cir.returnValue = overlayTexture
    }
}