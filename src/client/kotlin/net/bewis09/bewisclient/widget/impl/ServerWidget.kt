package net.bewis09.bewisclient.widget.impl

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.network.chat.Component

object ServerWidget : LineWidget(
    createIdentifier("bewisclient", "server_widget"),
    "Server Widget",
    "Displays your current server IP address."
) {
    override fun getLine(): Component = (client.currentServer?.ip ?: "example.com").toText()

    override fun defaultPosition(): WidgetPosition = SidedPosition(5, 5, SidedPosition.END, SidedPosition.END)

    override fun getMinimumWidth(): Int = 120

    override fun getMaximumWidth(): Int = 200

    override fun isEnabledByDefault(): Boolean = false

    override fun isHidden(): Boolean = client.currentServer == null

    override fun getCustomWidgetDataPoints() = listOf(
        CustomWidget.WidgetStringData("server_ip", "Server IP", "Your current server IP address", { getLine().string })
    )
}