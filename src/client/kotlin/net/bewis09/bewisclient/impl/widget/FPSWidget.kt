package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier

object FPSWidget: LineWidget() {
    override fun getLines(): List<String> = listOf(MinecraftClient.getInstance().currentFps.toString()+" FPS")

    override fun defaultPosition(): WidgetPosition = SidedPosition(5,5, SidedPosition.TransformerType.END, SidedPosition.TransformerType.START)

    override fun getId(): Identifier = Identifier.of("bewisclient", "fps_widget")

    override fun getWidth(): Int = 80
}