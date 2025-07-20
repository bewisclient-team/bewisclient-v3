package net.bewis09.bewisclient.impl

import net.bewis09.bewisclient.api.BewisclientAPIEntrypoint
import net.bewis09.bewisclient.game.Keybind
import net.bewis09.bewisclient.game.KeybindingImplementer
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.bewis09.bewisclient.settings.BewisclientSettings
import net.bewis09.bewisclient.settings.Settings
import net.bewis09.bewisclient.settings.SettingsLoader
import net.bewis09.bewisclient.widget.WidgetEventEntrypoint

class BewisclientSelfAPIEntrypoint: BewisclientAPIEntrypoint {
    override fun getEventEntrypoints(): List<EventEntrypoint> {
        return listOf(
            WidgetEventEntrypoint(),
            SettingsLoader,
            KeybindingImplementer
        )
    }

    override fun getSettingsObjects(): List<Settings> {
        return listOf(
            BewisclientSettings
        )
    }

    override fun getKeybinds(): List<Keybind> {
        return listOf(
            OpenOptionScreen
        )
    }
}