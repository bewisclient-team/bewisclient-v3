package net.bewis09.bewisclient.impl

import net.bewis09.bewisclient.impl.functionalities.fullbright.Fullbright
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.SettingStructure
import net.bewis09.bewisclient.drawable.renderables.popup.AddWidgetPopup
import net.bewis09.bewisclient.drawable.renderables.screen.HudEditScreen
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.bewis09.bewisclient.logic.colors

object TranslationLoader: EventEntrypoint {
    override fun onDatagen() {
        @Suppress("SimpleRedundantLet")
        SettingStructure(OptionScreen()).let {
            it.widgetsPlane.init(0, 0, 1000, 1000)
        }
        Fullbright
        colors
        AddWidgetPopup
        HudEditScreen
    }
}