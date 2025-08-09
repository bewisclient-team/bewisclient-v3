package net.bewis09.bewisclient.impl

import net.bewis09.bewisclient.impl.functionalities.fullbright.Fullbright
import net.bewis09.bewisclient.impl.screen.OptionScreen
import net.bewis09.bewisclient.impl.screen.OptionsScreenSettingStructure
import net.bewis09.bewisclient.logic.EventEntrypoint

object TranslationLoader: EventEntrypoint {
    override fun onDatagen() {
        @Suppress("SimpleRedundantLet")
        OptionsScreenSettingStructure(OptionScreen()).let {
            it.widgetsPlane.init(0, 0, 1000, 1000)
        }
        Fullbright
    }
}