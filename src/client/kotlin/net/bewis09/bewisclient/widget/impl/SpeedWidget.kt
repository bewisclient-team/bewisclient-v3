package net.bewis09.bewisclient.widget.impl

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.widget.logic.BOTTOM
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.world.phys.Vec3
import java.util.*

object SpeedWidget : LineWidget(
    createIdentifier("bewisclient", "speed_widget"),
    "Speed Widget",
    "Displays your current speed in blocks per second."
), EventEntrypoint {
    val verticalSpeed = boolean("vertical_speed", false)

    var oldPos: Vec3 = Vec3.ZERO
    var horizontalSpeed = 0f
    var totalSpeed = 0f

    override fun getLine() = String.format("%.2f m/s", if (!isInWorld()) if (verticalSpeed.get()) 6.9f else 4.2f else if (verticalSpeed.get()) totalSpeed else horizontalSpeed).toText()

    override fun defaultPosition(): WidgetPosition = RelativePosition(PingWidget, BOTTOM)

    override fun getMinimumWidth(): Int = 80

    override fun isEnabledByDefault(): Boolean = false

    override fun onClientTickStart() {
        client.player?.position()?.let {
            totalSpeed = it.distanceTo(oldPos).toFloat() * 20f
            horizontalSpeed = it.subtract(oldPos).horizontalDistance().toFloat() * 20
            oldPos = it
        }
    }

    override fun appendSettingsRenderables(
        list: ArrayList<Renderable>
    ) {
        list.addRenderableQS(verticalSpeed, "Include Vertical Speed")
        super.appendSettingsRenderables(list)
    }

    override fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf(
        CustomWidget.WidgetStringData("horizontal_speed", "Horizontal Speed", "Your current horizontal speed in blocks per second", { String.format("%.2f", if (!isInWorld()) 4.2f else horizontalSpeed).toText() }),
        CustomWidget.WidgetStringData("total_speed", "Total Speed", "Your current total speed in blocks per second", { String.format("%.2f", if (!isInWorld()) 6.9f else totalSpeed).toText() }),
    )
}
