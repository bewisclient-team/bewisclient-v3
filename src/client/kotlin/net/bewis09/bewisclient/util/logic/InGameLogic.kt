package net.bewis09.bewisclient.util.logic

import net.bewis09.bewisclient.util.Bewisclient
import net.minecraft.network.chat.Component

interface InGameLogic {
    fun showTitle(title: Component) {
        Bewisclient.client.player?.displayClientMessage(title, true)
    }
}