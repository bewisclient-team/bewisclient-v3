package net.bewis09.bewisclient.game

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.impl.functionalities.Perspective
import net.bewis09.bewisclient.util.EventEntrypoint
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient

object KeybindingImplementer : EventEntrypoint {
    override fun onInitializeClient() {
        val keybinds = APIEntrypointLoader.mapEntrypoint { it.getKeybinds() }.flatten()

        keybinds.forEach {
            KeyBindingHelper.registerKeyBinding(it.keyBinding)
        }

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient ->
            keybinds.forEach {
                while (it.keyBinding.wasPressed()) {
                    it.action?.invoke()
                }

                it.tick?.let { a ->
                    a(it.keyBinding.isPressed)
                }
            }

            if (client.options.togglePerspectiveKey.isPressed) {
                Perspective.cameraAddPitch = 0f
                Perspective.cameraAddYaw = 0f
            }
        })
    }
}