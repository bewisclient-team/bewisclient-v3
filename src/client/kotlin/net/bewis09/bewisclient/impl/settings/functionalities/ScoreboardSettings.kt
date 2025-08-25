package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.logic.number.Precision
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.settings.types.FloatSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

object ScoreboardSettings : ObjectSetting() {
    val enabled = create("enabled", BooleanSetting(false))
    val scale = create("scale", FloatSetting(1.0f, Precision(0.5f, 2.0f, 0.01f, 2)))
}
