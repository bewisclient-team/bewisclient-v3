package net.bewis09.bewisclient.core.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.bewis09.bewisclient.impl.settings.functionalities.BlockHighlightSettings;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public class WorldRendererMixin {
    @Redirect(method = "renderHitOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ShapeRenderer;renderShape(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/phys/shapes/VoxelShape;DDDI)V"))
    private void drawOutline(PoseStack matrixStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double x, double y, double z, int c) {
        var color = c;

        if (BlockHighlightSettings.INSTANCE.isEnabled()) {
            color = (BlockHighlightSettings.INSTANCE.getColor().get().getColorInt() & 0x00FFFFFF) | ((int) (BlockHighlightSettings.INSTANCE.getThickness().get() * 255f) << 24);
        }

        ShapeRenderer.renderShape(matrixStack, vertexConsumer, voxelShape, x, y, z, color);
    }
}
