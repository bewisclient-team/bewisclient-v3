package net.bewis09.bewisclient.api

import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.entrypoint.EntrypointContainer

object APIEntrypointLoader {
    /**
     * A list of all [BewisclientAPIEntrypoint]s that are registered in the mod. This list is lazily initialized
     */
    private val entrypoints: List<EntrypointContainer<BewisclientAPIEntrypoint>> by lazy {
        FabricLoader.getInstance().getEntrypointContainers("bewisclient", BewisclientAPIEntrypoint::class.java).sortedBy { it.provider.metadata.id?.let { id -> if (id == "bewisclient") "" else id } }.map { it }
    }

    /**
     * Execute the given action for each Container of [BewisclientAPIEntrypoint] that is registered in the mod.
     */
    fun <T> mapContainer(action: (EntrypointContainer<BewisclientAPIEntrypoint>) -> T): List<T> = entrypoints.map(action)

    /**
     * Execute the given action for each [BewisclientAPIEntrypoint] that is registered in the mod.
     */
    fun <T> mapEntrypoint(action: (BewisclientAPIEntrypoint) -> T): List<T> = entrypoints.map { action(it.entrypoint) }
}