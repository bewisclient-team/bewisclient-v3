package net.bewis09.bewisclient.impl.widget

import com.google.gson.Gson
import com.google.gson.JsonElement
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.bewis09.bewisclient.logic.TextColor
import net.bewis09.bewisclient.logic.catch
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
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
    var colorCodeBiome = boolean("color_code_biome", true)

    override val title = "Biome Widget"
    override val description = "Displays the current biome at your position."

    override fun onMinecraftClientInitFinished() {
        val resources = client.resourceManager.findAllResources(
            "bewisclient/biome_codes"
        ) { it.path.endsWith(".json") }

        resources.entries.forEach {
            it.value.forEach { resource ->
                val jsonElement = Gson().fromJson(resource.reader, JsonElement::class.java)

                if (jsonElement?.isJsonObject == true) {
                    val jsonObject = jsonElement.asJsonObject

                    jsonObject.keySet().forEach { key ->
                        val biomeCode = jsonObject.get(key)
                        if (biomeCode.isJsonPrimitive) {
                            TextColor.entries.firstOrNull { a -> a.name.lowercase() == biomeCode.asString }?.let { s ->
                                biomeCodes[Identifier.of(key)] = s.code
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
        5, 5, SidedPosition.START, SidedPosition.END
    )

    override fun getId(): Identifier = Identifier.of("bewisclient", "biome_widget")

    override fun getMinimumWidth(): Int = 140

    override fun getMaximumWidth(): Int = 200

    private fun getBiomeString(biome: RegistryEntry<Biome>?): String {
        return biome?.keyOrValue?.map({ biomeKey: RegistryKey<Biome> -> biomeKey.value.toString() }, { b: Biome -> "[unregistered $b]" }) ?: unknownBiome.getTranslatedString()
    }

    fun getText(colorCoded: Boolean): String {
        val biome = getBiomeID()

        return (if (colorCoded) biomeCodes[biome] else "") + Text.translatable(biome.toTranslationKey("biome")).string
    }

    fun getBiomeID(): Identifier {
        return (client.world?.getBiome(
            client.cameraEntity?.blockPos ?: BlockPos(0, 0, 0)
        ))?.let { Identifier.of(getBiomeString(it)) } ?: getBiomeByMonth()
    }

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(colorCodeBiome.createRenderable("widget.color_code_biome", "Color Code Biome"))
        super.appendSettingsRenderables(list)
    }

    override fun isEnabledByDefault(): Boolean = false

    fun getBiomeByMonth(): Identifier {
        return when (Calendar.getInstance().get(Calendar.MONTH)) {
            0 -> Identifier.of("minecraft:snowy_plains")
            1 -> Identifier.of("minecraft:ice_spikes")
            2 -> Identifier.of("minecraft:swamp")
            3 -> Identifier.of("minecraft:flower_forest")
            4 -> Identifier.of("minecraft:forest")
            5 -> Identifier.of("minecraft:plains")
            6 -> Identifier.of("minecraft:sunflower_plains")
            7 -> Identifier.of("minecraft:beach")
            8 -> Identifier.of("minecraft:wooded_badlands")
            9 -> Identifier.of("minecraft:dark_forest")
            10 -> Identifier.of("minecraft:old_growth_spruce_taiga")
            11 -> Identifier.of("minecraft:taiga")
            else -> Identifier.of("minecraft:plains")
        }
    }

    override fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf(
        CustomWidget.WidgetStringData("biome_name", "Biome Name", "The name of the biome you are currently in", { color -> getText(color == "colored") + "§r" }, "\"colored\" to color code the biome name"),
        CustomWidget.WidgetStringData("biome_id", "Biome ID", "The ID of the biome you are currently in", { color -> getBiomeID().let { (if (color == "colored") biomeCodes[it] else "") + it.toString() + "§r" } }, "\"colored\" to color code the biome name")
    )
}
