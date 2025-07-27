package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.settings.types.IntegerSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting

class BewisclientOptionsMenuSettings: ObjectSetting() {
    val animationTime = IntegerSetting(150)
    val blurBackground = BooleanSetting(true)

    init {
        create("animation_time", animationTime)
        create("blur_background", blurBackground)
    }
}