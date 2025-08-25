package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.settings.types.ObjectSetting

object WidgetSettings : ObjectSetting() {
    val defaults = create("defaults", DefaultWidgetSettings)
    val widgets = create("widgets", WidgetSingleSettings)
}
