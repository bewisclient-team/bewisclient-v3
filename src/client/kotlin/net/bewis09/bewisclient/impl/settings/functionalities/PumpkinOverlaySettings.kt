package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

object PumpkinOverlaySettings : ObjectSetting() {
    val enabled = create("enabled", BooleanSetting(false))
}
