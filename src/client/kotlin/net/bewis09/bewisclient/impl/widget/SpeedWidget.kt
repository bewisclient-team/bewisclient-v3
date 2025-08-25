package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import java.util.*

object SpeedWidget : LineWidget(), EventEntrypoint {
    val verticalSpeed = create("vertical_speed", BooleanSetting(false))

    val speedWidgetTranslation = Translation("widget.speed_widget.name", "Speed Widget")
    val speedWidgetDescription = Translation(
        "widget.speed_widget.description", "Displays your current speed in blocks per second."
    )

    var oldPos: Vec3d = Vec3d(0.0, 0.0, 0.0)
    var speed = 0.0

    override fun getLines(): List<String> = listOf(String.format("%.2f m/s", speed))

    override fun getOutOfWorldLines(): List<String> = listOf(if (verticalSpeed.get()) "6.90 m/s" else "4.20 m/s")

    override fun defaultPosition(): WidgetPosition = RelativePosition("bewisclient:ping_widget", "bottom")

    override fun getId(): Identifier = Identifier.of("bewisclient", "speed_widget")

    override fun getWidth(): Int = 80

    override fun getTranslation(): Translation = speedWidgetTranslation

    override fun getDescription(): Translation = speedWidgetDescription

    override fun isEnabledByDefault(): Boolean = false

    override fun onInitializeClient() {
        Timer().scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    MinecraftClient.getInstance().player?.pos?.let {
                        speed = if (verticalSpeed.get()) {
                            it.distanceTo(oldPos) * 20
                        } else {
                            it.subtract(oldPos).horizontalLength() * 20
                        }
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
}
