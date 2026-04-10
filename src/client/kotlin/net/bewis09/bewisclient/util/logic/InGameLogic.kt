// @VersionReplacement

package net.bewis09.bewisclient.util.logic

import net.bewis09.bewisclient.util.Bewisclient
import net.minecraft.network.chat.Component

interface InGameLogic {
    fun showTitle(message: Component) {
        // @[1.21.11] displayClientMessage(message, true) @[] sendOverlayMessage(message)
        Bewisclient.client.player?./*[@]*/sendOverlayMessage(message)/*[!@]*/
    }

    fun showSystemMessage(message: Component) {
        // @[1.21.11] displayClientMessage(message, false) @[] sendSystemMessage(message)
        Bewisclient.client.player?./*[@]*/sendSystemMessage(message)/*[!@]*/
    }
}