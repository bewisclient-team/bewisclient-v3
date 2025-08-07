package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.logic.number.Precision
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.settings.types.FloatSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

class FullbrightSettings: ObjectSetting() {
    val enabled = BooleanSetting(false)
    val nightVision = BooleanSetting(false)
    val brightness = FloatSetting(1f, Precision(0f, 10f, 0.01f, 2))

    init {
        create("enabled", enabled)
        create("night_vision", nightVision)
        create("brightness", brightness)
    }
}