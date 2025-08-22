package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.logic.color.StaticColorSaver
import net.bewis09.bewisclient.logic.number.Precision
import net.bewis09.bewisclient.settings.types.ColorSetting
import net.bewis09.bewisclient.settings.types.FloatSetting
import net.bewis09.bewisclient.settings.types.IntegerSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

object DefaultWidgetSettings: ObjectSetting() {
    val backgroundColor = ColorSetting(StaticColorSaver(0f, 0f, 0f), ColorSetting.STATIC, ColorSetting.CHANGING)
    val backgroundOpacity = FloatSetting(0.5f, Precision(0.01f, 1f, 0.01f, 2))
    val borderColor = ColorSetting(StaticColorSaver(0f, 0f, 0f), ColorSetting.STATIC, ColorSetting.CHANGING)
    val borderOpacity = FloatSetting(0f, Precision(0.01f, 1f, 0.01f, 2))
    val paddingSize = IntegerSetting(4, 0, 10)
    val lineSpacing = IntegerSetting(3, 0, 20)
    val textColor = ColorSetting(StaticColorSaver(1f, 1f, 1f), ColorSetting.STATIC, ColorSetting.CHANGING)
    val borderRadius = IntegerSetting(0, 0, 20)
    val scale = FloatSetting(1f, Precision(0.1f, 2f, 0.01f, 2))
    val gap = IntegerSetting(2, 0, 20)
    val screenEdgeDistance = IntegerSetting(5, 0, 10)

    init {
        create("background_color", backgroundColor)
        create("background_opacity", backgroundOpacity)
        create("border_color", borderColor)
        create("border_opacity", borderOpacity)
        create("padding_size", paddingSize)
        create("text_color", textColor)
        create("border_radius", borderRadius)
        create("scale", scale)
        create("gap", gap)
        create("line_spacing", lineSpacing)
    }
}