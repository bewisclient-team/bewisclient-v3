package net.bewis09.bewisclient.core.mixin;

import net.bewis09.bewisclient.impl.settings.functionalities.BlockHighlightSettings;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow
    private static void drawCuboidShapeOutline(MatrixStack matrices, VertexConsumer vertexConsumer, VoxelShape shape, double offsetX, double offsetY, double offsetZ, float red, float green, float blue, float alpha) {
    }

    @Redirect(method = "drawBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawCuboidShapeOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/shape/VoxelShape;DDDFFFF)V"))
    private void drawOutline(MatrixStack matrices, VertexConsumer vertexConsumer, VoxelShape shape, double offsetX, double offsetY, double offsetZ, float red, float green, float blue, float alpha) {
        if (BlockHighlightSettings.INSTANCE.getEnabled().get()) {
            alpha = BlockHighlightSettings.INSTANCE.getThickness().get();
            red = ((BlockHighlightSettings.INSTANCE.getColor().get().getColorInt() >> 16) & 255) / 255f;
            green = ((BlockHighlightSettings.INSTANCE.getColor().get().getColorInt() >> 8) & 255) / 255f;
            blue = (BlockHighlightSettings.INSTANCE.getColor().get().getColorInt() & 255) / 255f;
        }

        drawCuboidShapeOutline(matrices, vertexConsumer, shape, offsetX, offsetY, offsetZ, red, green, blue, alpha);
    }
}
