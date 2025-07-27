package net.bewis09.bewisclient.widget.types

import com.google.gson.JsonObject
import net.bewis09.bewisclient.widget.Widget

abstract class ScalableWidget(): Widget() {
    var scale: Float? = null

    override fun loadProperties(properties: JsonObject) {
        scale = try {
            properties.get("scale")?.asFloat
        } catch (_: Exception) {
            null
        }
    }

    override fun saveProperties(properties: JsonObject) {
        scale?.let {
            properties.addProperty("scale", it)
        }
    }

    override fun getScale(): Float {
        return scale ?: getSettings().widgetSettings.defaults.scale.get()
    }
}