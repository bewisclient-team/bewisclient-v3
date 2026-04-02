package net.bewis09.bewisclient.drawable.renderables.notification

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.EventEntrypoint

object NotificationManager : EventEntrypoint {
    private val notifications = mutableListOf<Notification>()

    fun addNotification(renderable: Notification, duration: Long = 5000) {
        notifications.add(renderable)
    }

    fun getNotifications(): List<Renderable> {
        notifications.removeIf { it.progress >= 1f }
        return notifications
    }

    fun renderNotifications(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        var yOffset = 0
        getNotifications().forEach {
            it.setPosition(screenWidth - it.width, 4 + yOffset)
            it.render(screenDrawing, mouseX, mouseY)
            yOffset += it.height + 4
        }
    }
}