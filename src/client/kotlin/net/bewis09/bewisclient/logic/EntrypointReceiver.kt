package net.bewis09.bewisclient.logic

import net.bewis09.bewisclient.exception.ProgramCodeException

/**
 * A class when it is needed to do something when those entrypoints are called.
 */
open class EntrypointReceiver {
    companion object {
        val entrypointReceivers = mutableListOf<EntrypointReceiver>()
        var isRegistered = false

        private fun register(entrypointReceiver: EntrypointReceiver) {
            entrypointReceivers.add(entrypointReceiver)
        }

        fun registerEntrypoints() {
            if (isRegistered) throw ProgramCodeException("EntrypointReceiver is already registered.")
        }

        fun onAllEntrypoints(callback: (EntrypointReceiver) -> Unit) {
            entrypointReceivers.forEach(callback)
        }
    }

    /**
     * This is called when the Minecraft client is launched, before the game or any mods or resources are loaded.
     */
    open fun onMinecraftClientInitStarted() {

    }

    /**
     * This is the normal mod initialization method.
     * Some resources may not be loaded yet, so this is not the place to do things that require all resources.
     */
    open fun onInitializeClient() {

    }

    /**
     * Used to do things for which all resources need to be loaded.
     * This is only called when the game starts, not when the resources are reloaded.
     */
    open fun onMinecraftClientInitFinished() {

    }
}