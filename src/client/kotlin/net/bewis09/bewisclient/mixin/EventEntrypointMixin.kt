package net.bewis09.bewisclient.mixin

import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.util.EventEntrypoint.Companion.onAllEventEntrypoints

object EventEntrypointMixin {
    /**
     * The kotlin interface for [net.bewis09.bewisclient.mixin.client.MinecraftClientMixin.onInitFinished]
     * This is executed when the Minecraft client has finished starting up.
     */
    fun onMinecraftClientInitFinished() {
        onAllEventEntrypoints { e: EventEntrypoint -> e.onMinecraftClientInitFinished() }
    }
}