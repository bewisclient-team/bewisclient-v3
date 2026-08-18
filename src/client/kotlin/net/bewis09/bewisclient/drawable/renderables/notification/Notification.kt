package net.bewis09.bewisclient.drawable.renderables.notification

import net.bewis09.bewisclient.common.Color
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.minecraft.network.chat.Component

abstract class Notification : Renderable() {
    abstract val progress: Float

    fun renderNotifLines(screenDrawing: ScreenDrawing, lines: List<Component>, mouseX: Int, mouseY: Int) {
        internalHeight = lines.size * 9 + 8
        internalWidth = 128 + if (isMinecrafty) 2 else 0

        if (isMinecrafty) {
            SelectiveScreenDrawer.renderButtonBackground(screenDrawing, 0f, 0f, x, y, width + 4, height, 0f, mouseX, mouseY)
        } else {
            screenDrawing.fill(x, y, width, height, Color.BLACK alpha 0.5f)
        }

        lines.forEachIndexed { index, line ->
            screenDrawing.drawText(line, x + if (isMinecrafty) 5 else 4, y + index * 9 + 4, General.getTextThemeColor())
        }
    }
}