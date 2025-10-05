package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.logic.color.StaticColorSaver
import net.bewis09.bewisclient.settings.types.ColorSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

object DefaultWidgetSettings : ObjectSetting() {
    val backgroundColor = color("background_color", StaticColorSaver(0f, 0f, 0f), ColorSetting.STATIC, ColorSetting.CHANGING, ColorSetting.THEME)
    val backgroundOpacity = float("background_opacity", 0.5f, 0f, 1f, 0.01f, 2)
    val borderColor = color("border_color", StaticColorSaver(0f, 0f, 0f), ColorSetting.STATIC, ColorSetting.CHANGING, ColorSetting.THEME)
    val borderOpacity = float("border_opacity", 0f, 0f, 1f, 0.01f, 2)
    val paddingSize = int("padding_size", 4, 0, 10)
    val lineSpacing = int("line_spacing", 2, 0, 20)
    val textColor = color("text_color", StaticColorSaver(1f, 1f, 1f), ColorSetting.STATIC, ColorSetting.CHANGING, ColorSetting.THEME)
    val borderRadius = int("border_radius", 0, 0, 20)
    val shadow = boolean("shadow", true)
    val scale = float("net.bewis09.bewisclient.core.scale", .8f, 0.5f, 2f, 0.01f, 2)
    val gap = int("gap", 1, 0, 20)
    val screenEdgeDistance = int("screen_edge_distance", 5, 0, 10)
}