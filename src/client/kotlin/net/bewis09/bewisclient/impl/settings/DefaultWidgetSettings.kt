package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.logic.color.StaticColorSaver
import net.bewis09.bewisclient.settings.types.ColorSetting
import net.bewis09.bewisclient.settings.types.FloatSetting
import net.bewis09.bewisclient.settings.types.IntegerSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

class DefaultWidgetSettings: ObjectSetting() {
    val backgroundColor = ColorSetting(StaticColorSaver(0f, 0f, 0f, 0.5f))
    val borderColor = ColorSetting(StaticColorSaver(0f, 0f, 0f, 0f))
    val paddingSize = IntegerSetting(4)
    val lineSpacing = IntegerSetting(2)
    val textColor = ColorSetting(StaticColorSaver(1f,1f,1f,1f))
    val borderRadius = IntegerSetting(0)
    val scale = FloatSetting(1f)
    val gap = IntegerSetting(2)

    init {
        create("background_color", backgroundColor)
        create("border_color", borderColor)
        create("padding_size", paddingSize)
        create("text_color", textColor)
        create("border_radius", borderRadius)
        create("scale", scale)
        create("gap", gap)
        create("line_spacing", lineSpacing)
    }
}