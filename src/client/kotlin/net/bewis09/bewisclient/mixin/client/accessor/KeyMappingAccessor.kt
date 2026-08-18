package net.bewis09.bewisclient.mixin.client.accessor

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(KeyMapping::class)
interface KeyMappingAccessor {
    @Accessor("key")
    fun getKey(): InputConstants.Key
}