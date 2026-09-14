// @VersionReplacement

package net.bewis09.bewisclient.game

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.common.IndependentResourceMetadataSerializer
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.util.logic.ClientInterface
import net.minecraft.server.packs.PackLocationInfo
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.PackSelectionConfig
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.repository.Pack
import net.minecraft.server.packs.repository.Pack.ResourcesSupplier
import net.minecraft.server.packs.repository.PackCompatibility
import net.minecraft.server.packs.repository.PackSource
import net.minecraft.server.packs.resources.IoSupplier
import net.minecraft.world.flag.FeatureFlagSet
import java.io.InputStream
import java.util.*
import kotlin.jvm.optionals.getOrNull

object BewisclientResourcePack : PackResources, ClientInterface {
    val bewisclientResourcesTitle = Translation("menu.resource_pack.bewisclient_resource", "Bewisclient Custom Resources")
    val bewisclientResourcesDescription = Translation("menu.resource_pack.bewisclient_resource_description", "Features can be enabled in the Bewisclient settings")

    val packInfo = PackLocationInfo(
        "bewisclient_resources",
        bewisclientResourcesTitle(),
        PackSource.DEFAULT,
        Optional.empty()
    )

    val metadata = Pack.Metadata(
        bewisclientResourcesDescription(),
        PackCompatibility.COMPATIBLE,
        FeatureFlagSet.of(),
        mutableListOf()
    )

    val pack = Pack(
        packInfo,
        object : ResourcesSupplier {
            // @[26.2] openPrimary @[] openMetadata
            override fun /*[@]*/openMetadata/*[!@]*/(info: PackLocationInfo) = BewisclientResourcePack
            // @[26.2] openFull @[] openResources
            override fun /*[@]*/openResources/*[!@]*/(info: PackLocationInfo, metadata: Pack.Metadata) =
                // @[26.2] openPrimary(info) @[] listOf(openMetadata(info) as PackResources).stream()
                /*[@]*/listOf(openMetadata(info) as PackResources).stream()/*[!@]*/
        },
        metadata,
        PackSelectionConfig(true, Pack.Position.TOP, true)
    )

    override fun getRootResource(vararg strings: String): IoSupplier<InputStream>? {
        if (strings.contentEquals(arrayOf("pack.png"))) {
            return IoSupplier { client.resourceManager.getResource(createIdentifier("bewisclient", "icon.png")).getOrNull()?.open()!! }
        }
        return null
    }

    override fun listResources(packType: PackType, string: String, string2: String, resourceOutput: PackResources.ResourceOutput) {}

    override fun getResource(packType: PackType, identifier: Identifier): IoSupplier<InputStream>? {
        if (packType != PackType.CLIENT_RESOURCES) return null

        APIEntrypointLoader.mapEntrypoint { it.getCustomResourceProviders() }.forEach { providers ->
            providers.forEach { provider -> provider.provideResources(identifier)?.let { return it } }
        }

        return null
    }

    override fun <T : Any> getMetadataSection(metadataSectionType: IndependentResourceMetadataSerializer<T>): T? = null

    override fun location(): PackLocationInfo = packInfo

    override fun getNamespaces(type: PackType): Set<String> = setOf("bewisclient", "minecraft")

    override fun close() {}

    interface CustomResourceProvider {
        fun provideResources(id: Identifier): IoSupplier<InputStream>?
    }
}