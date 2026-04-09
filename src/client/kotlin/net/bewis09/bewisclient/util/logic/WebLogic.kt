package net.bewis09.bewisclient.util.logic

import net.bewis09.bewisclient.version.Util
import net.bewis09.bewisclient.util.Bewisclient
import java.io.ByteArrayOutputStream
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
        Util.nonCriticalIoPool().execute {
            try {
                val connection = url.toURL().openConnection()
                connection.getInputStream().use { input ->
                    val bytes = input.readBytes()
                    onComplete(bytes)
                }
            } catch (e: Exception) {
                onError?.apply { this(e) } ?: Bewisclient.error("Failed to download file from URL: ${url.path} \n  Error Message: ${e.message}")
            }
        }
    }

    fun downloadFile(url: URL, onComplete: (success: ByteArray) -> Unit) {
        downloadFile(url, onComplete, null)
    }

    fun downloadFile(url: URL, onComplete: (success: ByteArray) -> Unit, onError: ((error: Exception) -> Unit)? = null) {
        Util.nonCriticalIoPool().execute {
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

    fun downloadFileWithProgress(url: URI, onProgress: (progress: Float) -> Unit, onComplete: (success: ByteArray) -> Unit, onError: ((error: Exception) -> Unit)? = null) {
        Util.nonCriticalIoPool().execute {
            try {
                val connection = url.toURL().openConnection()
                val contentLength = connection.contentLengthLong
                var totalRead: Long = 0
                connection.getInputStream().use { input ->
                    val buffer = ByteArray(8192)
                    var bytesRead: Int
                    val output = ByteArrayOutputStream()
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        totalRead += bytesRead
                        if (contentLength > 0) {
                            onProgress(totalRead.toFloat() / contentLength)
                        }
                    }
                    onComplete(output.toByteArray())
                }
            } catch (e: Exception) {
                onError?.apply { this(e) } ?: error("Failed to download file from URL: ${url.path} \n  Error Message: ${e.message}")
            }
        }
    }
}