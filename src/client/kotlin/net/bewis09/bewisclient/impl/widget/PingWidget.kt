package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.util.Identifier

object PingWidget: LineWidget() {
    val pingWidgetTranslation = Translation("widget.ping_widget.name", "Ping Widget")
    val pingWidgetDescription = Translation("widget.ping_widget.description", "Displays your current ping in milliseconds (ms).")

    override fun getTranslation(): Translation = pingWidgetTranslation
    override fun getDescription(): Translation = pingWidgetDescription

    override fun getLines(): List<String> = listOf("Ping: 0ms")

    override fun defaultPosition(): WidgetPosition = RelativePosition("bewisclient:daytime_widget", "bottom")

    override fun getId(): Identifier = Identifier.of("bewisclient", "ping_widget")

    override fun getWidth(): Int = 80
}