package net.bewis09.bewisclient.impl.widget

import com.google.gson.JsonObject
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.catch
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier

object DaytimeWidget: LineWidget() {
    var format12Hours: Boolean = false

    val daytimeWidgetTranslation = Translation("widget.daytime_widget.name", "Daytime Widget")
    val daytimeWidgetDescription = Translation("widget.daytime_widget.description", "Displays the current in-game time in hours and minutes.")

    override fun getTranslation(): Translation = daytimeWidgetTranslation
    override fun getDescription(): Translation = daytimeWidgetDescription

    override fun getLines(): List<String> {
        val daytime = MinecraftClient.getInstance().world?.timeOfDay ?: 0
        val hours = (daytime / 1000L + 6) % 24
        val minutes = ((daytime % 1000L) / 1000f * 60L).toInt()

        if (format12Hours) {
            val period = if (hours < 12) "AM" else "PM"
            val adjustedHours = if (hours == 0L || hours == 12L) 12 else hours % 12
            return listOf(String.format("%02d:%02d %s", adjustedHours, minutes, period))
        }

        return listOf(String.format("%02d:%02d", hours, minutes))
    }

    override fun defaultPosition(): WidgetPosition = RelativePosition("bewisclient:cps_widget", "bottom")

    override fun getId(): Identifier = Identifier.of("bewisclient", "daytime_widget")

    override fun getWidth(): Int = 80

    override fun saveProperties(properties: JsonObject) {
        super.saveProperties(properties)

        properties.addProperty("format_12_hours", format12Hours)
    }

    override fun loadProperties(properties: JsonObject) {
        super.loadProperties(properties)

        format12Hours = catch { properties.get("format_12_hours")?.asBoolean } ?: false
    }
}