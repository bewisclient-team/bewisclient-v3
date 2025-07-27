package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.settings.types.ObjectSetting

class BewisclientSettingsObject() : ObjectSetting() {
    val optionsMenu = BewisclientOptionsMenuSettings()
    val widgetSettings = WidgetSettings()

    init {
        create("options_menu", optionsMenu)
        create("widgets", widgetSettings)
    }
}