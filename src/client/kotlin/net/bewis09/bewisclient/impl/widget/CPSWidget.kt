package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.util.Identifier

object CPSWidget: LineWidget() {
    val cpsWidgetTranslation = Translation("widget.cps_widget.name", "CPS Widget")
    val cpsWidgetDescription = Translation("widget.cps_widget.description", "Displays your current clicks per second (CPS).")

    override fun getTranslation(): Translation = cpsWidgetTranslation
    override fun getDescription(): Translation = cpsWidgetDescription

    override fun getLines(): List<String> = listOf("0 | 0 CPS")

    override fun defaultPosition(): WidgetPosition = RelativePosition("bewisclient:day_widget", "bottom")

    override fun getId(): Identifier = Identifier.of("bewisclient", "cps_widget")

    override fun getWidth(): Int = 80
}