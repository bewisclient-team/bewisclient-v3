package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.util.Identifier

object FPSWidget : LineWidget() {
    val fpsWidgetTranslation = Translation("widget.fps_widget.name", "FPS Widget")
    val fpsWidgetDescription = Translation("widget.fps_widget.description", "Displays your current frames per second (FPS).")

    override fun getTranslation(): Translation = fpsWidgetTranslation
    override fun getDescription(): Translation = fpsWidgetDescription

    override fun getLines(): List<String> = listOf(client.currentFps.toString() + " FPS")

    override fun defaultPosition(): WidgetPosition = RelativePosition("bewisclient:coordinates_widget", "bottom")

    override fun getId(): Identifier = Identifier.of("bewisclient", "fps_widget")

    override fun getWidth(): Int = 80
}