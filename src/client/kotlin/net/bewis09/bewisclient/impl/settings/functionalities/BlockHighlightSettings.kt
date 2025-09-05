package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.logic.color.StaticColorSaver
import net.bewis09.bewisclient.settings.types.ColorSetting
import net.bewis09.bewisclient.settings.types.FeatureSetting

object BlockHighlightSettings : FeatureSetting() {
    val color = color("color", StaticColorSaver(0f, 0f, 0f), ColorSetting.STATIC, ColorSetting.CHANGING)
    val thickness = float("thickness", 0.4f, 0f, 1f, 0.01f, 2)
}
