package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.settings.types.BooleanMapSetting
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.settings.types.IntegerSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

object HeldItemTooltipSettings : ObjectSetting() {
    val enabled = create("enabled", BooleanSetting(true))
    val maxShownLines = create("max_shown_lines", IntegerSetting(5, 1, 10))
    val showMap = create("show_map", BooleanMapSetting())
}
