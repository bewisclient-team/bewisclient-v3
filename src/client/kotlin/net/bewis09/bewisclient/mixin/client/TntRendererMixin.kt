// @VersionReplacement

package net.bewis09.bewisclient.mixin.client

import net.bewis09.bewisclient.features.utilities.TntTimer
import net.minecraft.client.renderer.entity.EntityRenderDispatcher
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.world.entity.Entity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Suppress("unused")
@Mixin(EntityRenderer::class)
abstract class TntRendererMixin {
    @Shadow
    val entityRenderDispatcher: EntityRenderDispatcher? = null

    // @[1.21.1] @Shadow abstract fun renderNameTag(entity: Entity, nameTag: Component, poseStack: com.mojang.blaze3d.vertex.PoseStack, multiBufferSource: net.minecraft.client.renderer.MultiBufferSource, i: Int, f: Float) @[]
    /*[@]*//*[!@]*/

    @Inject(
        // @[1.21.1] "render" @[] "extractRenderState"
        method = [/*[@]*/"extractRenderState"/*[!@]*/],
        at = [At("RETURN")],
    )
    // @[1.21.1] entity: Entity, f: Float, g: Float, poseStack: com.mojang.blaze3d.vertex.PoseStack, multiBufferSource: net.minecraft.client.renderer.MultiBufferSource, i: Int, ci: CallbackInfo @[] entity: Entity, state: net.minecraft.client.renderer.entity.state.EntityRenderState, f: Float, ci: CallbackInfo
    fun onSubmit(/*[@]*/entity: Entity, state: net.minecraft.client.renderer.entity.state.EntityRenderState, f: Float, ci: CallbackInfo/*[!@]*/) {
        if (TntTimer.enabled && (this.entityRenderDispatcher?.distanceToSqr(entity) ?: 0.0) <= 144.0) {
            val nameTag = TntTimer.getNameTagForEntity(entity) ?: return
            // @[1.21.1] this.renderNameTag(entity, nameTag, poseStack, multiBufferSource, i, g) @[] state.nameTagAttachment = net.minecraft.world.phys.Vec3(0.0, 1.0, 0.0)
            /*[@]*/state.nameTagAttachment = net.minecraft.world.phys.Vec3(0.0, 1.0, 0.0)/*[!@]*/
            // @[1.21.1] @[] state.nameTag = nameTag
            /*[@]*/state.nameTag = nameTag/*[!@]*/
        }
    }
}