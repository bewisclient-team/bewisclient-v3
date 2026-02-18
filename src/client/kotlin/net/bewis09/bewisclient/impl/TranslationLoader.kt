package net.bewis09.bewisclient.impl

import net.bewis09.bewisclient.drawable.SettingStructure
import net.bewis09.bewisclient.drawable.renderables.popup.AddWidgetPopup
import net.bewis09.bewisclient.drawable.renderables.screen.HudEditScreen
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.impl.functionalities.Fullbright
import net.bewis09.bewisclient.impl.renderable.TiwylaLinesSettingsPopup
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.util.color.colors

@Suppress("unusedExpression")
object TranslationLoader : EventEntrypoint {
    override fun onDatagen() {
        OptionScreen()
        SettingStructure
        Fullbright
        colors
        AddWidgetPopup
        HudEditScreen
        TiwylaLinesSettingsPopup
    }
}