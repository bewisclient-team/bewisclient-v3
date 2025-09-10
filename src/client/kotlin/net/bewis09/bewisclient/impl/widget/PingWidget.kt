package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.mixin.client.ClientPlayNetworkHandlerMixin
import net.bewis09.bewisclient.screen.RenderableScreen
import net.bewis09.bewisclient.widget.logic.RelativePosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.util.Identifier

object PingWidget : LineWidget() {
    var lastLatency = 0
    var lastRequest = System.currentTimeMillis()

    val pingWidgetTranslation = Translation("widget.ping_widget.name", "Ping Widget")
    val pingWidgetDescription = Translation("widget.ping_widget.description", "Displays your current ping in milliseconds (ms).")

    val pingText = Translation("widget.ping_widget.ping", "Ping: %s")
    val loadingText = Translation("widget.ping_widget.loading", "Loading...")

    override fun getTranslation(): Translation = pingWidgetTranslation
    override fun getDescription(): Translation = pingWidgetDescription

    override fun getLines(): List<String> {
        if ((client.isInSingleplayer || client.world == null) && (client.currentScreen is RenderableScreen)) return arrayListOf(pingText(99.toString()).string)
        if (getLatency() < 0) return arrayListOf(loadingText.getTranslatedString())
        return arrayListOf(pingText(getLatency().toString()).string)
    }

    override fun defaultPosition(): WidgetPosition = RelativePosition("bewisclient:daytime_widget", "bottom")

    override fun getId(): Identifier = Identifier.of("bewisclient", "ping_widget")

    override fun getMinimumWidth(): Int = 80

    override fun isHidden(): Boolean = client.isInSingleplayer

    private fun getLatency(): Int {
        try {
            if (lastRequest + 100 < System.currentTimeMillis()) {
                if (!client.debugHud.shouldShowPacketSizeAndPingCharts()) {
                    (client.networkHandler as ClientPlayNetworkHandlerMixin).pingMeasurer.ping()
                }

                var l = 0
                var o = 0
                val log = client.debugHud.pingLog

                for (i in 0..19.coerceAtMost(log.length - 1)) {
                    o++
                    l += log.get(i).toInt()
                }

                lastRequest = System.currentTimeMillis()

                lastLatency = l / o
            }

            return lastLatency
        } catch (_: Exception) {
            return -1
        }
    }
}