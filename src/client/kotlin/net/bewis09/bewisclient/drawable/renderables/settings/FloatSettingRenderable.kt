package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.util.number.Precision
import net.bewis09.bewisclient.settings.types.Setting

class FloatSettingRenderable(
    title: Translation, description: Translation? = null, setting: Setting<Float>, precision: Precision
) : FaderSettingRenderable<Float>(title, description, setting, precision, { it })