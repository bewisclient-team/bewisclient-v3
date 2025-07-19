package net.bewis09.bewisclient.settings

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.logic.EventEntrypoint

object SettingsLoader: EventEntrypoint() {
    override fun onInitializeClient() {
        APIEntrypointLoader.forEachEntrypoint {
            it.getSettingsObjects().forEach { settings ->
                settings.load()
            }
        }
    }
}