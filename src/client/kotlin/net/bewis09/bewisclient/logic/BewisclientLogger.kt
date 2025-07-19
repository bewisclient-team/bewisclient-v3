package net.bewis09.bewisclient.logic

import org.slf4j.Logger

interface BewisclientLogger {
    fun getLogger(): Logger
    
    fun debug(msg: String?) = getLogger().debug(msg)
    fun debug(format: String?, arg: Any?) = getLogger().debug(format, arg)
    fun debug(format: String?, arg1: Any?, arg2: Any?) = getLogger().debug(format, arg1, arg2)
    fun debug(format: String?, vararg arguments: Any?) = getLogger().debug(format, *arguments)
    fun debug(msg: String?, t: Throwable?) = getLogger().debug(msg, t)

    fun info(msg: String?) = getLogger().info(msg)
    fun info(format: String?, arg: Any?) = getLogger().info(format, arg)
    fun info(format: String?, arg1: Any?, arg2: Any?) = getLogger().info(format, arg1, arg2)
    fun info(format: String?, vararg arguments: Any?) = getLogger().info(format, *arguments)
    fun info(msg: String?, t: Throwable?) = getLogger().info(msg, t)

    fun warn(msg: String?) = getLogger().warn(msg)
    fun warn(format: String?, arg: Any?) = getLogger().warn(format, arg)
    fun warn(format: String?, vararg arguments: Any?) = getLogger().warn(format, *arguments)
    fun warn(format: String?, arg1: Any?, arg2: Any?) = getLogger().warn(format, arg1, arg2)
    fun warn(msg: String?, t: Throwable?) = getLogger().warn(msg, t)

    fun error(msg: String?) = getLogger().error(msg)
    fun error(format: String?, arg: Any?) = getLogger().error(format, arg)
    fun error(format: String?, arg1: Any?, arg2: Any?) = getLogger().error(format, arg1, arg2)
    fun error(format: String?, vararg arguments: Any?) = getLogger().error(format, *arguments)
    fun error(msg: String?, t: Throwable?) = getLogger().error(msg, t)
}