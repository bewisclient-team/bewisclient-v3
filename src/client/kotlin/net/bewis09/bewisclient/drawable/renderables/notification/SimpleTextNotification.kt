package net.bewis09.bewisclient.drawable.renderables.notification

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.util.color.Color
import net.minecraft.text.Text

class SimpleTextNotification(val text: Text, val duration: Long = 5000): Renderable() {
    val start = System.currentTimeMillis()

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.translate(0L.coerceAtLeast(System.currentTimeMillis() + 500L - start - duration) / 400f * 120,0f) {
            screenDrawing.setBewisclientFont()
            val lines = screenDrawing.wrapText(text.string, 120)

            internalHeight = lines.size * 9 + 8
            internalWidth = 128

            screenDrawing.fill(x, y, width, height, Color.BLACK alpha 0.5f)

            lines.forEachIndexed { index, line ->
                screenDrawing.drawText(line, x + 4, y + index * 9 + 4, OptionsMenuSettings.getTextThemeColor())
            }

            screenDrawing.fill(x, y + height - 1, (width * ((System.currentTimeMillis() - start).toFloat() / duration)).toInt(), 1, OptionsMenuSettings.getThemeColor())
            screenDrawing.setDefaultFont()
        }
    }
}