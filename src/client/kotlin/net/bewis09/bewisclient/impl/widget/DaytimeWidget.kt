package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
import java.text.DateFormat
import java.util.*

object DaytimeWidget : LineWidget() {
    var format12Hours = boolean("format_12_hours", isSystem12HourFormat())

    val daytimeWidgetTranslation = Translation("widget.daytime_widget.name", "Daytime Widget")
    val daytimeWidgetDescription = Translation(
        "widget.daytime_widget.description", "Displays the current in-game time in hours and minutes."
    )

    override fun getTranslation(): Translation = daytimeWidgetTranslation
    override fun getDescription(): Translation = daytimeWidgetDescription

    override fun getLines(): List<String> {
        val daytime = MinecraftClient.getInstance().world?.timeOfDay ?: 15684L
        val hours = (daytime / 1000L + 6) % 24
        val minutes = ((daytime % 1000L) / 1000f * 60L).toInt()

        if (format12Hours.get()) {
            val period = if (hours < 12) "AM" else "PM"
            val adjustedHours = if (hours == 0L || hours == 12L) 12 else hours % 12
            return listOf(String.format("%02d:%02d %s", adjustedHours, minutes, period))
        }

        return listOf(String.format("%02d:%02d", hours, minutes))
    }

    override fun defaultPosition(): WidgetPosition = RelativePosition("bewisclient:cps_widget", "bottom")

    override fun getId(): Identifier = Identifier.of("bewisclient", "daytime_widget")

    override fun getWidth(): Int = 80

    private fun isSystem12HourFormat(): Boolean {
        val df = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
        val pattern = (df as? java.text.SimpleDateFormat)?.toPattern() ?: return false
        return pattern.indexOf('h') >= 0 || pattern.indexOf('K') >= 0 || pattern.indexOf('a') >= 0
    }

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(
            format12Hours.createRenderable(
                "widget.daytime_widget.format_12_hours", "Use 12-Hour Format"
            )
        )
        super.appendSettingsRenderables(list)
    }
}
