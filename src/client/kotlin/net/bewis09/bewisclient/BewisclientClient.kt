package net.bewis09.bewisclient

import net.bewis09.bewisclient.logic.Bewisclient
import net.bewis09.bewisclient.logic.EntrypointReceiver
import net.fabricmc.api.ClientModInitializer

object BewisclientClient : Bewisclient, ClientModInitializer {
	override fun onInitializeClient() {
		EntrypointReceiver.onAllEntrypoints { e: EntrypointReceiver -> e.onInitializeClient() }
	}
}