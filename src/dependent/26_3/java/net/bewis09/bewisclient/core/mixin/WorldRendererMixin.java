package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.features.utilities.BlockHighlight;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public class WorldRendererMixin {
    @Redirect(method = "submitBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;black(I)I", ordinal = 0))
    private static int drawOutline(int alpha) {
        if (!BlockHighlight.INSTANCE.getEnabled()) return ARGB.black(alpha);

        return (BlockHighlight.INSTANCE.getColor().getColorInt() & 0x00FFFFFF) | ((int) (BlockHighlight.INSTANCE.getThickness() * 255f) << 24);
    }
}
