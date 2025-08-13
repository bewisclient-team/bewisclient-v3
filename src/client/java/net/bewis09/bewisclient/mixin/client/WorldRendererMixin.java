package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.logic.Bewisclient;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Redirect(method = "drawBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexRendering;drawOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/shape/VoxelShape;DDDI)V"))
    private void drawOutline(
            net.minecraft.client.util.math.MatrixStack matrixStack,
            net.minecraft.client.render.VertexConsumer vertexConsumer,
            net.minecraft.util.shape.VoxelShape voxelShape,
            double x, double y, double z, int c) {
        var color = c;

        if (Bewisclient.INSTANCE.getSettings().getBlockHighlight().getEnabled().get()) {
            color = (Bewisclient.INSTANCE.getSettings().getBlockHighlight().getColor().get().getColor() & 0x00FFFFFF) | ((int) (Bewisclient.INSTANCE.getSettings().getBlockHighlight().getThickness().get() * 255f) << 24);
        }

        VertexRendering.drawOutline(matrixStack, vertexConsumer, voxelShape, x, y, z, color);
    }
}
