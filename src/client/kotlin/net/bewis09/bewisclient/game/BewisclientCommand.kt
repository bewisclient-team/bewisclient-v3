package net.bewis09.bewisclient.game

import com.mojang.brigadier.arguments.StringArgumentType
import net.bewis09.bewisclient.impl.screenshot.openBigScreenshotNewScreen
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.util.toText
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.loader.api.FabricLoader
import java.io.File

object BewisclientCommand: EventEntrypoint {
    override fun onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(ClientCommandManager.literal("bewisclient").then(
                ClientCommandManager.literal("screenshot").then(
                    ClientCommandManager.argument("path", StringArgumentType.greedyString()).executes {
                        val path = StringArgumentType.getString(it, "path")
                        it.source.client.execute {
                            openBigScreenshotNewScreen(File(path))
                        }
                        1
                    }
                )
            ).executes {
                it.source.sendFeedback("Bewisclient v${FabricLoader.getInstance().allMods.firstOrNull { a -> a.metadata.id == "bewisclient" }?.metadata?.version ?: "Unknown Version"}".toText())
                1
            })
        }
    }
}