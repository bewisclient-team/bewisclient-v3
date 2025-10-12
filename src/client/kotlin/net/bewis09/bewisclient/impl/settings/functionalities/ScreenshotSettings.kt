package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.settings.types.ObjectSetting

object ScreenshotSettings: ObjectSetting() {
    val redirect = boolean("redirect", true)
}