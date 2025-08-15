package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.settings.Settings

object BewisclientSettings : Settings {
    override fun getId(): String = "bewisclient"
    override fun getMainSetting() = BewisclientSettingsObject
}