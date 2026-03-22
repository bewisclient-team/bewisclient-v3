package net.bewis09.bewisclient.impl.settings

import com.google.gson.JsonPrimitive
import net.bewis09.bewisclient.settings.types.ListSetting
import net.bewis09.bewisclient.settings.types.ObjectSetting
import net.bewis09.bewisclient.util.string

object HomePlaneSettings: ObjectSetting() {
    val quickSettings = create("quick_settings", ListSetting(mutableListOf(), { it.string() }, ::JsonPrimitive))
}