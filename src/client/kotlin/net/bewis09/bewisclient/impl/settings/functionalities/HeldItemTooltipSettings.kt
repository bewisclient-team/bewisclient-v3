package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.settings.types.BooleanMapSetting
import net.bewis09.bewisclient.settings.types.FeatureSetting

object HeldItemTooltipSettings : FeatureSetting() {
    val maxShownLines = int("max_shown_lines", 5, 1, 10)
    val showMap = create("show_map", BooleanMapSetting())
}
