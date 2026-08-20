package net.bewis09.bewisclient.features.cosmetics

import com.google.gson.Gson
import com.mojang.authlib.GameProfile
import net.bewis09.bewisclient.common.*
import net.bewis09.bewisclient.cosmetics.CommonCosmeticLoader
import net.bewis09.bewisclient.cosmetics.CommonCosmeticLoader.cosmeticData
import net.bewis09.bewisclient.cosmetics.CosmeticIdentifier
import net.bewis09.bewisclient.cosmetics.CosmeticType
import net.bewis09.bewisclient.data.Constants
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.components.element.Rectangle
import net.bewis09.bewisclient.drawable.renderables.components.element.TextElement
import net.bewis09.bewisclient.drawable.renderables.components.structure.Plane
import net.bewis09.bewisclient.drawable.renderables.components.structure.VerticalAlignPlane
import net.bewis09.bewisclient.drawable.renderables.components.structure.VerticalAlignScrollPlane
import net.bewis09.bewisclient.drawable.renderables.components.structure.VerticalGrid
import net.bewis09.bewisclient.drawable.renderables.impl.SelectCapeElement
import net.bewis09.bewisclient.server.Authorization
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.settings.structure.SidebarFeature
import net.bewis09.bewisclient.settings.types.StringMapSetting
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.renderite.logic.Color
import net.minecraft.client.multiplayer.PlayerInfo
import net.minecraft.world.item.Items
import java.net.URI
import java.security.MessageDigest
import java.util.*

object CosmeticLoader : SidebarFeature(createIdentifier("bewisclient", "cosmetics"), "Cosmetics"), EventEntrypoint {
    val allowedCosmetics = mutableListOf<CosmeticIdentifier>()
    val specialCosmetics = mutableListOf<CosmeticIdentifier>()
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
    val onlineSelected = create("online_selected", StringMapSetting())
    val timestamp = long("timestamp", 0)
    val elytra = boolean("elytra", true)

    var playerCosmeticsTimeStamp: Long? = null
    var hashedPlayerCosmetics = mapOf<String, OnePlayerData>()
    var playerCosmetics = hashMapOf<String, OnePlayerData?>()

    var lastCosmeticChange: Long = 0

    override fun getRenderable(): Renderable = Plane { x, y, width, height ->
        listOf(
            elytra.createRenderable(this, "elytra", "Apply cape to elytra", "Some capes include a unique texture for the elytra, which can be disabled here if desired.")(x, y, width, 22),
            getCosmeticGrid()(x, y + 27, width, height - 27)
        )
    }

    fun getStatus(identifier: CosmeticIdentifier): DownloadStatus {
        return status.getOrDefault(identifier, DownloadStatus.NOT_STARTED)
    }

    fun downloadCosmetic(cosmetic: CosmeticIdentifier, path: String, frames: Int) {
        catch {
            Util.nonCriticalIoPool().execute {
                val it = downloadWithOfflineFile(Constants.COSMETIC_URL + path, path) ?: run {
                    status[cosmetic] = DownloadStatus.FAILED
                    return@execute
                }
                status[cosmetic] = DownloadStatus.LOADED
                byteData[cosmetic] = it to frames
            }
        } ?: run {
            status[cosmetic] = DownloadStatus.FAILED
        }
    }

    fun loadCosmetic(cosmetic: CosmeticIdentifier, frames: Int) {
        if (getStatus(cosmetic) == DownloadStatus.NOT_STARTED) {
            status[cosmetic] = DownloadStatus.IN_PROGRESS

            downloadCosmetic(cosmetic, "${cosmetic.type}/${cosmetic.id}${if (frames > 1) ".gif" else ".png"}", frames)
        }
    }

    fun loadCosmeticFromByteArray(identifier: CosmeticIdentifier, data: ByteArray, frames: Int): Cosmetic? {
        return if (frames > 1) {
            catch { AnimatedCosmetic.create(identifier, data, frames) }
        } else {
            catch { StaticCosmetic.create(identifier, data) }
        }
    }

    override fun onInitializeClient() {
        CommonCosmeticLoader.afterLoadData {
            EventEntrypoint.onAllEventEntrypoints { CommonCosmeticLoader.metadata?.let { metadata -> it.onMetadataLoaded(metadata) } }
        }
    }

    override fun onClientTickStart() {
        if (
            Authorization.accessToken != null &&
            System.currentTimeMillis() - lastCosmeticChange > 1000 * 60 * 5 &&
            CommonCosmeticLoader.metadata?.url != null &&
            selected.get() != onlineSelected.get()
        ) {
            val url = CommonCosmeticLoader.metadata?.url ?: return
            lastCosmeticChange = System.currentTimeMillis()

            Util.nonCriticalIoPool().execute {
                catch {
                    val res = requestPost(
                        URI(url + "change").toURL(), """
                        {
                            "uuid": "${Authorization.env["uuid"] ?: client.gameProfile.id}",
                            "data": {
                                "cape": "${selected[CosmeticType.CAPE.id] ?: ""}"
                            },
                            "timestamp": ${timestamp.get()}
                        }
                    """.trimIndent().toByteArray(), headers = mapOf("Authorization" to (Authorization.accessToken ?: return@execute))
                    )

                    val resString = res?.body()?.decodeToString()

                    val s = resString?.let { catch { Gson().fromJson(it, OnePlayerData::class.java) } } ?: return@execute
                    onlineSelected["cape"] = s.cape
                    onlineSelected["hat"] = s.hat
                    onlineSelected["wing"] = s.wing
                    selected["cape"] = s.cape
                    selected["hat"] = s.hat
                    selected["wing"] = s.wing

                    timestamp.set(System.currentTimeMillis())
                }
            }
        }
    }

    fun loadSpecials(data: String?) {
        catch {
            val data = data?.let { catch { Gson().fromJson(it, JoinData::class.java) } }

            cosmeticData?.forEach {
                val identifier = it.getCosmetic() ?: return@forEach
                val isSpecial = data?.specials?.any { (id, type) -> id == identifier.id && type == identifier.type.id } == true
                if (isSpecial) specialCosmetics.add(identifier)
                if (it.default || isSpecial) allowedCosmetics.add(identifier)
                if (it.hasElytra) elytraCosmetics.add(identifier)
                loadCosmetic(identifier, it.frames)
            }

            data?.all_data?.let { loadData(it) }

            loadSelected(data?.selected)
        }
    }

    fun loadSelected(selected: Selected?) {
        if (selected == null) return

        this.onlineSelected["cape"] = selected.cape
        this.onlineSelected["hat"] = selected.hat
        this.onlineSelected["wing"] = selected.wing

        if (selected.timestamp > timestamp.get()) {
            timestamp.set(selected.timestamp)

            this.selected["cape"] = selected.cape
            this.selected["hat"] = selected.hat
            this.selected["wing"] = selected.wing
        }
    }

    fun loadData(data: String) {
        val parts = data.split("|")

        playerCosmeticsTimeStamp = parts.firstOrNull()?.toLong()

        val types = (parts.getOrNull(1) ?: return).split(";")
        val capeIndex = (types.indexOf("cape") + 1).let { if (it == 0) -1 else it }
        val hatIndex = (types.indexOf("hat") + 1).let { if (it == 0) -1 else it }
        val wingIndex = (types.indexOf("wing") + 1).let { if (it == 0) -1 else it }

        hashedPlayerCosmetics = parts.filterIndexed { i, _ -> i > 1 }.mapNotNull {
            val ps = it.split(";")
            val hash = ps.firstOrNull() ?: return@mapNotNull null

            return@mapNotNull hash to OnePlayerData(ps.getOrNull(capeIndex), ps.getOrNull(hatIndex), ps.getOrNull(wingIndex))
        }.toMap()
    }

    data class OnePlayerData(val cape: String?, val hat: String?, val wing: String?) {
        fun get(type: CosmeticType): String? = when (type) {
            CosmeticType.CAPE -> cape
            CosmeticType.HAT -> hat
            CosmeticType.WING -> wing
        }

        fun get(type: String): String? = when (type) {
            "cape" -> cape
            "hat" -> hat
            "wing" -> wing
            else -> null
        }
    }

    override fun onMinecraftClientInitFinished() {
        loadCosmeticByteData()
    }

    fun loadCosmeticByteData() {
        byteData.forEach {
            if (status[it.key] == DownloadStatus.LOADED) {
                status[it.key] = DownloadStatus.REGISTER_IN_PROGRESS
                val cosmetic = loadCosmeticFromByteArray(it.key, it.value.first, it.value.second) ?: return@forEach
                cosmetics[it.key] = cosmetic
                status[it.key] = DownloadStatus.COMPLETED
            }
        }
    }

    fun getEntityBySkinTextures(hashCode: Int): PlayerInfo? {
        val playerList = client.connection?.listedOnlinePlayers ?: return null
        return playerList.firstOrNull { it.skin.hashCode() == hashCode }
    }

    @Suppress("PropertyName")
    class JoinData(
        val specials: Array<SpecialEntry>,
        val all_data: String,
        val selected: Selected
    )

    class Selected(
        val cape: String?,
        val hat: String?,
        val wing: String?,
        val timestamp: Long
    )

    data class SpecialEntry(
        val id: String,
        val type: String,
        val signature: String
    )

    fun getCosmeticForPlayer(player: GameProfile, type: CosmeticType): Cosmetic? {
        val id = if (client.gameProfile.id == player.id)
            getCosmeticForSelf(type)
        else
            loadUnhashed(player.id.toString())?.get(type)?.let { CosmeticIdentifier(type, it) }

        if (id in elytraCosmetics && elytra.get()) return cosmetics[id]
        val elytraEquipped = client.level?.players()?.firstOrNull { it.gameProfile.id == player.id }?.inventory?.getItem(38)?.item == Items.ELYTRA && type == CosmeticType.CAPE
        if (elytraEquipped) return null
        return cosmetics[id]
    }

    fun getCosmeticForSelf(type: CosmeticType): CosmeticIdentifier? {
        val selected = this.selected[type.id]?.let { CosmeticIdentifier(type, it) }
        if (selected in allowedCosmetics) return selected
        return null
    }

    fun loadUnhashed(uuid: String): OnePlayerData? {
        return playerCosmetics.getOrElse(uuid) {
            val hash = sha256(uuid)
            val data = hashedPlayerCosmetics[hash]
            playerCosmetics[uuid] = data
            return@loadUnhashed data
        }
    }

    fun sha256(input: String): String {
        val bytes = input.toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return Base64.getEncoder().encodeToString(digest)
    }

    fun getCosmeticGrid(): Renderable {
        val categories = mutableListOf<Pair<String, List<CosmeticIdentifier>>>()

        categories.add("Special" to specialCosmetics)

        cosmeticData?.filter { it.default }?.groupBy { it.category ?: "_" }?.toList()?.sortedBy { it.first }?.forEach {
            categories.add(it.first.run { if (this == "_") "General" else this } to it.second.mapNotNull(CommonCosmeticLoader.CosmeticEntry::getCosmetic))
        }

        val setCategories = categories.filter { it.second.isNotEmpty() }

        val value = VerticalAlignScrollPlane(setCategories.mapIndexed { i, category ->
            VerticalAlignPlane(
                listOfNotNull(
                    TextElement(category.first.replaceFirstChar { it.uppercase() }.toText(), Color.WHITE, centered = true).setHeight(9),
                    VerticalGrid(category.second.mapNotNull { id ->
                        if (id.type == CosmeticType.CAPE && allowedCosmetics.contains(id) && cosmetics[id] != null) {
                            SelectCapeElement(id, cosmetics[id]!!)
                        } else null
                    }.let { a -> { _ -> a } }, 5, 65),
                    if (i != setCategories.size - 1) Rectangle { General.getTextThemeColor().withBrightness(0.3f) }.setHeight(1) else null
                ), 5
            )
        }, 5)

        return value
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