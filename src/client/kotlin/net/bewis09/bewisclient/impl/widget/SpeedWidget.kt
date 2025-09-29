package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.core.pos
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.bewis09.bewisclient.logic.toText
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import java.util.*

object SpeedWidget : LineWidget(Identifier.of("bewisclient", "speed_widget")), EventEntrypoint {
    val verticalSpeed = boolean("vertical_speed", false)

    var oldPos: Vec3d = Vec3d(0.0, 0.0, 0.0)
    var horizontalSpeed = 0.0
    var totalSpeed = 0.0

    override fun getLine() = String.format("%.2f m/s", if (MinecraftClient.getInstance().world == null) if (verticalSpeed.get()) 6.9f else 4.2f else if (verticalSpeed.get()) totalSpeed else horizontalSpeed).toText()

    override fun defaultPosition(): WidgetPosition = RelativePosition("bewisclient:ping_widget", "bottom")

    override fun getMinimumWidth(): Int = 80

    override val title = "Speed Widget"
    override val description = "Displays your current speed in blocks per second."

    override fun isEnabledByDefault(): Boolean = false

    override fun onInitializeClient() {
        Timer().scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    client.player?.pos()?.let {
                        totalSpeed = it.distanceTo(oldPos) * 20
                        horizontalSpeed = it.subtract(oldPos).horizontalLength() * 20
                        oldPos = it
                    }
                }
            }, 0, 50
        )
    }

    override fun appendSettingsRenderables(
        list: ArrayList<net.bewis09.bewisclient.drawable.Renderable>
    ) {
        list.add(
            verticalSpeed.createRenderable(
                "widget.speed_widget.vertical_speed", "Include Vertical Speed"
            )
        )
        super.appendSettingsRenderables(list)
    }

    override fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf(
        CustomWidget.WidgetStringData("horizontal_speed", "Horizontal Speed", "Your current horizontal speed in blocks per second", { String.format("%.2f", if (MinecraftClient.getInstance().world == null) 4.2f else horizontalSpeed).toText() }),
        CustomWidget.WidgetStringData("total_speed", "Total Speed", "Your current total speed in blocks per second", { String.format("%.2f", if (MinecraftClient.getInstance().world == null) 6.9f else totalSpeed).toText() }),
    )
}
