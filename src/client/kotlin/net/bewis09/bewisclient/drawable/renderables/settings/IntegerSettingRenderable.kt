package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.interfaces.SettingInterface
import net.bewis09.bewisclient.logic.number.Precision

class IntegerSettingRenderable(title: Translation, description: Translation?, setting: SettingInterface<Int>, min: Int, max: Int) : FaderSettingRenderable<Int>(title, description, setting, Precision(min.toFloat(), max.toFloat(), 1f, 0), { original: Float -> original.toInt() })