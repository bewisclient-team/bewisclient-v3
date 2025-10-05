package net.bewis09.bewisclient.logic

import net.minecraft.client.MinecraftClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Interface for the Bewisclient which should be implemented by most classes in the Bewisclient codebase to access important utilities more easily.
 */
interface BewisclientInterface : BewisclientLogger, FileLogic, InGameLogic, DrawingLogic {
    companion object {
        private val logger = LoggerFactory.getLogger("Bewisclient")
    }

    override val logger: Logger
        get() = BewisclientInterface.logger

    val util: UtilLogic
        get() = UtilLogic

    val client: MinecraftClient
        get() = MinecraftClient.getInstance()
}