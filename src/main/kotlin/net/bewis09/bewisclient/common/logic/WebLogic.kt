package net.bewis09.bewisclient.common.logic

import net.bewis09.bewisclient.common.Util
import net.bewis09.bewisclient.server.BewisclientServer
import java.io.ByteArrayOutputStream
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

interface WebLogic {
    fun requestPost(url: URL, postData: ByteArray, headers: Map<String, String>? = null): HttpResponse<ByteArray>? {
        return try {
            HttpClient.newBuilder().build().send(
                HttpRequest.newBuilder()
                    .uri(url.toURI())
                    .POST(HttpRequest.BodyPublishers.ofByteArray(postData)).apply {
                        if (headers != null) {
                            headers(*headers.toList().flatMap { listOf(it.first, it.second) }.toTypedArray())
                        }
                    }
                    .build(),
                HttpResponse.BodyHandlers.ofByteArray()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun downloadWithOfflineFile(url: String, offlinePath: String): ByteArray? {
        return downloadWithOfflineFile(URI(url), offlinePath)
    }

    fun downloadWithOfflineFile(url: URI, offlinePath: String): ByteArray? {
        return downloadWithOfflineFile(url.toURL(), offlinePath)
    }

    fun downloadWithOfflineFile(url: URL, offlinePath: String): ByteArray? {
        return try {
            downloadSync(url).also { BewisclientServer.saveRelativeFile(it, "bewisclient", "server", offlinePath) }
        } catch (_: Exception) {
            BewisclientServer.readRelativeFileBytes("bewisclient", "server", offlinePath) ?: return null
        }
    }

    fun downloadSync(url: String): ByteArray {
        return downloadSync(URI(url))
    }

    fun downloadSync(url: URI): ByteArray {
        return downloadSync(url.toURL())
    }

    fun downloadSync(url: URL): ByteArray {
        return url.openStream().use { it.readBytes() }
    }

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
                onComplete(downloadSync(url))
            } catch (e: Exception) {
                onError?.apply { this(e) } ?: BewisclientServer.error("Failed to download file from URL: ${url.path} \n  Error Message: ${e.message}")
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