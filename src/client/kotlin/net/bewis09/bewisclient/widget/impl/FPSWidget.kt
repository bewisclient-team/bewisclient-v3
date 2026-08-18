package net.bewis09.bewisclient.widget.impl

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.widget.logic.BOTTOM
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget

object FPSWidget : LineWidget(
    createIdentifier("bewisclient", "fps_widget"),
    "FPS Widget",
    "Displays your current frames per second (FPS)."
) {
    val fpsText = createTranslation("fps", "%s FPS")

    override fun getLine() = fpsText(client.fps.toString())

    override fun defaultPosition(): WidgetPosition = RelativePosition(CoordinatesWidget, BOTTOM)

    override fun getMinimumWidth(): Int = 80

    override fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf(
        CustomWidget.WidgetStringData("fps", "Frames Per Second", "Your current frames per second", { client.fps.toString() }),
    )
}