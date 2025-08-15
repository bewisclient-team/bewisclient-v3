package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

object ZoomSettings: ObjectSetting() {
    val enabled = BooleanSetting(true)
    val smooth = BooleanSetting(true)
    val instant = BooleanSetting(false)

    init {
        create("enabled", enabled)
        create("smooth", smooth)
        create("instant", instant)
    }
}