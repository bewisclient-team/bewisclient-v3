package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.settings.types.IntegerSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

object OptionsMenuSettings : ObjectSetting() {
    val animationTime = create("animation_time", IntegerSetting(150, 0, 500))
    val blurBackground = create("blur_background", BooleanSetting(true))
    val buttonInTitleScreen = create("button_in_title_screen", BooleanSetting(true))
    val buttonInGameScreen = create("button_in_game_screen", BooleanSetting(true))
}
