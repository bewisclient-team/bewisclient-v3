package net.bewis09.bewisclient.impl

import net.bewis09.bewisclient.impl.screen.OptionScreen
import net.bewis09.bewisclient.impl.screen.SettingsStructure
import net.bewis09.bewisclient.logic.EventEntrypoint

object TranslationLoader: EventEntrypoint {
    override fun onDatagen() {
        SettingsStructure(OptionScreen())
    }
}