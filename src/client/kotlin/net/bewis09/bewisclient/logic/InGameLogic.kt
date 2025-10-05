package net.bewis09.bewisclient.logic

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

interface InGameLogic {
    fun showTitle(title: Text) {
        MinecraftClient.getInstance().player?.sendMessage(title, true)
    }
}