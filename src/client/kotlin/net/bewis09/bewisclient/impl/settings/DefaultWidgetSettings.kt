package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.logic.color.StaticColorSaver
import net.bewis09.bewisclient.logic.number.Precision
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.settings.types.ColorSetting
import net.bewis09.bewisclient.settings.types.FloatSetting
import net.bewis09.bewisclient.settings.types.IntegerSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

object DefaultWidgetSettings : ObjectSetting() {
    val backgroundColor = create("background_color", ColorSetting(StaticColorSaver(0f, 0f, 0f), ColorSetting.STATIC, ColorSetting.CHANGING))
    val backgroundOpacity = create("background_opacity", FloatSetting(0.5f, Precision(0.01f, 1f, 0.01f, 2)))
    val borderColor = create("border_color", ColorSetting(StaticColorSaver(0f, 0f, 0f), ColorSetting.STATIC, ColorSetting.CHANGING))
    val borderOpacity = create("border_opacity", FloatSetting(0f, Precision(0.01f, 1f, 0.01f, 2)))
    val paddingSize = create("padding_size", IntegerSetting(4, 0, 10))
    val lineSpacing = create("line_spacing", IntegerSetting(3, 0, 20))
    val textColor = create("text_color", ColorSetting(StaticColorSaver(1f, 1f, 1f), ColorSetting.STATIC, ColorSetting.CHANGING))
    val borderRadius = create("border_radius", IntegerSetting(0, 0, 20))
    val shadow = create("shadow", BooleanSetting(true))
    val scale = create("scale", FloatSetting(.8f, Precision(0.5f, 2f, 0.01f, 2)))
    val gap = create("gap", IntegerSetting(1, 0, 20))
    val screenEdgeDistance = create("screen_edge_distance", IntegerSetting(5, 0, 10))
}
}