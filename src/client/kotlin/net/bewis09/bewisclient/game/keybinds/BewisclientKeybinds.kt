package net.bewis09.bewisclient.game.keybinds

import com.mojang.blaze3d.platform.InputConstants
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.util.Bewisclient

val OpenOptionScreen = Keybind(InputConstants.KEY_RSHIFT, "open_option_screen", "Open Bewisclient Option Screen") {
    Bewisclient.setRenderableScreen(OptionScreen.getOrCreateInstance())
}