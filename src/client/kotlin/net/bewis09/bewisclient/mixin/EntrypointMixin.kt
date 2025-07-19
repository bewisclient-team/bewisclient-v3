package net.bewis09.bewisclient.mixin

import net.bewis09.bewisclient.logic.EntrypointReceiver
import net.bewis09.bewisclient.logic.EntrypointReceiver.Companion.onAllEntrypoints

object EntrypointMixin {
    fun onMinecraftClientInitStarted() {
        EntrypointReceiver.registerEntrypoints()
        onAllEntrypoints { e: EntrypointReceiver -> e.onMinecraftClientInitStarted() }
    }

    fun onMinecraftClientInitFinished() {
        onAllEntrypoints { e: EntrypointReceiver -> e.onMinecraftClientInitFinished() }
    }
}