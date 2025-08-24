package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

object BetterVisibilitySettings: ObjectSetting() {
    val enabled = BooleanSetting(false)
    val nether = BooleanSetting(false)
    val water = BooleanSetting(false)
    val lava = BooleanSetting(false)
    val powder_snow = BooleanSetting(false)

    init {
        create("enabled", enabled)
        create("nether", nether)
        create("water", water)
        create("lava", lava)
        create("powder_snow", powder_snow)
    }
}