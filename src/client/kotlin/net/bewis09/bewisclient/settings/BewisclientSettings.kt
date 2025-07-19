package net.bewis09.bewisclient.settings

import net.bewis09.bewisclient.settings.structure.BewisclientSettingsObject

object BewisclientSettings: Settings {
    val settingsObject = BewisclientSettingsObject(this)

    override fun getId(): String = "bewisclient"
    override fun getMainSetting() = settingsObject
}