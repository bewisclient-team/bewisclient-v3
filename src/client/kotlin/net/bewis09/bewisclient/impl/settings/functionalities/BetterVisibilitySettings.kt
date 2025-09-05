package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.settings.types.FeatureSetting

object BetterVisibilitySettings : FeatureSetting() {
    val nether = boolean("nether", false)
    val water = boolean("water", false)
    val lava = boolean("lava", false)
    val powder_snow = boolean("powder_snow", false)
}
