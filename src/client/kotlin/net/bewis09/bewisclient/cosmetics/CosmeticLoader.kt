package net.bewis09.bewisclient.cosmetics

import com.google.gson.Gson
import net.bewis09.bewisclient.data.Constants
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.util.catch
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.util.logic.BewisclientInterface
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.PlayerListEntry
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import java.net.HttpURLConnection
import java.net.URI
import java.security.MessageDigest.getInstance
import kotlin.io.encoding.Base64

object CosmeticLoader : BewisclientInterface, EventEntrypoint {
    val status = mutableMapOf<Identifier, DownloadStatus>()
    val byteData = mutableMapOf<Identifier, Pair<ByteArray, Int>>()
    val cosmetics = mutableMapOf<Identifier, Cosmetic>()

    fun getStatus(identifier: Identifier): DownloadStatus {
        return status.getOrDefault(identifier, DownloadStatus.NOT_STARTED)
    }

    fun downloadCosmetic(identifier: Identifier, path: String, frames: Int) {
        catch {
            downloadFile(Constants.COSMETIC_URL + path) {
                status[identifier] = DownloadStatus.LOADED
                saveRelativeFile(it, "bewisclient", "server", path)
                byteData[identifier] = it to frames
            }
        } ?: run {
            status[identifier] = DownloadStatus.FAILED
        }
    }

    fun loadCosmetic(type: CosmeticType, id: String, hash: String, frames: Int) {
        val identifier = createIdentifier("bewisclient", "cosmetics/${type.id}/$id")
        if (getStatus(identifier) == DownloadStatus.NOT_STARTED) {
            status[identifier] = DownloadStatus.IN_PROGRESS

            val data = checkCosmetic(type, id + (if (frames > 1) ".gif" else ".png"), hash) ?: run {
                downloadCosmetic(identifier, "${type.id}/$id${if (frames > 1) ".gif" else ".png"}", frames)
                return
            }

            byteData[identifier] = data to frames
            status[identifier] = DownloadStatus.LOADED
        }
    }

    fun loadCosmeticFromByteArray(identifier: Identifier, data: ByteArray, frames: Int): Cosmetic? {
        return if (frames > 1) {
            catch { AnimatedCosmetic.create(identifier, data, frames) }
        } else {
            catch { StaticCosmetic.create(identifier, data) }
        }
    }

    fun checkCosmetic(type: CosmeticType, path: String, hash: String): ByteArray? {
        val data = readRelativeFileBytes("bewisclient", "server", "${type.id}/$path") ?: return null
        return if (checkHash(data, hash)) data else null
    }

    fun checkHash(data: ByteArray, hash: String): Boolean {
        return try {
            val digest = getInstance("SHA-256")
            val computedHash = Base64.encode(digest.digest(data))
            computedHash.equals(hash, ignoreCase = true)
        } catch (e: Exception) {
            false
        }
    }

    override fun onInitializeClient() {
        return
//        Util.getIoWorkerExecutor().execute {
//            val connection = URI(Constants.DATA_URL).toURL().openConnection() as? HttpURLConnection ?: return@execute
//            connection.requestMethod = "POST"
//            connection.doOutput = true
//
//            val out: ByteArray = """{"uuid":"${MinecraftClient.getInstance().gameProfile.id}"}""".toByteArray()
//
//            connection.setFixedLengthStreamingMode(out.size)
//            connection.connect()
//            connection.outputStream.use { it.write(out) }
//
//            val result = connection.getInputStream().readAllBytes()
//            val data = Gson().fromJson(result.decodeToString(), CosmeticData::class.java)
//
//            data.cosmetics.forEach {
//                loadCosmetic(
//                    CosmeticType.fromId(it.type) ?: return@forEach,
//                    it.id,
//                    it.hash,
//                    it.frames
//                )
//            }
//        }
    }

    override fun onMinecraftClientInitFinished() {
        return
//        byteData.forEach {
//            if (status[it.key] == DownloadStatus.LOADED) {
//                status[it.key] = DownloadStatus.REGISTER_IN_PROGRESS
//                val cosmetic = loadCosmeticFromByteArray(it.key, it.value.first, it.value.second) ?: return@forEach
//                cosmetics[it.key] = cosmetic
//                status[it.key] = DownloadStatus.COMPLETED
//            }
//        }
    }

    @Suppress("PropertyName")
    data class CosmeticData(
        val base_url: String,
        val cosmetics: Array<CosmeticEntry>,
        val specials: Array<SpecialEntry>
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as CosmeticData

            if (base_url != other.base_url) return false
            if (!cosmetics.contentEquals(other.cosmetics)) return false
            if (!specials.contentEquals(other.specials)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = base_url.hashCode()
            result = 31 * result + cosmetics.contentHashCode()
            result = 31 * result + specials.contentHashCode()
            return result
        }
    }

    fun getEntityBySkinTextures(hashCode: Int): PlayerListEntry? {
        val client = MinecraftClient.getInstance()
        val playerList = client.networkHandler?.playerList ?: return null
        return playerList.firstOrNull { it.skinTextures.hashCode() == hashCode }
    }

    data class SpecialEntry(
        val id: String,
        val type: String,
        val uuid: String
    )

    data class CosmeticEntry(
        val id: String,
        val type: String,
        val hash: String,
        val frames: Int,
        val default: Boolean
    )

    fun getCosmeticForPlayer(player: PlayerListEntry, type: CosmeticType): Cosmetic? {
        return null
//        val id = Identifier.of("bewisclient", "cosmetics/${type.id}/golden_creeper")
//        if (cosmetics.containsKey(id)) return cosmetics[id]
//        if (status[id] == DownloadStatus.LOADED) {
//            status[id] = DownloadStatus.REGISTER_IN_PROGRESS
//            val data = byteData[id] ?: return null
//            val cosmetic = loadCosmeticFromByteArray(id, data.first, data.second) ?: return null
//            cosmetics[id] = cosmetic
//            status[id] = DownloadStatus.COMPLETED
//        }
//        return null
    }

    enum class DownloadStatus {
        NOT_STARTED,
        IN_PROGRESS,
        LOADED,
        FAILED,
        REGISTER_IN_PROGRESS,
        COMPLETED
    }
}