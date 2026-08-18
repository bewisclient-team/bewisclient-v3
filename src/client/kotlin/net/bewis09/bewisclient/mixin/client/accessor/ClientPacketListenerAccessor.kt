package net.bewis09.bewisclient.mixin.client.accessor

import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.client.multiplayer.PingDebugMonitor
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(ClientPacketListener::class)
interface ClientPacketListenerAccessor {
    @Accessor("pingDebugMonitor")
    fun getPingDebugMonitor(): PingDebugMonitor
}