package net.bewis09.bewisclient.util.logic

import net.minecraft.util.Util
import java.net.URI
import java.net.URL

interface WebLogic {
    fun downloadFile(url: String, onComplete: (success: ByteArray) -> Unit) {
        downloadFile(URI(url), onComplete, null)
    }

    fun downloadFile(url: String, onComplete: (success: ByteArray) -> Unit, onError: ((error: Exception) -> Unit)? = null) {
        downloadFile(URI(url), onComplete, onError)
    }

    fun downloadFile(url: URI, onComplete: (success: ByteArray) -> Unit) {
        downloadFile(url, onComplete, null)
    }

    fun downloadFile(url: URI, onComplete: (success: ByteArray) -> Unit, onError: ((error: Exception) -> Unit)? = null) {
        Util.getDownloadWorkerExecutor().execute {
            try {
                val connection = url.toURL().openConnection()
                connection.getInputStream().use { input ->
                    val bytes = input.readBytes()
                    onComplete(bytes)
                }
            } catch (e: Exception) {
                onError?.apply { this(e) } ?: error("Failed to download file from URL: ${url.path} \n  Error Message: ${e.message}")
            }
        }
    }

    fun downloadFile(url: URL, onComplete: (success: ByteArray) -> Unit) {
        downloadFile(url, onComplete, null)
    }

    fun downloadFile(url: URL, onComplete: (success: ByteArray) -> Unit, onError: ((error: Exception) -> Unit)? = null) {
        Util.getDownloadWorkerExecutor().execute {
            try {
                val connection = url.openConnection()
                connection.getInputStream().use { input ->
                    val bytes = input.readBytes()
                    onComplete(bytes)
                }
            } catch (e: Exception) {
                onError?.apply { this(e) } ?: error("Failed to download file from URL: ${url.path} \n  Error Message: ${e.message}")
            }
        }
    }
}