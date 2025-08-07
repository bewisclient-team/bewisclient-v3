package net.bewis09.bewisclient.logic

import net.bewis09.bewisclient.impl.settings.BewisclientSettings
import net.bewis09.bewisclient.impl.settings.BewisclientSettingsObject
import net.minecraft.client.MinecraftClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Interface for the Bewisclient which should be implemented by most classes in the Bewisclient codebase to access important utilities more easily.
 */
interface BewisclientInterface : BewisclientLogger, FileLogic, InGameLogic {
    companion object {
        private val logger = LoggerFactory.getLogger("Bewisclient")
    }

    fun getSettings(): BewisclientSettingsObject {
        return BewisclientSettings.settingsObject
    }

    override fun getLogger(): Logger = logger

    fun getClient(): MinecraftClient = MinecraftClient.getInstance()
}