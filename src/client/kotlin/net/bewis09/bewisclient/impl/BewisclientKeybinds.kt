package net.bewis09.bewisclient.impl

import net.bewis09.bewisclient.game.Keybind
import net.bewis09.bewisclient.drawable.OptionScreen
import net.bewis09.bewisclient.screen.RenderableScreen
import net.minecraft.client.MinecraftClient
import org.lwjgl.glfw.GLFW

val OpenOptionScreen = Keybind(GLFW.GLFW_KEY_RIGHT_SHIFT, "open_option_screen", "Open Option Screen") {
    MinecraftClient.getInstance().setScreen(RenderableScreen(OptionScreen()))
}