package net.bewis09.bewisclient.util.logic

import net.bewis09.bewisclient.util.logic.BewisclientLogger
import net.minecraft.client.MinecraftClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Interface for the Bewisclient which should be implemented by most classes in the Bewisclient codebase to access important utilities more easily.
 */
interface BewisclientInterface : BewisclientLogger, FileLogic, InGameLogic, DrawingLogic, WebLogic {
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