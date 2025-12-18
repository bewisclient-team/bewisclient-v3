package net.bewis09.bewisclient.settings.types

open class FeatureSetting(defaultEnabled: Boolean = false) : ObjectSetting() {
    val enabled = boolean("enabled", defaultEnabled)

    fun isEnabled(): Boolean = enabled.get()
}
