package net.bewis09.bewisclient.util.logic

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

interface InGameLogic {
    fun showTitle(title: Text) {
        MinecraftClient.getInstance().player?.sendMessage(title, true)
    }
}