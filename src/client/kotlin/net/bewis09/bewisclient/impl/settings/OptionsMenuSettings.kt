package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.util.color.*
import net.bewis09.bewisclient.settings.types.ColorSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

object OptionsMenuSettings : ObjectSetting() {
    val animationTime = int("animation_time", 200, 0, 500)
    val blurBackground = boolean("blur_background", true)
    val buttonInTitleScreen = boolean("button_in_title_screen", true)
    val buttonInGameScreen = boolean("button_in_game_screen", true)
    val themeColor = color("theme_color", StaticColorSaver(0xFFFFFF.color), ColorSetting.STATIC)
    val backgroundColor = color("background_color", ThemeColorSaver(0.2f), ColorSetting.STATIC, ColorSetting.THEME)
    val backgroundOpacity = float("background_opacity", 0.8f, 0f, 1f, 0.01f, 2)

    fun getBackgroundColor(): Color = 0.3f within (Color.BLACK to backgroundColor.get().getColor()) alpha backgroundOpacity.get()

    fun getThemeColor(white: Float = 1f, alpha: Float = 1f, black: Float = 1f) = (black within (Color.BLACK to (white within (Color.WHITE to themeColor.get().getColor())))) alpha alpha
    fun getTextThemeColor() = (0.5f within (Color.WHITE to themeColor.get().getColor()))
}
