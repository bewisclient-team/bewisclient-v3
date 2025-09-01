package net.bewis09.bewisclient

import net.bewis09.bewisclient.logic.BewisclientInterface
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.fabricmc.api.ClientModInitializer

object BewisclientInitializer : BewisclientInterface, ClientModInitializer {
    override fun onInitializeClient() {
        EventEntrypoint.registerEntrypoints()
        EventEntrypoint.onAllEventEntrypoints { e: EventEntrypoint -> e.onInitializeClient() }
    }
}