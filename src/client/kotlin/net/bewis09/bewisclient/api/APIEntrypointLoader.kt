package net.bewis09.bewisclient.api

import net.fabricmc.loader.api.FabricLoader

object APIEntrypointLoader {
    /**
     * A list of all [BewisclientAPIEntrypoint]s that are registered in the mod. This list is lazily initialized
     */
    private val entrypoints: List<BewisclientAPIEntrypoint> by lazy {
        FabricLoader.getInstance().getEntrypoints("bewisclient", BewisclientAPIEntrypoint::class.java).filterIsInstance<BewisclientAPIEntrypoint>()
    }

    /**
     * Execute the given action for each [BewisclientAPIEntrypoint] that is registered in the mod.
     */
    fun forEachEntrypoint(action: (BewisclientAPIEntrypoint) -> Unit) {
        entrypoints.forEach(action)
    }
}