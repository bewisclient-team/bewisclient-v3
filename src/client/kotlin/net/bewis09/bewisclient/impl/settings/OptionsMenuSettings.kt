package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.settings.types.ObjectSetting

object OptionsMenuSettings : ObjectSetting() {
    val animationTime = int("animation_time", 200, 0, 500)
    val blurBackground = boolean("blur_background", true)
    val buttonInTitleScreen = boolean("button_in_title_screen", true)
    val buttonInGameScreen = boolean("button_in_game_screen", true)
}
