package net.bewis09.bewisclient.util.logic

import net.minecraft.util.Util
import java.net.URI
import java.net.URL

interface WebLogic {
    fun downloadFile(url: String, onComplete: (success: ByteArray) -> Unit) {
        downloadFile(URI(url), onComplete)
    }

    fun downloadFile(url: URI, onComplete: (success: ByteArray) -> Unit) {
        Util.getDownloadWorkerExecutor().execute {
            try {
                val connection = url.toURL().openConnection()
                connection.getInputStream().use { input ->
                    val bytes = input.readBytes()
                    onComplete(bytes)
                }
            } catch (e: Exception) {
                error("Failed to download file from URL: ${url.path} \n  Error Message: ${e.message}")
            }
        }
    }

    fun downloadFile(url: URL, onComplete: (success: ByteArray) -> Unit) {
        Util.getDownloadWorkerExecutor().execute {
            try {
                val connection = url.openConnection()
                connection.getInputStream().use { input ->
                    val bytes = input.readBytes()
                    onComplete(bytes)
                }
            } catch (e: Exception) {
                error("Failed to download file from URL: ${url.path} \n  Error Message: ${e.message}")
            }
        }
    }
}