package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.impl.settings.BewisclientSettings
import net.bewis09.bewisclient.settings.types.ObjectSetting

object PanoramaSettings : ObjectSetting() {
    val enabled = boolean("enabled", false) { _, _ ->
        if (path.get().isNotEmpty() && !BewisclientSettings.isLoading) {
            client.reloadResources()
        }
    }
    val path = string("path", "")
}