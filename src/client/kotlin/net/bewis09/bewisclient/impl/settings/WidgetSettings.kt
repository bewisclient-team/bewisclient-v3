package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.settings.types.ObjectSetting

object WidgetSettings : ObjectSetting() {
    init {
        create("defaults", DefaultWidgetSettings)
        create("widgets", WidgetSingleSettings)
    }
}
