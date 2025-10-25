package net.bewis09.bewisclient.cosmetics

import com.google.gson.Gson
import com.mojang.authlib.GameProfile
import net.bewis09.bewisclient.data.Constants
import net.bewis09.bewisclient.settings.types.ObjectSetting
import net.bewis09.bewisclient.settings.types.StringMapSetting
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.util.catch
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.PlayerListEntry
import net.minecraft.item.Items
import net.minecraft.util.Util
import java.net.HttpURLConnection
import java.net.URI
import java.security.MessageDigest.getInstance
import kotlin.io.encoding.Base64

object CosmeticLoader : ObjectSetting(), EventEntrypoint {
    val allowedCosmetics = mutableListOf<CosmeticIdentifier>()
    val elytraCosmetics = mutableListOf<CosmeticIdentifier>()
    val status = mutableMapOf<CosmeticIdentifier, DownloadStatus>()
    val byteData = mutableMapOf<CosmeticIdentifier, Pair<ByteArray, Int>>()
    val cosmetics = mutableMapOf<CosmeticIdentifier, Cosmetic>()
        get() {
            for (id in field.keys) {
                if (status[id] == DownloadStatus.LOADED) {
                    status[id] = DownloadStatus.REGISTER_IN_PROGRESS
                    val data = byteData[id] ?: continue
                    val cosmetic = loadCosmeticFromByteArray(id, data.first, data.second) ?: continue
                    field[id] = cosmetic
                    status[id] = DownloadStatus.COMPLETED
                }
            }
            return field
        }

    val selected = create("selected", StringMapSetting())
    val elytra = boolean("elytra", true)

    fun getStatus(identifier: CosmeticIdentifier): DownloadStatus {
        return status.getOrDefault(identifier, DownloadStatus.NOT_STARTED)
    }

    fun downloadCosmetic(cosmetic: CosmeticIdentifier, path: String, frames: Int) {
        catch {
            downloadFile(Constants.COSMETIC_URL + path) {
                status[cosmetic] = DownloadStatus.LOADED
                saveRelativeFile(it, "bewisclient", "server", path)
                byteData[cosmetic] = it to frames
            }
        } ?: run {
            status[cosmetic] = DownloadStatus.FAILED
        }
    }

    fun loadCosmetic(cosmetic: CosmeticIdentifier, hash: String, frames: Int) {
        if (getStatus(cosmetic) == DownloadStatus.NOT_STARTED) {
            status[cosmetic] = DownloadStatus.IN_PROGRESS

            val data = checkCosmetic(cosmetic.type, cosmetic.id + (if (frames > 1) ".gif" else ".png"), hash) ?: run {
                downloadCosmetic(cosmetic, "${cosmetic.type}/${cosmetic.id}${if (frames > 1) ".gif" else ".png"}", frames)
                return
            }

            byteData[cosmetic] = data to frames
            status[cosmetic] = DownloadStatus.LOADED
        }
    }

    fun loadCosmeticFromByteArray(identifier: CosmeticIdentifier, data: ByteArray, frames: Int): Cosmetic? {
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
        return catch {
            val digest = getInstance("SHA-256")
            val computedHash = Base64.encode(digest.digest(data))
            return@catch computedHash.equals(hash, ignoreCase = true)
        } ?: false
    }

    override fun onInitializeClient() {
        Util.getIoWorkerExecutor().execute {
            val result: ByteArray = catch {
                val connection = URI(Constants.DATA_URL).toURL().openConnection() as? HttpURLConnection ?: return@execute
                connection.requestMethod = "POST"
                connection.doOutput = true

                val out: ByteArray = """{"uuid":"${MinecraftClient.getInstance().gameProfile.id}"}""".toByteArray()

                connection.setFixedLengthStreamingMode(out.size)
                connection.connect()
                connection.outputStream.use { it.write(out) }

                val res = connection.getInputStream().readAllBytes()

                saveRelativeFile(res, "bewisclient", "server", "data.json")

                return@catch res
            } ?: readRelativeFileBytes("bewisclient", "server", "data.json") ?: return@execute

            val data = Gson().fromJson(result.decodeToString(), CosmeticData::class.java)

            data.cosmetics.forEach {
                if (it.default || data.specials.any { a -> a.id == it.id && a.type == it.type && a.uuid == client.gameProfile.id.toString() }) {
                    it.getCosmetic()?.let { element -> allowedCosmetics.add(element) }
                }
                if (it.has_elytra) {
                    it.getCosmetic()?.let { element -> elytraCosmetics.add(element) }
                }
            }

            data.cosmetics.forEach {
                loadCosmetic(
                    it.getCosmetic() ?: return@forEach,
                    it.hash,
                    it.frames,
                )
            }
        }
    }

    override fun onMinecraftClientInitFinished() {
        byteData.forEach {
            if (status[it.key] == DownloadStatus.LOADED) {
                status[it.key] = DownloadStatus.REGISTER_IN_PROGRESS
                val cosmetic = loadCosmeticFromByteArray(it.key, it.value.first, it.value.second) ?: return@forEach
                cosmetics[it.key] = cosmetic
                status[it.key] = DownloadStatus.COMPLETED
            }
        }
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
        val default: Boolean,
        @Suppress("PropertyName")
        val has_elytra: Boolean
    ) {
        fun getCosmetic() = CosmeticType.fromId(type)?.let { CosmeticIdentifier(it, id) }
    }

    fun getCosmeticForPlayer(player: GameProfile, type: CosmeticType): Cosmetic? {
        val elytraEquipped = client.world?.players?.firstOrNull { it.gameProfile.id == player.id }?.inventory?.getStack(38)?.item == Items.ELYTRA && type == CosmeticType.CAPE
        if (player.id != client.gameProfile.id || (elytraEquipped && !this.elytra.get())) return null
        val id = CosmeticIdentifier(type, this.selected[type.id] ?: return null)
        if (id !in allowedCosmetics || (elytraEquipped && !elytraCosmetics.contains(id))) return null
        return cosmetics.getOrDefault(id, null)
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