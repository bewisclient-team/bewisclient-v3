package net.bewis09.bewisclient.util

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.util.logic.BewisclientInterface

/**
 * A class when it is needed to do something when those entrypoints are called.
 */
interface EventEntrypoint : BewisclientInterface {
    companion object {
        private val eventEntrypointReceivers = mutableListOf<EventEntrypoint>()
        private var isRegistered = false

        /**
         * Registers all entrypoint receivers.
         * This should only be called once, and it will throw an exception if called multiple times.
         */
        fun registerEntrypoints() {
            if (isRegistered) throw IllegalStateException("EventEntrypoints already registered.")

            APIEntrypointLoader.mapEntrypoint { eventEntrypointReceivers.addAll(it.getEventEntrypoints()) }
        }

        /**
         * Invokes the given callback for all registered entrypoint receivers.
         */
        fun onAllEventEntrypoints(callback: (EventEntrypoint) -> Unit) {
            eventEntrypointReceivers.forEach(callback)
        }
    }

    /**
     * This is the normal mod initialization method.
     * Some resources may not be loaded yet, so this is not the place to do things that require all resources.
     */
    fun onInitializeClient() {

    }

    /**
     * Used to do things for which all resources need to be loaded.
     * This is only called when the game starts, not when the resources are reloaded.
     */
    fun onMinecraftClientInitFinished() {

    }

    /**
     * Called before the data generation starts.
     * This is the place to register data generators or other things that need to be done before the data generation.
     */
    fun onDatagen() {

    }

    /**
     * Called when the resources are reloaded.
     */
    fun onResourcesReloaded() {

    }

    /**
     * Called at the start of each client tick.
     */
    fun onClientTickStart() {

    }
}