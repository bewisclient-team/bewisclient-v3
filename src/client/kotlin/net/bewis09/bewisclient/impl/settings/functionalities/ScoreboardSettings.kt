package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.settings.types.FeatureSetting

object ScoreboardSettings : FeatureSetting() {
    val scale = float("net.bewis09.bewisclient.core.scale", 1.0f, 0.5f, 2.0f, 0.01f, 2)
}
