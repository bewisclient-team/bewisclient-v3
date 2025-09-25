package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.logic.toText
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object ServerWidget : LineWidget(Identifier.of("bewisclient", "server_widget")) {
    override fun getLine(): Text = (client.currentServerEntry?.address ?: "example.com").toText()

    override fun defaultPosition(): WidgetPosition = SidedPosition(5,5, SidedPosition.END, SidedPosition.END)

    override fun getMinimumWidth(): Int = 120

    override fun getMaximumWidth(): Int = 200

    override fun isEnabledByDefault(): Boolean = false

    override val title = "Server Widget"

    override val description = "Displays your current server IP address."

    override fun isHidden(): Boolean = client.currentServerEntry == null

    override fun getCustomWidgetDataPoints() = listOf(
        CustomWidget.WidgetStringData("server_ip","Server IP", "Your current server IP address", { getLine() })
    )
}