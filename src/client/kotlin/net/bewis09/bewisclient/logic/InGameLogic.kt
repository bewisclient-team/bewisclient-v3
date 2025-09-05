package net.bewis09.bewisclient.logic

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

interface InGameLogic {
    fun showTitle(title: Text) {
        client.player?.sendMessage(title, true)
    }

    val client: MinecraftClient
        get() = MinecraftClient.getInstance()
}