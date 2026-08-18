package net.bewis09.bewisclient.widget.impl

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.version.clockTime
import net.bewis09.bewisclient.widget.logic.BOTTOM
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import java.text.DateFormat
import java.time.Instant
import java.util.*

object DayWidget : LineWidget(
    createIdentifier("bewisclient", "day_widget"),
    "Day Widget",
    "Displays the current in-game day."
) {
    val dayText = createTranslation("day", "Day %s")

    override fun getLine() = dayText(getText())

    fun getText(): Int = client.level?.clockTime?.div(24000L)?.toInt() ?: ((System.currentTimeMillis() - 1679875200000L) / 86400000L).toInt()

    override fun defaultPosition(): WidgetPosition {
        return RelativePosition(FPSWidget, BOTTOM)
    }

    override fun getMinimumWidth(): Int = 80

    override fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf(
        CustomWidget.WidgetStringData("day", "In-Game Day", "The current in-game day", { getText().toString() }),
        CustomWidget.WidgetStringData("real_day", "Real-Life Day", "The current real-life day", { (DateFormat.getDateInstance(DateFormat.SHORT, it?.let { Locale.forLanguageTag(it) } ?: Locale.getDefault()).format(Date.from(Instant.now()))) }, "A language tag (e.g., \"en-US\") to format the date accordingly")
    )
}