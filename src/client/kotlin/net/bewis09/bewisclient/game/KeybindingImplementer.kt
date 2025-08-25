package net.bewis09.bewisclient.game

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient

object KeybindingImplementer : EventEntrypoint {
    val CATEGORY = Translation(
        "key.category", "Bewisclient"
    )

    override fun onInitializeClient() {
        val keybinds = APIEntrypointLoader.mapEntrypoint { it.getKeybinds() }.flatten()

        keybinds.forEach {
            KeyBindingHelper.registerKeyBinding(it.keyBinding)
        }

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient? ->
            keybinds.forEach {
                while (it.keyBinding.wasPressed()) {
                    it.action?.invoke()
                }

                it.tick?.let { a ->
                    a(it.keyBinding.isPressed)
                }
            }
        })
    }
}