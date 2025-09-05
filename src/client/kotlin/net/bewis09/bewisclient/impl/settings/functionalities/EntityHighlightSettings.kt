package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.logic.color.StaticColorSaver
import net.bewis09.bewisclient.settings.types.ColorSetting
import net.bewis09.bewisclient.settings.types.FeatureSetting

object EntityHighlightSettings : FeatureSetting() {
    val color = color("color", StaticColorSaver(0xFF0000), ColorSetting.STATIC, ColorSetting.CHANGING)
    val alpha = float("alpha", 0.19f, 0f, 1f, 0.01f, 2)
}
