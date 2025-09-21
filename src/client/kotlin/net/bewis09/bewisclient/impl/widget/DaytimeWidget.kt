package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.logic.toText
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.text.DateFormat
import java.time.Instant
import java.util.*

object DaytimeWidget : LineWidget(Identifier.of("bewisclient", "daytime_widget")) {
    var format12Hours = boolean("format_12_hours", isSystem12HourFormat())

    override val title = "Daytime Widget"
    override val description = "Displays the current in-game time in hours and minutes."

    override fun getLine() = getText(format12Hours.get())

    fun getText(format12Hours: Boolean): Text {
        val daytime = client.world?.timeOfDay ?: 15684L
        val hours = (daytime / 1000L + 6) % 24
        val minutes = ((daytime % 1000L) / 1000f * 60L).toInt()

        if (format12Hours) {
            val period = if (hours < 12) "AM" else "PM"
            val adjustedHours = if (hours == 0L || hours == 12L) 12 else hours % 12
            return String.format("%02d:%02d %s", adjustedHours, minutes, period).toText()
        }

        return String.format("%02d:%02d", hours, minutes).toText()
    }

    override fun defaultPosition(): WidgetPosition = RelativePosition("bewisclient:cps_widget", "bottom")

    override fun getMinimumWidth(): Int = 80

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

    override fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf(
        CustomWidget.WidgetStringData("daytime", "In-Game Time", "The current in-game time in hours and minutes", { getText((it == "true" || format12Hours.get()) && it != "false") }, "\"true\" or \"false\" to override the 12-hour format setting"),
        CustomWidget.WidgetStringData("real_time", "Real-Life Time", "The current real-life time in hours and minutes", { DateFormat.getTimeInstance(if (it == "seconds") DateFormat.MEDIUM else DateFormat.SHORT).format(Date.from(Instant.now())).toText() }, "\"seconds\" to include seconds in the time"),
    )
}
