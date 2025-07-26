package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.settings.types.ObjectSetting

class BewisclientSettingsObject() : ObjectSetting() {
    val optionsMenu = BewisclientOptionsMenuSetting()

    init {
        create("options_menu", optionsMenu)
    }
}