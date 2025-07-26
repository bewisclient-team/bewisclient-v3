package net.bewis09.bewisclient.exception

import net.bewis09.bewisclient.data.Constants

/**
 * Exception thrown by Bewisclient when there is a bug in the code or an extension of it.
 *
 * This exception is used to indicate that there is a problem in the Bewisclient code itself,
 * and it is not something that can be fixed by the user.
 *
 * The user is advised to report this issue to the Bewisclient team for further investigation.
 *
 * @param message The detail message of the exception.
 */
open class ProgramCodeException(message: String) : BewisclientException(message) {
    init {
        error(
            """Exception thrown by Bewisclient. "$message"
            |There is probably a bug in the Bewisclient code or an extension of it
            |In most case you can't do anything about it, except trying to update the Bewisclient version you're using.
            |If you are using the latest version, please report this issue to the Bewisclient team.
            |
            |Please use github (${Constants.GITHUB_URL}) to contact the developers.
            |Include the following information in your message: 
            |1. The Bewisclient version you are using.
            |2. The full stack trace of the exception.
            |3. The message of the exception: "$message"
            |4. A description of what you were doing when the exception occurred.
            |5. Any other relevant information that might help us diagnose the issue.""".trimMargin()
        )
    }
}