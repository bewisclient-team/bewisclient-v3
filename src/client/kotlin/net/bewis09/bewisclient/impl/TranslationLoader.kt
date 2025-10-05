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
    @Suppress("SimpleRedundantLet")
    override fun onDatagen() {
        SettingStructure(OptionScreen()).let {
            it.widgetsPlane.init(0, 0, 1000, 1000)
        }
        Fullbright
        colors
        AddWidgetPopup
        HudEditScreen
        TiwylaLinesSettingsPopup
    }
}