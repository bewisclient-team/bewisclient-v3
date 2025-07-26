package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.settings.Settings

object BewisclientSettings : Settings {
    val settingsObject = BewisclientSettingsObject()

    override fun getId(): String = "bewisclient"
    override fun getMainSetting() = settingsObject
}