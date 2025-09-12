package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.logic.toText
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.util.Identifier

object FPSWidget : LineWidget(Identifier.of("bewisclient", "fps_widget")) {
    override val title = "FPS Widget"
    override val description = "Displays your current frames per second (FPS)."

    val fpsText = createTranslation("fps", "%s FPS")

    override fun getLine() = fpsText(client.currentFps.toString())

    override fun defaultPosition(): WidgetPosition = RelativePosition("bewisclient:coordinates_widget", "bottom")

    override fun getMinimumWidth(): Int = 80

    override fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf(
        CustomWidget.WidgetStringData("fps", "Frames Per Second", "Your current frames per second", { client.currentFps.toText() }),
    )
}