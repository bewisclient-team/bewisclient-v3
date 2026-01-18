package net.bewis09.bewisclient.impl.pack

import net.bewis09.bewisclient.core.*
import net.bewis09.bewisclient.drawable.Translations
import net.bewis09.bewisclient.settings.Settings.Companion.gson
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.util.logic.BewisclientInterface
import net.minecraft.SharedConstants
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.apache.commons.io.output.ByteArrayOutputStream
import java.io.ByteArrayInputStream
import java.net.URI
import java.net.URLEncoder
import javax.imageio.ImageIO

object Modrinth : BewisclientInterface {
    val typeMaps = mutableMapOf<Pair<Type, String>, Pair<StartableFailureList<List<ListPack>>, Int?>>()
    val packCache = mutableMapOf<String, Pack?>()
    val versionCache = mutableMapOf<String, Map<String, Version>?>()

    val imageCache = mutableMapOf<URI, Triple<NativeImage, Identifier, Boolean>?>()

    @Suppress("PropertyName")
    data class ListPack(
        val project_id: String,
        val project_type: String,
        val slug: String,
        val author: String,
        val title: String,
        val description: String,
        val categories: List<String>,
        val display_categories: List<String>,
        val versions: List<String>,
        val downloads: Int,
        val follows: Int,
        val icon_url: String,
        val date_created: String,
        val date_modified: String,
        val latest_version: String,
        val license: String?,
        val client_side: String?,
        val server_side: String?,
        val gallery: List<String>,
        val featured_gallery: String,
        val color: Int,
    )

    @Suppress("PropertyName")
    data class Pack(
        val client_side: String,
        val server_side: String,
        val game_versions: List<String>,
        val id: String,
        val slug: String,
        val project_type: String,
        val team: String?,
        val organization: String?,
        val title: String,
        val description: String,
        val body: String,
        val body_url: String,
        val published: String,
        val updated: String,
        val approved: String,
        val queued: String,
        val status: String,
        val requested_status: String?,
        val moderator_message: String?,
        val licence: Licence?,
        val downloads: Int,
        val followers: Int,
        val categories: List<String>,
        val additional_categories: List<String>,
        val loaders: List<String>,
        val versions: List<String>,
        val icon_url: String?,
        val issues_url: String?,
        val source_url: String?,
        val wiki_url: String?,
        val discord_url: String?,
        val donations_urls: List<DonationUrl>?,
        val gallery: List<GalleryImage>,
        val color: Int,
        val thread_id: String?,
        val monetization_status: String
    )

    data class Licence(
        val id: String,
        val name: String,
        val url: String?
    )

    data class DonationUrl(
        val id: String,
        val platform: String,
        val url: String
    )

    @Suppress("PropertyName")
    data class GalleryImage(
        val url: String,
        val raw_url: String,
        val featured: Boolean,
        val title: String,
        val description: String?,
        val created: String,
        val ordering: Int
    )

    @Suppress("PropertyName")
    data class Version(
        val name: String,
        val version_number: String,
        val changelog: String,
        val dependencies: List<Dependency>,
        val game_versions: List<String>,
        val version_type: String,
        val loaders: List<String>,
        val featured: Boolean,
        val status: String,
        val requested_status: String?,
        val id: String,
        val project_id: String,
        val author_id: String,
        val date_published: String,
        val downloads: Int,
        val changelog_url: String?,
        val files: List<VersionFile>
    )

    @Suppress("PropertyName")
    data class Dependency(
        val version_id: String,
        val project_id: String,
        val file_name: String?,
        val dependency_type: String
    )

    @Suppress("PropertyName")
    data class VersionFile(
        val url: String,
        val filename: String,
        val primary: Boolean,
        val size: Int,
        val file_type: String,
        val hashes: Hashes
    )

    data class Hashes(
        val sha1: String,
        val sha256: String
    )

    @Suppress("PropertyName")
    data class ModrinthSearchResult(
        val hits: List<ListPack>,
        val offset: Int,
        val limit: Int,
        val total_hits: Int
    )

    enum class Type(val url: String, val text: Text) {
        RESOURCE_PACK("resourcepack", Translations.ADD_RESOURCE_PACK()),
        DATA_PACK("datapack", Translations.ADD_DATA_PACK()),
//        SHADER("shader")
    }

    fun getPageOfType(type: Type, page: Int, query: String): List<ListPack>? {
        loadPage(type, page, query)
        return typeMaps[type to query]?.first?.getOrDefault(page, null)?.first
    }

    fun loadPage(type: Type, page: Int, query: String) {
        if (typeMaps[type to query] == null) {
            typeMaps[type to query] = mutableMapOf<Int, Pair<List<ListPack>?, Boolean>>() to null
        }

        if (typeMaps[type to query]!!.first.containsKey(page)) {
            return
        }

        typeMaps[type to query]!!.first[page] = null to false

        downloadFile("https://api.modrinth.com/v2/search?query=${URLEncoder.encode(query.replace(Regex("&\\?="), ""), "UTF-8")}&facets=%5B%5B%22project_type:${type.url}%22%5D,%5B%22versions:${SharedConstants.getGameVersion().name}%22%5D%5D&limit=20&offset=${page * 20}") {
            val json = gson.fromJson(String(it), ModrinthSearchResult::class.java)
            if (typeMaps[type to query]!!.second == null) {
                typeMaps[type to query] = typeMaps[type to query]!!.first to json.total_hits
            }
            typeMaps[type to query]!!.first[page] = json.hits to true
        }
    }

    fun loadVersions(pack: Pack, onFinish: (( Map<String, Version>) -> Unit)? = null) {
        if (versionCache.containsKey(pack.slug)) {
            versionCache[pack.slug]?.let { onFinish?.invoke(it) }
            return
        }

        versionCache.putIfAbsent(pack.slug, null)

        downloadFile("https://api.modrinth.com/v2/versions?ids=%5B%22${pack.versions.joinToString("%22,%22")}%22%5D") {
            val json = gson.fromJson(String(it), Array<Version>::class.java)
            versionCache[pack.slug] = json.associateBy { v -> v.id }
            versionCache[pack.slug]?.let { p1 -> onFinish?.invoke(p1) }
        }
    }

    fun loadPack(slug: String, onFinish: ((Pack) -> Unit)? = null) {
        if (packCache.containsKey(slug)) {
            packCache[slug]?.let { onFinish?.invoke(it) }
            return
        }

        packCache.putIfAbsent(slug, null)

        downloadFile("https://api.modrinth.com/v2/project/$slug") {
            val json = gson.fromJson(String(it), Pack::class.java)
            packCache[slug] = json
            onFinish?.invoke(json)
        }
    }

    fun getImageByURL(uri: URI): Identifier? {
        if (imageCache.containsKey(uri)) {
            return imageCache[uri]?.let {
                if (it.third) it.second else {
                    MinecraftClient.getInstance().registerTexture(it.second, it.first)
                    imageCache[uri] = Triple(it.first, it.second, true)
                    it.second
                }
            }
        }

        imageCache[uri] = null
        downloadFile(uri) { success ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            ImageIO.write(ImageIO.read(ByteArrayInputStream(success)), "PNG", byteArrayOutputStream)
            val nativeImage = NativeImage.read(byteArrayOutputStream.toByteArray())
            val identifier = createIdentifier("bewisclient", "modrinth/${uri.path.hashCode()}")
            imageCache[uri] = Triple(nativeImage, identifier, false)
        }

        return null
    }
}

typealias StartableFailureList<T> = MutableMap<Int, Pair<T?, Boolean>>