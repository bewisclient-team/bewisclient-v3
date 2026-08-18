package net.bewis09.bewisclient.game.keybinds

import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.util.Bewisclient
import org.lwjgl.glfw.GLFW

val OpenOptionScreen = Keybind(GLFW.GLFW_KEY_RIGHT_SHIFT, "open_option_screen", "Open Bewisclient Option Screen") {
    Bewisclient.setRenderableScreen(OptionScreen.getOrCreateInstance())
}