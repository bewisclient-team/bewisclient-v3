package net.bewis09.bewisclient.exception

import net.bewis09.bewisclient.logic.BewisclientInterface

/**
 * Base class for all Bewisclient exceptions.
 *
 * @param message The detail message of the exception.
 */
open class BewisclientException(message: String) : Exception("[Bewisclient] $message"), BewisclientInterface