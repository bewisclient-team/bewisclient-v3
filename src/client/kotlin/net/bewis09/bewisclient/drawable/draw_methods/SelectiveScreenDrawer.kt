package net.bewis09.bewisclient.drawable.draw_methods

import net.bewis09.bewisclient.util.Bewisclient

val SelectiveScreenDrawer: DrawMethods
    get() = if (Bewisclient.isMinecrafty) minecraftyMethods else flatMethods

val minecraftyMethods = MinecraftyMethods
val flatMethods = FlatMethods