package net.bewis09.bewisclient.game

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.drawable.Translations
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.util.logic.BewisclientInterface
import net.minecraft.resource.InputSupplier
import net.minecraft.resource.ResourcePack
import net.minecraft.resource.ResourcePackCompatibility
import net.minecraft.resource.ResourcePackInfo
import net.minecraft.resource.ResourcePackProfile
import net.minecraft.resource.ResourcePackSource
import net.minecraft.resource.ResourceType
import net.minecraft.resource.featuretoggle.FeatureSet
import net.minecraft.resource.metadata.ResourceMetadataSerializer
import net.minecraft.util.Identifier
import java.io.InputStream
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

object BewisclientResourcePack : ResourcePack, BewisclientInterface {
    val packInfo = ResourcePackInfo(
        "bewisclient_resources",
        Translations.BEWISCLIENT_RESOURCES(),
        ResourcePackSource.NONE,
        Optional.empty()
    )

    val metadata = ResourcePackProfile.Metadata(
        Translations.BEWISCLIENT_RESOURCES_DESCRIPTION(),
        ResourcePackCompatibility.COMPATIBLE,
        FeatureSet.empty(),
        mutableListOf<String?>()
    )

    override fun openRoot(vararg segments: String?): InputSupplier<InputStream?>? {
        if (segments.contentEquals(arrayOf("pack.png"))) {
            return InputSupplier { client.resourceManager.getResource(createIdentifier("bewisclient", "icon.png")).getOrNull()?.inputStream }
        }
        return null
    }

    override fun open(type: ResourceType, id: Identifier): InputSupplier<InputStream?>? {
        if (type != ResourceType.CLIENT_RESOURCES) return null

        APIEntrypointLoader.mapEntrypoint { it.getCustomResourceProviders() }.forEach { providers ->
            providers.forEach { provider -> provider.provideResources(id)?.let { return it } }
        }

        return null
    }

    override fun findResources(type: ResourceType, namespace: String, prefix: String, consumer: ResourcePack.ResultConsumer) {}

    override fun getNamespaces(type: ResourceType?): Set<String?> {
        return setOf("bewisclient", "minecraft")
    }

    override fun <T> parseMetadata(metadataSerializer: ResourceMetadataSerializer<T>): T? {
        return null
    }

    override fun getInfo(): ResourcePackInfo {
        return packInfo
    }

    override fun close() {}

    interface CustomResourceProvider {
        fun provideResources(id: Identifier): InputSupplier<InputStream?>?
    }
}