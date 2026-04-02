package net.bewis09.bewisclient.drawable.renderables.notification

import net.bewis09.bewisclient.drawable.Renderable

abstract class Notification() : Renderable() {
    abstract val progress: Float
}