package net.bewis09.bewisclient.settings

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.util.EventEntrypoint

object SettingsLoader : EventEntrypoint {
    override fun onInitializeClient() {
        getAllSettings().forEach { settings ->
            settings.load()
        }
    }

    override fun onClientTickStart() {
        getAllSettings().forEach { settings ->
            settings.save()
        }
    }

    fun getAllSettings(): List<Settings> {
        return APIEntrypointLoader.mapEntrypoint { it.getSettingsObjects() }.flatten()
    }
}