package net.bewis09.bewisclient.logic

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import java.util.concurrent.CompletableFuture

/**
 * The Data Generator entrypoint for Bewisclient.
 * This is mostly used for translations
 */
object BewisclientDataGenerator : DataGeneratorEntrypoint {
    val translations = hashMapOf<String, String>()

    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        EventEntrypoint.onAllEventEntrypoints { it.onDatagen() }

        val pack: FabricDataGenerator.Pack = fabricDataGenerator.createPack()

        pack.addProvider(::BewisclientEnglishLangProvider)
    }

    class BewisclientEnglishLangProvider(dataOutput: FabricDataOutput?, registryLookup: CompletableFuture<WrapperLookup?>?) : FabricLanguageProvider(dataOutput, "en_us", registryLookup) {
        override fun generateTranslations(wrapperLookup: WrapperLookup?, translationBuilder: TranslationBuilder?) {
            translations.forEach { (key, value) ->
                translationBuilder?.add(key, value)
            }
        }
    }
}

fun addTranslation(key: String, @Suppress("LocalVariableName") en_us: String) {
    if (key.isEmpty()) {
        throw IllegalArgumentException("Translation key cannot be empty")
    }

    if (en_us.isEmpty()) {
        throw IllegalArgumentException("Translation value cannot be empty")
    }

    BewisclientDataGenerator.translations["bewisclient.$key"] = en_us
}