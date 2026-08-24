package net.bewis09.bewisclient.drawable.renderables.notification

import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.logic.Color
import net.minecraft.network.chat.Component

abstract class Notification<T: Notification<T>>(p: Props<T>) : PropedRenderable<T>(p) {
    abstract val progress: Float

    fun renderNotifLines(screenDrawing: ScreenDrawing, lines: List<Component>, mouseX: Int, mouseY: Int) {
        height = lines.size * 9 + 8
        width = 128 + if (General.isMinecrafty) 2 else 0

        if (General.isMinecrafty) {
            SelectiveScreenDrawer.renderButtonBackground(screenDrawing, 0f, 0f, x, y, width + 4, height, 0f, mouseX, mouseY)
        } else {
            screenDrawing.fill(x, y, width, height, Color.BLACK alpha 0.5f)
        }

        lines.forEachIndexed { index, line ->
            screenDrawing.drawText(line, x + if (General.isMinecrafty) 5 else 4, y + index * 9 + 4, General.getTextThemeColor())
        }
    }
}