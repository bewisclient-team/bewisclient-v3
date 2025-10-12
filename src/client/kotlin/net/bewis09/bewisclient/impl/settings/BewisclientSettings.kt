package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.settings.Settings
import net.bewis09.bewisclient.settings.Version2Migration

object BewisclientSettings : Settings {
    override fun getId(): String = "bewisclient"
    override fun getMainSetting() = BewisclientSettingsObject

    override fun load() {
        if (Version2Migration.update()) {
            save()
        }

        super.load()
    }
}