package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.interfaces.WorldRendererQueueAccessor;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements WorldRendererQueueAccessor {
    @Accessor
    public abstract OrderedRenderCommandQueueImpl getEntityRenderCommandQueue();
}
