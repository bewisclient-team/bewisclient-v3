package net.bewis09.bewisclient.impl

import net.bewis09.bewisclient.impl.functions.fullbright.Fullbright
import net.bewis09.bewisclient.impl.screen.OptionScreen
import net.bewis09.bewisclient.impl.screen.SettingsStructure
import net.bewis09.bewisclient.logic.EventEntrypoint

object TranslationLoader: EventEntrypoint {
    override fun onDatagen() {
        @Suppress("SimpleRedundantLet")
        SettingsStructure(OptionScreen()).let {
            it.widgetsPlane.init(0, 0, 1000, 1000)
        }
        Fullbright
    }
}