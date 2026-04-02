package net.bewis09.bewisclient.core.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.bewis09.bewisclient.impl.settings.functionalities.BlockHighlightSettings;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow
    private static void renderShape(PoseStack poseStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d, double e, double f, float g, float h, float i, float j) {
    }

    @Redirect(method = "renderHitOutline", at = @At(value = "INVOKE", target ="Lnet/minecraft/client/renderer/LevelRenderer;renderShape(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/phys/shapes/VoxelShape;DDDFFFF)V"))
    private void drawOutline(PoseStack poseStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double offsetX, double offsetY, double offsetZ, float red, float green, float blue, float alpha) {
        if (BlockHighlightSettings.INSTANCE.getEnabled().get()) {
            alpha = BlockHighlightSettings.INSTANCE.getThickness().get();
            red = ((BlockHighlightSettings.INSTANCE.getColor().get().getColorInt() >> 16) & 255) / 255f;
            green = ((BlockHighlightSettings.INSTANCE.getColor().get().getColorInt() >> 8) & 255) / 255f;
            blue = (BlockHighlightSettings.INSTANCE.getColor().get().getColorInt() & 255) / 255f;
        }

        renderShape(poseStack, vertexConsumer, voxelShape, offsetX, offsetY, offsetZ, red, green, blue, alpha);
    }
}
