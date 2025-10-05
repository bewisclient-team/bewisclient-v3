package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.logic.createIdentifier
import net.bewis09.bewisclient.logic.toText
import net.bewis09.bewisclient.mixin.client.ClientPlayNetworkHandlerMixin
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

object PingWidget : LineWidget(createIdentifier("bewisclient", "ping_widget")) {
    var lastLatency = 0
    var lastRequest = System.currentTimeMillis()

    val pingText = createTranslation("ping", "Ping: %s")
    val loadingText = createTranslation("loading", "Loading...")

    override val title = "Ping Widget"
    override val description = "Displays your current ping in milliseconds (ms)."

    override fun getLine(): Text {
        if ((client.isInSingleplayer || !util.isInWorld()) && util.getCurrentRenderableScreen() != null) return pingText(99.toString())
        if (getLatency() < 0) return loadingText()
        return pingText(getLatency().toString())
    }

    override fun defaultPosition(): WidgetPosition = RelativePosition("bewisclient:daytime_widget", "bottom")

    override fun getMinimumWidth(): Int = 80

    override fun isHidden(): Boolean = client.isInSingleplayer

    private fun getLatency(): Int {
        try {
            if (client.isInSingleplayer || MinecraftClient.getInstance().networkHandler == null) return -1

            if (lastRequest + 100 < System.currentTimeMillis()) {
                if (!MinecraftClient.getInstance().debugHud.shouldShowPacketSizeAndPingCharts()) {
                    (MinecraftClient.getInstance().networkHandler as ClientPlayNetworkHandlerMixin).pingMeasurer.ping()
                }

                var l = 0
                var o = 0
                val log = client.debugHud.pingLog

                for (i in 0..19.coerceAtMost(log.length - 1)) {
                    o++
                    l += log[i].toInt()
                }

                lastRequest = System.currentTimeMillis()

                lastLatency = l / o
            }

            return lastLatency
        } catch (_: Exception) {
            return -1
        }
    }

    override fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf(
        CustomWidget.WidgetStringData("ping", "Ping", "Your current ping in milliseconds", { getLatency().toText() })
    )
}