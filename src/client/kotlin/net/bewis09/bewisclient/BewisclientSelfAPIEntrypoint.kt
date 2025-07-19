package net.bewis09.bewisclient

import net.bewis09.bewisclient.api.BewisclientAPIEntrypoint
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.bewis09.bewisclient.settings.BewisclientSettings
import net.bewis09.bewisclient.settings.Settings
import net.bewis09.bewisclient.settings.SettingsLoader
import net.bewis09.bewisclient.widget.WidgetEventEntrypoint

class BewisclientSelfAPIEntrypoint: BewisclientAPIEntrypoint {
    override fun getEventEntrypoints(): List<EventEntrypoint> {
        return listOf(
            WidgetEventEntrypoint(),
            SettingsLoader
        )
    }

    override fun getSettingsObjects(): List<Settings> {
        return listOf(
            BewisclientSettings
        )
    }
}