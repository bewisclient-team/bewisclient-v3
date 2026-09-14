package net.bewis09.bewisclient

import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.util.logic.ClientInterface
import net.fabricmc.api.ClientModInitializer

object BewisclientInitializer : ClientInterface, ClientModInitializer {
    override fun onInitializeClient() {
        EventEntrypoint.registerEntrypoints()
        EventEntrypoint.onAllEventEntrypoints(EventEntrypoint::onInitializeClient)
    }
}