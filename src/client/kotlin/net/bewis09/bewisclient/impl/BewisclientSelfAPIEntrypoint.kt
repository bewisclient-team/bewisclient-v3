package net.bewis09.bewisclient.impl

import net.bewis09.bewisclient.api.BewisclientAPIEntrypoint
import net.bewis09.bewisclient.game.Keybind
import net.bewis09.bewisclient.game.KeybindingImplementer
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.bewis09.bewisclient.impl.settings.BewisclientSettings
import net.bewis09.bewisclient.impl.widget.BiomeWidget
import net.bewis09.bewisclient.impl.widget.DayWidget
import net.bewis09.bewisclient.impl.widget.FPSWidget
import net.bewis09.bewisclient.settings.Settings
import net.bewis09.bewisclient.settings.SettingsLoader
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.WidgetLoader

class BewisclientSelfAPIEntrypoint : BewisclientAPIEntrypoint {
    override fun getEventEntrypoints(): List<EventEntrypoint> {
        return listOf(
            WidgetLoader,
            SettingsLoader,
            KeybindingImplementer,
            TranslationLoader
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

    override fun getWidgets(): List<Widget> {
        return listOf(
            FPSWidget,
            BiomeWidget,
            DayWidget
        )
    }
}