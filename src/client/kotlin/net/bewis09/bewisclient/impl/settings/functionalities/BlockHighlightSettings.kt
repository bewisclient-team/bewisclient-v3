package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.logic.color.OpaqueStaticColorSaver
import net.bewis09.bewisclient.logic.number.Precision
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.settings.types.ColorSetting
import net.bewis09.bewisclient.settings.types.FloatSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

class BlockHighlightSettings: ObjectSetting() {
    val enabled = BooleanSetting(false)
    val color = ColorSetting(OpaqueStaticColorSaver(0f,0f,0f))
    val thickness = FloatSetting(1f, Precision(0f, 1f, 0.01f, 2))

    init {
        create("enabled", enabled)
        create("color", color)
        create("thickness", thickness)
    }
}