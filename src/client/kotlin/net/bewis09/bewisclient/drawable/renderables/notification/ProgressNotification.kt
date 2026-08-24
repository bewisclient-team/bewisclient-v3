package net.bewis09.bewisclient.drawable.renderables.notification

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.drawer.translate
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.logic.Color
import net.minecraft.network.chat.Component

class ProgressNotification(p: Props<ProgressNotification>) : Notification<ProgressNotification>(p) {
    lateinit var text: Component

    init { props() }

    override var progress: Float = 0f
        set(value) {
            if (value >= 1f) {
                field = -1f
                removeStartTime = System.currentTimeMillis() + 500L
                return
            }
            field = value
        }
        get() = if (System.currentTimeMillis() - removeStartTime > 400) 1f else field

    var removeStartTime = Long.MAX_VALUE

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.translate(((System.currentTimeMillis() - removeStartTime) / 400f).coerceIn(0f, 1f) * 120, 0f) {
            val lines = screenDrawing.wrapText(text.string, 120).map(Component::literal) + Component.literal("${((if (progress == -1f) 1f else progress) * 100).toInt()}%").withColor(Color.GRAY.argb)
            renderNotifLines(screenDrawing, lines, mouseX, mouseY)
            screenDrawing.fill(x + if (General.isMinecrafty) 1 else 0, y + height - 1, (width * (if (progress == -1f) 1f else progress)).toInt(), 1, General.getThemeColor())
        }
    }
}