package net.bewis09.bewisclient.api

import net.fabricmc.loader.api.FabricLoader

object APIEntrypointLoader {
    /**
     * A list of all [BewisclientAPIEntrypoint]s that are registered in the mod. This list is lazily initialized
     */
    private val entrypoints: List<BewisclientAPIEntrypoint> by lazy {
        FabricLoader.getInstance().getEntrypointContainers("bewisclient", BewisclientAPIEntrypoint::class.java).sortedBy { it.provider.metadata.id?.let { id -> if (id == "bewisclient") "" else id } }.map { it.entrypoint }
    }

    /**
     * Execute the given action for each [BewisclientAPIEntrypoint] that is registered in the mod.
     */
    fun <T> mapEntrypoint(action: (BewisclientAPIEntrypoint) -> T): List<T> = entrypoints.map(action)

    /**
     * Execute the given action for each [BewisclientAPIEntrypoint] that is registered in the mod.
     */
    fun forEachEntrypoint(action: (BewisclientAPIEntrypoint) -> Unit) {
        entrypoints.forEach(action)
    }
}