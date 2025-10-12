package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.settings.types.FeatureSetting

object FireHeightSettings: FeatureSetting() {
    val height = float("height", 1f, 0f, 1f, 0.01f, 2)
}