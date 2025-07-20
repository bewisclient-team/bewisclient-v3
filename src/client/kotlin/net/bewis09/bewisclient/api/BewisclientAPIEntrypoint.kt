package net.bewis09.bewisclient.api

import net.bewis09.bewisclient.game.Keybind
import net.bewis09.bewisclient.logic.BewisclientInterface
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.bewis09.bewisclient.settings.Settings

/**
 * The Bewisclient API entrypoint interface.
 * It is used to provide access to the Bewisclient API and register extensions for the Bewisclient mod.
 *
 * To add your own Bewisclient API entrypoint, implement this interface in your mod.
 * Then register your entrypoint in your `fabric.mod.json` file under `custom`, `bewisclient`
 */
interface BewisclientAPIEntrypoint: BewisclientInterface {
    /**
     * Returns a list of [EventEntrypoint]s that are registered in the mod.
     * This is used to register event handlers for the Bewisclient API.
     *
     * @return A list of [EventEntrypoint]s that are registered in the mod.
     */
    fun getEventEntrypoints(): List<EventEntrypoint> {
        return emptyList()
    }

    /**
     * Returns a list of [Settings] objects that are registered in the mod.
     * This is used to register settings for the Bewisclient API.
     * Each [Settings] object should be a singleton that holds the settings for your mod.
     *
     * @return A list of [Settings] objects that are registered in the mod.
     */
    fun getSettingsObjects(): List<Settings> {
        return emptyList()
    }

    fun getKeybinds(): List<Keybind> {
        return emptyList()
    }
}