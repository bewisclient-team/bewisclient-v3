package net.bewis09.bewisclient.util.logic

import net.minecraft.util.Util
import java.net.URI

interface WebLogic {
    fun downloadFile(url: String, onComplete: (success: ByteArray) -> Unit) {
        Util.getIoWorkerExecutor().execute {
            val connection = URI(url).toURL().openConnection()
            connection.getInputStream().use { input ->
                val bytes = input.readBytes()
                onComplete(bytes)
            }
        }
    }
}