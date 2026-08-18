package net.bewis09.bewisclient.mixin.client.accessor

import net.bewis09.bewisclient.version.ScissorStack
import net.minecraft.client.gui.navigation.ScreenRectangle
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor
import java.util.Deque

@Mixin(ScissorStack::class)
interface ScissorStackAccessor {
    @Accessor
    fun getStack(): Deque<ScreenRectangle>
}