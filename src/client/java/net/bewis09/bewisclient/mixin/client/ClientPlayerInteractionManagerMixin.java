package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.interfaces.BreakingProgressAccessor;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin implements BreakingProgressAccessor {
    @Accessor
    public abstract float getCurrentBreakingProgress();
}
