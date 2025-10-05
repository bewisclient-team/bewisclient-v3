package net.bewis09.bewisclient.util.logic

import org.slf4j.Logger

/**
 * Interface for logging in the Bewisclient.
 */
interface BewisclientLogger {
    val logger: Logger

    fun info(vararg msg: Any?) = logger.info(msg.joinToString(" ") { it.toString() })
    fun warn(vararg msg: Any?) = logger.warn(msg.joinToString(" ") { it.toString() })
    fun error(vararg msg: Any?) = logger.error(msg.joinToString(" ") { it.toString() })
}