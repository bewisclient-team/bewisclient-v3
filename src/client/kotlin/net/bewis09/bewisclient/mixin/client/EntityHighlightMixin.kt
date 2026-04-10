// @VersionReplacement

package net.bewis09.bewisclient.mixin.client

import com.mojang.blaze3d.platform.NativeImage
import net.bewis09.bewisclient.impl.settings.functionalities.EntityHighlightSettings
import net.minecraft.client.renderer.texture.OverlayTexture
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect

@Mixin(OverlayTexture::class)
class EntityHighlightMixin {
    // @[1.21.1] "Lcom/mojang/blaze3d/platform/NativeImage;setPixelRGBA(III)V" @[] "Lcom/mojang/blaze3d/platform/NativeImage;setPixel(III)V"
    @Redirect(method = ["<init>"], at = At(value = "INVOKE", target = /*[@]*/"Lcom/mojang/blaze3d/platform/NativeImage;setPixel(III)V"/*[!@]*/))
    private fun bewisclientInit(instance: NativeImage, j: Int, i: Int, color: Int) {
        if (!EntityHighlightSettings.isEnabled() || i >= 8) {
            setPixel(instance, j, i, color)
            return
        }

        val color = EntityHighlightSettings.color.get().getColor()

        setPixel(
            instance, j, i, createColor(
                // @[1.21.1] color.blue, color.green, color.red @[] color.red, color.green, color.blue
                (255 - EntityHighlightSettings.alpha.get() * 255).toInt(), /*[@]*/color.red, color.green, color.blue/*[!@]*/
            )
        )
    }

    fun createColor(first: Int, second: Int, third: Int, fourth: Int): Int {
        return first shl 24 or (second shl 16) or (third shl 8) or fourth
    }

    // @[1.21.1] setPixelRGBA @[] setPixel
    fun setPixel(nativeImage: NativeImage, x: Int, y: Int, color: Int) = nativeImage./*[@]*/setPixel/*[!@]*/(x, y, color)
}