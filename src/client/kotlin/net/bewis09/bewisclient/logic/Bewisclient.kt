package net.bewis09.bewisclient.logic

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface Bewisclient: BewisclientLogger {
    companion object {
        private val logger = LoggerFactory.getLogger("Bewisclient")
    }

    override fun getLogger(): Logger = logger
}