package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.settings.types.FeatureSetting

object FullbrightSettings : FeatureSetting() {
    val nightVision = boolean("night_vision", false)
    val brightness = float("brightness", 1f, 0f, 15f, 0.01f, 2)
}
