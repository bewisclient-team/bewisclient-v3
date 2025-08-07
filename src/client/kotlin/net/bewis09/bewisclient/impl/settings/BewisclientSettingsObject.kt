package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.impl.settings.functionalities.FullbrightSettings
import net.bewis09.bewisclient.settings.types.ObjectSetting

class BewisclientSettingsObject() : ObjectSetting() {
    val optionsMenu = OptionsMenuSettings()
    val widgetSettings = WidgetSettings()
    val fullbright = FullbrightSettings()

    init {
        create("options_menu", optionsMenu)
        create("widgets", widgetSettings)
        create("fullbright", fullbright)
    }
}