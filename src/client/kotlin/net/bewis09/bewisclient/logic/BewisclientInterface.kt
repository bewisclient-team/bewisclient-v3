package net.bewis09.bewisclient.logic

import net.bewis09.bewisclient.settings.BewisclientSettings
import net.bewis09.bewisclient.settings.structure.BewisclientSettingsObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Interface for the Bewisclient which should be implemented by most classes in the Bewisclient codebase to access important utilities more easily.
 */
interface BewisclientInterface: BewisclientLogger, FileLogic {
    companion object {
        private val logger = LoggerFactory.getLogger("Bewisclient")
    }

    fun getSettings(): BewisclientSettingsObject {
        return BewisclientSettings.settingsObject
    }

    override fun getLogger(): Logger = logger
}