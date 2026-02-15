package net.bewis09.bewisclient.drawable.renderables.notification

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.EventEntrypoint

object NotificationManager : EventEntrypoint {
    private val notifications = mutableListOf<Notification>()

    fun addNotification(renderable: Renderable, duration: Long = 5000) {
        notifications.add(Notification(renderable, System.currentTimeMillis(), duration))
    }

    fun Renderable.removeNotification() {
        notifications.removeIf { it.renderable == this }
    }

    fun getNotifications(): List<Renderable> {
        notifications.removeIf { System.currentTimeMillis() - it.startTime > it.duration }
        return notifications.map { it.renderable }
    }

    private class Notification(val renderable: Renderable, val startTime: Long, val duration: Long)

    fun renderNotifications(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        var yOffset = 0
        getNotifications().forEach {
            it.setPosition(client.window.scaledWidth - it.width, 4 + yOffset)
            it.render(screenDrawing, mouseX, mouseY)
            yOffset += it.height + 4
        }
    }
}