package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.logic.color.StaticColorSaver
import net.bewis09.bewisclient.logic.number.Precision
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.settings.types.ColorSetting
import net.bewis09.bewisclient.settings.types.FloatSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

object EntityHighlightSettings : ObjectSetting() {
    val enabled = create("enabled", BooleanSetting(false))
    val color =
        create(
            "color",
            ColorSetting(
                StaticColorSaver(0xFF0000),
                ColorSetting.STATIC,
                ColorSetting.CHANGING
            )
        )
    val alpha = create("alpha", FloatSetting(0.19f, Precision(0f, 1f, 0.01f, 2)))
}
