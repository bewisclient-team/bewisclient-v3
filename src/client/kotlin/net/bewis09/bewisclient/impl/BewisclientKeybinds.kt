package net.bewis09.bewisclient.impl

import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.game.Keybind
import net.bewis09.bewisclient.logic.Bewisclient
import net.bewis09.bewisclient.screen.RenderableScreen
import org.lwjgl.glfw.GLFW

val OpenOptionScreen = Keybind(GLFW.GLFW_KEY_RIGHT_SHIFT, "open_option_screen", "Open Bewisclient Option Screen") {
    Bewisclient.client.setScreen(RenderableScreen(OptionScreen()))
}