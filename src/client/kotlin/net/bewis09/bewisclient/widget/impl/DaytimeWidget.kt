package net.bewis09.bewisclient.widget.impl

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.version.clockTime
import net.bewis09.bewisclient.widget.logic.BOTTOM
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.network.chat.Component
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

object DaytimeWidget : LineWidget(
    createIdentifier("bewisclient", "daytime_widget"),
    "Daytime Widget",
    "Displays the current in-game time in hours and minutes."
) {
    var format12Hours = boolean("format_12_hours", isSystem12HourFormat())

    override fun getLine() = getText(format12Hours.get())

    fun getText(format12Hours: Boolean): Component {
        val daytime = client.level?.clockTime ?: 15684L
        val hours = (daytime / 1000L + 6) % 24
        val minutes = ((daytime % 1000L) / 1000f * 60L).toInt()

        if (format12Hours) {
            val period = if (hours < 12) "AM" else "PM"
            val adjustedHours = if (hours == 0L || hours == 12L) 12 else hours % 12
            return String.format("%02d:%02d %s", adjustedHours, minutes, period).toText()
        }

        return String.format("%02d:%02d", hours, minutes).toText()
    }

    override fun defaultPosition(): WidgetPosition = RelativePosition(CPSWidget, BOTTOM)

    override fun getMinimumWidth(): Int = 80

    private fun isSystem12HourFormat(): Boolean {
        val df = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
        val pattern = (df as? SimpleDateFormat)?.toPattern() ?: return false
        return pattern.indexOf('h') >= 0 || pattern.indexOf('K') >= 0 || pattern.indexOf('a') >= 0
    }

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.menuQuick(format12Hours, "Use 12-Hour Format")
        super.appendSettingsRenderables(list)
    }

    override fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf(
        CustomWidget.WidgetStringData("daytime", "In-Game Time", "The current in-game time in hours and minutes", { getText((it == "true" || format12Hours.get()) && it != "false").string }, "\"true\" or \"false\" to override the 12-hour format setting"),
        CustomWidget.WidgetStringData("real_time", "Real-Life Time", "The current real-life time in hours and minutes", { DateFormat.getTimeInstance(if (it == "seconds") DateFormat.MEDIUM else DateFormat.SHORT).format(Date.from(Instant.now())) }, "\"seconds\" to include seconds in the time"),
    )
}
