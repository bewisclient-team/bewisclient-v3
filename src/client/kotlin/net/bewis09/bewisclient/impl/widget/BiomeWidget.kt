package net.bewis09.bewisclient.impl.widget

import com.google.gson.Gson
import com.google.gson.JsonElement
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.bewis09.bewisclient.logic.TextColors
import net.bewis09.bewisclient.logic.catch
import net.bewis09.bewisclient.settings.types.BooleanSetting
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
import java.util.*

object BiomeWidget : LineWidget(), EventEntrypoint {
    val unknownBiome = Translation("widget.biome_widget.unknown_biome", "Unknown Biome")

    val biomeCodes = hashMapOf<Identifier, String>()
    var colorCodeBiome = create("color_code_biome", BooleanSetting(true))

    val biomeWidgetTranslation = Translation("widget.biome_widget.name", "Biome Widget")
    val biomeWidgetDescription = Translation(
        "widget.biome_widget.description", "Displays the current biome at your position."
    )

    override fun getTranslation(): Translation = biomeWidgetTranslation
    override fun getDescription(): Translation = biomeWidgetDescription

    override fun onMinecraftClientInitFinished() {
        val resources = MinecraftClient.getInstance().resourceManager.findAllResources(
            "bewisclient/biome_codes"
        ) { it.path.endsWith(".json") }

        resources.entries.forEach {
            it.value.forEach { resource ->
                val jsonElement = Gson().fromJson(resource.reader, JsonElement::class.java)

                if (jsonElement.isJsonObject) {
                    val jsonObject = jsonElement.asJsonObject

                    jsonObject.keySet().forEach { key ->
                        val biomeCode = jsonObject.get(key)
                        if (biomeCode.isJsonPrimitive) {
                            TextColors.COLORS[biomeCode.asString]?.let { s ->
                                biomeCodes[Identifier.of(key)] = s
                            } ?: run {
                                warn(
                                    "Unknown biome color code: ${biomeCode.asString} in ${it.key}"
                                )
                            }
                        } else {
                            warn("Invalid biome code format for $key in ${it.key}")
                        }
                    }
                } else {
                    warn("Invalid biome code JSON format in ${it.key}")
                }
            }
        }
    }

    override fun getLines(): List<String> = listOf(catch { getText(colorCodeBiome.get()) } ?: unknownBiome.getTranslatedString())

    override fun defaultPosition(): WidgetPosition = SidedPosition(
        5, 5, SidedPosition.TransformerType.START, SidedPosition.TransformerType.END
    )

    override fun getId(): Identifier = Identifier.of("bewisclient", "biome_widget")

    override fun getWidth(): Int = 140

    private fun getBiomeString(biome: RegistryEntry<Biome>?): String {
        return biome?.keyOrValue?.map({ biomeKey: RegistryKey<Biome> -> biomeKey.value.toString() }, { b: Biome -> "[unregistered $b]" }) ?: unknownBiome.getTranslatedString()
    }

    fun getText(colorCoded: Boolean): String {
        val biome = Identifier.of(
            (MinecraftClient.getInstance().world?.getBiome(
                MinecraftClient.getInstance().cameraEntity?.blockPos ?: BlockPos(0, 0, 0)
            ))?.let { getBiomeString(it) })

        return (if (colorCoded) biomeCodes[biome] else "") + Text.translatable(biome.toTranslationKey("biome")).string
    }

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(colorCodeBiome.createRenderable("widget.color_code_biome", "Color Code Biome"))
        super.appendSettingsRenderables(list)
    }

    override fun isEnabledByDefault(): Boolean = false

    fun getBiomeByMonth(): Identifier {
        return when (Calendar.getInstance().get(Calendar.MONTH)) {
            0 -> Identifier.of("minecraft:snowy_tundra")
            1 -> Identifier.of("minecraft:ice_spikes")
            2 -> Identifier.of("minecraft:swamp")
            3 -> Identifier.of("minecraft:flower_forest")
            4 -> Identifier.of("minecraft:forest")
            5 -> Identifier.of("minecraft:plains")
            6 -> Identifier.of("minecraft:sunflower_plains")
            7 -> Identifier.of("minecraft:beach")
            8 -> Identifier.of("minecraft:wooded_hills")
            9 -> Identifier.of("minecraft:dark_forest")
            10 -> Identifier.of("minecraft:giant_tree_taiga")
            11 -> Identifier.of("minecraft:taiga")
            else -> Identifier.of("minecraft:plains")
        }
    }

    override fun getOutOfWorldLines(): List<String> {
        val biome = getBiomeByMonth()

        return listOf(
            (if (colorCodeBiome.get()) biomeCodes[biome] else "") + Text.translatable(biome.toTranslationKey("biome")).string
        )
    }
}
