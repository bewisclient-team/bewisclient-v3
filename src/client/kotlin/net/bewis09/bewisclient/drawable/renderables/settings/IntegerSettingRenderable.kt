package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.number.Precision
import net.bewis09.bewisclient.settings.types.Setting

class IntegerSettingRenderable(title: Translation, description: Translation?, setting: Setting<Int>, min: Int, max: Int) : FaderSettingRenderable<Int>(title, description, setting, Precision(min.toFloat(), max.toFloat(), 1f, 0), { original -> original.toInt() })