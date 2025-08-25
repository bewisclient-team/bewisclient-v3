package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

object ZoomSettings : ObjectSetting() {
    val enabled = create("enabled", BooleanSetting(true))
    val smooth = create("smooth", BooleanSetting(true))
    val instant = create("instant", BooleanSetting(false))
}
