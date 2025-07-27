package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.settings.types.ObjectSetting

class WidgetSettings: ObjectSetting() {
    val defaults = DefaultWidgetSettings()

    init {
        create("defaults", defaults)
    }
}