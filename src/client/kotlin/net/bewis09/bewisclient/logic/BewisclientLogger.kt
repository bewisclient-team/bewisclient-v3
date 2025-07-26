package net.bewis09.bewisclient.logic

import org.slf4j.Logger

/**
 * Interface for logging in the Bewisclient.
 */
interface BewisclientLogger {
    fun getLogger(): Logger

    fun info(vararg msg: Any?) = getLogger().info(msg.joinToString(" ") { it.toString() })
    fun warn(vararg msg: Any?) = getLogger().warn(msg.joinToString(" ") { it.toString() })
    fun error(vararg msg: Any?) = getLogger().error(msg.joinToString(" ") { it.toString() })
}