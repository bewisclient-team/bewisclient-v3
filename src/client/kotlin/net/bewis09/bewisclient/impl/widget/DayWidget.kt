package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.util.toText
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import java.text.DateFormat
import java.time.Instant
import java.util.*

object DayWidget : LineWidget(createIdentifier("bewisclient", "day_widget")) {
    val dayText = createTranslation("day", "Day %s")

    override val title = "Day Widget"
    override val description = "Displays the current in-game day."

    override fun getLine() = dayText(getText())

    fun getText(): Int = client.world?.timeOfDay?.div(24000L)?.toInt() ?: ((System.currentTimeMillis() - 1679875200000L) / 86400000L).toInt()

    override fun defaultPosition(): WidgetPosition {
        return RelativePosition("bewisclient:fps_widget", "bottom")
    }

    override fun getMinimumWidth(): Int = 80

    override fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf(
        CustomWidget.WidgetStringData("day", "In-Game Day", "The current in-game day", { getText().toText() }),
        CustomWidget.WidgetStringData("real_day", "Real-Life Day", "The current real-life day", { (DateFormat.getDateInstance(DateFormat.SHORT, it?.let { Locale.forLanguageTag(it) } ?: Locale.getDefault()).format(Date.from(Instant.now()))).toText() }, "A language tag (e.g., \"en-US\") to format the date accordingly")
    )
}