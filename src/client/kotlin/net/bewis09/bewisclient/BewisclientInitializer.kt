package net.bewis09.bewisclient

import net.bewis09.bewisclient.util.logic.BewisclientInterface
import net.bewis09.bewisclient.util.EventEntrypoint
import net.fabricmc.api.ClientModInitializer

object BewisclientInitializer : BewisclientInterface, ClientModInitializer {
    override fun onInitializeClient() {
        EventEntrypoint.registerEntrypoints()
        EventEntrypoint.onAllEventEntrypoints { e: EventEntrypoint -> e.onInitializeClient() }
    }
}