package net.bewis09.bewisclient.mixin.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.bewis09.bewisclient.interfaces.KeyBindingAccessor;
import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyMapping.class)
public abstract class KeyBindingMixin implements KeyBindingAccessor {
    @Accessor
    public abstract InputConstants.@NotNull Key getKey();
}