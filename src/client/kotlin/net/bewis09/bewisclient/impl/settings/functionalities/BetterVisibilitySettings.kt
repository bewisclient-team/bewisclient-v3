package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

object BetterVisibilitySettings : ObjectSetting() {
    val enabled = create("enabled", BooleanSetting(false))
    val nether = create("nether", BooleanSetting(false))
    val water = create("water", BooleanSetting(false))
    val lava = create("lava", BooleanSetting(false))
    val powder_snow = create("powder_snow", BooleanSetting(false))
}
