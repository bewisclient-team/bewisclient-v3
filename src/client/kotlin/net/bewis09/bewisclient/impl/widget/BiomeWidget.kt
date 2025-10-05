package net.bewis09.bewisclient.impl.widget

import com.google.gson.Gson
import com.google.gson.JsonElement
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.util.*
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import java.util.*

object BiomeWidget : LineWidget(createIdentifier("bewisclient", "biome_widget")), EventEntrypoint {
    val biomeCodes = hashMapOf<Identifier, String>()
    var colorCodeBiome = boolean("color_code_biome", true)

    override val title = "Biome Widget"
    override val description = "Displays the current biome at your position."

    override fun onResourcesReloaded() {
        biomeCodes.clear()

        val resources = util.findAllResources(
            "bewisclient/biome_codes"
        ) { it.path.endsWith(".json") }

        resources.entries.forEach {
            it.value.forEach { resource ->
                catch {
                    (Gson().fromJson(resource.reader, JsonElement::class.java).asJsonObject)?.let { jsonObject ->
                        jsonObject.keySet().forEach { key ->
                            val biomeCode = jsonObject.get(key)
                            if (biomeCode.isJsonPrimitive) {
                                biomeCodes[createIdentifier(key)] = biomeCode.asString
                            } else {
                                warn("Invalid biome code format for $key in ${it.key}")
                            }
                        }
                    }
                } ?: warn("Invalid biome code JSON format in ${it.key}")
            }
        }
    }

    override fun getLine() = catch { getText(colorCodeBiome.get()) } ?: "Error".toText()

    override fun defaultPosition(): WidgetPosition = SidedPosition(
        5, 5, SidedPosition.START, SidedPosition.END
    )

    override fun getMinimumWidth(): Int = 140

    override fun getMaximumWidth(): Int = 200

    fun getText(colorCoded: Boolean) = applyColor(Text.translatable(getBiomeID().toTranslationKey("biome")), colorCoded)

    fun applyColor(text: Text, colorCoded: Boolean): Text {
        if (!colorCoded) return text

        val biome = getBiomeID()
        val color = TextColor.parse(biomeCodes[biome] ?: return text)
        if (color.isSuccess) return text.setColor(color.getOrThrow().rgb)
        return text
    }

    fun getBiomeID(): Identifier {
        return if(util.isInWorld()) createIdentifier(getBiomeString() ?: "minecraft:plains") else getBiomeByMonth()
    }

    fun getBiomeString(): String? {
        return (MinecraftClient.getInstance().world?.getBiome(
            MinecraftClient.getInstance().cameraEntity?.blockPos ?: BlockPos(0, 0, 0)
        ))?.keyOrValue?.map({ biomeKey: RegistryKey<Biome> -> biomeKey.value.toString() }, null)
    }

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(colorCodeBiome.createRenderable("widget.color_code_biome", "Color Code Biome"))
        super.appendSettingsRenderables(list)
    }

    override fun isEnabledByDefault(): Boolean = false

    fun getBiomeByMonth(): Identifier {
        return when (Calendar.getInstance().get(Calendar.MONTH)) {
            0 -> createIdentifier("minecraft:snowy_plains")
            1 -> createIdentifier("minecraft:ice_spikes")
            2 -> createIdentifier("minecraft:swamp")
            3 -> createIdentifier("minecraft:flower_forest")
            4 -> createIdentifier("minecraft:forest")
            5 -> createIdentifier("minecraft:plains")
            6 -> createIdentifier("minecraft:sunflower_plains")
            7 -> createIdentifier("minecraft:beach")
            8 -> createIdentifier("minecraft:wooded_badlands")
            9 -> createIdentifier("minecraft:dark_forest")
            10 -> createIdentifier("minecraft:old_growth_spruce_taiga")
            11 -> createIdentifier("minecraft:taiga")
            else -> createIdentifier("minecraft:plains")
        }
    }

    override fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf(
        CustomWidget.WidgetStringData("biome_name", "Biome Name", "The name of the biome you are currently in", { color -> getText(color == "colored") }, "\"colored\" to color code the biome name"),
        CustomWidget.WidgetStringData("biome_id", "Biome ID", "The ID of the biome you are currently in", { color -> applyColor(getBiomeID().toString().toText(), color == "colored") }, "\"colored\" to color code the biome name")
    )
}
