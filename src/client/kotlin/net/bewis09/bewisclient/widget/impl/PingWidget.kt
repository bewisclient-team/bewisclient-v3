package net.bewis09.bewisclient.widget.impl

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.mixin.client.accessor.ClientPacketListenerAccessor
import net.bewis09.bewisclient.widget.logic.BOTTOM
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.network.chat.Component

object PingWidget : LineWidget(
    createIdentifier("bewisclient", "ping_widget"),
    "Ping Widget",
    "Displays your current ping in milliseconds (ms)."
) {
    var lastLatency = 0
    var lastRequest = System.currentTimeMillis()

    var lastUpdate = System.currentTimeMillis()
    var latencyText = 0

    val pingText = createTranslation("ping", "Ping: %s")
    val loadingText = createTranslation("loading", "Loading...")

    override fun getLine(): Component {
        if ((isHidden() || !isInWorld()) && getCurrentRenderableScreen() != null) return pingText(99.toString())
        if (getLatency() < 0) return loadingText()
        return pingText(getLatency().toString())
    }

    override fun defaultPosition(): WidgetPosition = RelativePosition(DaytimeWidget, BOTTOM)

    override fun getMinimumWidth(): Int = 80

    override fun isHidden(): Boolean = client.singleplayerServer != null

    private fun getLatency(): Int {
        try {
            if (isHidden() || client.connection == null) return -1

            if (lastRequest + 100 < System.currentTimeMillis()) {
                if (!client.debugOverlay.showNetworkCharts()) (client.connection as? ClientPacketListenerAccessor)?.getPingDebugMonitor()?.tick()

                var l = 0
                var o = 0
                val log = client.debugOverlay.pingLogger

                for (i in 0..19.coerceAtMost(log.size() - 1)) {
                    o++
                    l += log[i].toInt()
                }

                lastRequest = System.currentTimeMillis()

                lastLatency = l / o
            }

            if (lastUpdate + 1000 < System.currentTimeMillis()) {
                latencyText = lastLatency
                lastUpdate = System.currentTimeMillis()
            }

            return latencyText
        } catch (_: Exception) {
            return -1
        }
    }

    override fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf(
        CustomWidget.WidgetStringData("ping", "Ping", "Your current ping in milliseconds", { getLatency().toString() })
    )
}