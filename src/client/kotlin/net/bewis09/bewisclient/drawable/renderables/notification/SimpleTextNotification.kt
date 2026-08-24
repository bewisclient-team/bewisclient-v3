package net.bewis09.bewisclient.drawable.renderables.notification

import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.drawer.translate
import net.bewis09.bewisclient.features.sidebar.General
import net.minecraft.network.chat.Component

class SimpleTextNotification(p: Props<SimpleTextNotification>) : Notification<SimpleTextNotification>(p) {
    lateinit var text: Component
    val duration: Long = 5000

    init { props() }

    val start = System.currentTimeMillis()

    override val progress: Float
        get() = ((System.currentTimeMillis() - start).toFloat() / duration).coerceIn(0f, 1f)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.translate(0L.coerceAtLeast(System.currentTimeMillis() + 500L - start - duration) / 400f * 120, 0f) {
            renderNotifLines(screenDrawing, screenDrawing.wrapText(text.string, 120).map(String::toText), mouseX, mouseY)
            screenDrawing.fill(x + if (General.isMinecrafty) 1 else 0, y + height - 1, (width * ((System.currentTimeMillis() - start).toFloat() / duration)).toInt(), 1, General.getThemeColor())
        }
    }
}