package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.catch
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome

object BiomeWidget: LineWidget() {
    val unknownBiome = Translation("widget.biome_widget.unknown_biome", "Unknown Biome")

    override fun getLines(): List<String> = listOf(
        catch { getText() } ?: unknownBiome.getTranslatedString()
    )

    override fun defaultPosition(): WidgetPosition = SidedPosition(5, 5, SidedPosition.TransformerType.START, SidedPosition.TransformerType.END)

    override fun getId(): Identifier = Identifier.of("bewisclient", "biome_widget")

    override fun getWidth(): Int = 120

    private fun getBiomeString(biome: RegistryEntry<Biome>?): String {
        return biome?.keyOrValue?.map({ biomeKey: RegistryKey<Biome> -> biomeKey.value.toString() }, { b: Biome -> "[unregistered $b]" }) ?: unknownBiome.getTranslatedString()
    }

    fun getText(): String {
        return Text.translatable(Identifier.of(MinecraftClient.getInstance().world?.getBiome(MinecraftClient.getInstance().cameraEntity?.blockPos
            ?: BlockPos(0, 0, 0)
        )?.let { getBiomeString(it) }).toTranslationKey("biome")).string
    }
}