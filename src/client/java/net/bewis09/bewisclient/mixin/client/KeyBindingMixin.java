package net.bewis09.bewisclient.mixin.client;

import net.bewis09.bewisclient.interfaces.KeyBindingAccessor;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin implements KeyBindingAccessor {
    @Accessor
    public abstract InputUtil.@NotNull Key getBoundKey();
}