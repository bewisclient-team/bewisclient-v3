package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.settings.types.ObjectSetting
import net.bewis09.bewisclient.settings.types.WidgetsSetting

class WidgetSettings: ObjectSetting() {
    val defaults = DefaultWidgetSettings()
    val widgets = WidgetsSetting()

    init {
        create("defaults", defaults)
        create("widgets", widgets)
    }
}