package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.impl.settings.functionalities.BlockHighlightSettings;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Redirect(method = "drawBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexRendering;drawOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/shape/VoxelShape;DDDI)V"))
    private void drawOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double x, double y, double z, int c) {
        var color = c;

        if (BlockHighlightSettings.INSTANCE.getEnabled().get()) {
            color = (BlockHighlightSettings.INSTANCE.getColor().get().getColorInt() & 0x00FFFFFF) | ((int) (BlockHighlightSettings.INSTANCE.getThickness().get() * 255f) << 24);
        }

        VertexRendering.drawOutline(matrixStack, vertexConsumer, voxelShape, x, y, z, color);
    }
}
