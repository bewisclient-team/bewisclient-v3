package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.util.Identifier

object DayWidget : LineWidget() {
    val dayText = Translation("widget.day_widget.day", "Day %d")

    val dayWidgetTranslation = Translation("widget.day_widget.name", "Day Widget")
    val dayWidgetDescription = Translation("widget.day_widget.description", "Displays the current in-game day.")

    override fun getTranslation(): Translation = dayWidgetTranslation
    override fun getDescription(): Translation = dayWidgetDescription

    override fun getLines(): List<String> = listOf(
        dayText(
            client.world?.time?.div(24000L)?.toInt() ?: ((System.currentTimeMillis() - 1679875200000L) / 86400000L)
        ).string
    )

    override fun defaultPosition(): WidgetPosition {
        return RelativePosition("bewisclient:fps_widget", "bottom")
    }

    override fun getId(): Identifier = Identifier.of("bewisclient", "day_widget")

    override fun getMinimumWidth(): Int = 80
}