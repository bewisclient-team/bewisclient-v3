package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.logic.number.Precision
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.settings.types.FloatSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

object FullbrightSettings : ObjectSetting() {
    val enabled = create("enabled", BooleanSetting(false))
    val nightVision = create("night_vision", BooleanSetting(false))
    val brightness = create("brightness", FloatSetting(1f, Precision(0f, 15f, 0.01f, 2)))
}
