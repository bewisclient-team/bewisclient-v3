package net.bewis09.bewisclient.impl.settings.functionalities

import net.bewis09.bewisclient.settings.types.FeatureSetting

object ZoomSettings : FeatureSetting(true) {
    val smooth = boolean("smooth", true)
    val instant = boolean("instant", false)
}
