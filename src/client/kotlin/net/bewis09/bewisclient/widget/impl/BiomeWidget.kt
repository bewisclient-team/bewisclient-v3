package net.bewis09.bewisclient.widget.impl

import com.google.gson.Gson
import com.google.gson.JsonElement
import net.bewis09.bewisclient.common.*
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor
import net.minecraft.server.packs.resources.Resource
import java.util.*
import kotlin.jvm.optionals.getOrNull

object BiomeWidget : LineWidget(
    createIdentifier("bewisclient", "biome_widget"),
    "Biome Widget",
    "Displays the current biome at your position."
), EventEntrypoint {
    val biomeCodes = hashMapOf<Identifier, String>()
    var colorCodeBiome = boolean("color_code_biome", true)
    val altBiome = catch {
        val resource = client.resourceManager.getResource(createIdentifier("bewisclient", "bewisclient/widget_alt_data/biomes.json")).getOrNull() ?: return@catch null
        val month = Calendar.getInstance().get(Calendar.MONTH)
        return@catch createIdentifier(Gson().fromJson(resource.openAsReader(), JsonElement::class.java).asJsonObject.get("monthly").asJsonArray[month].asString)
    } ?: createIdentifier("minecraft:plains")

    override fun onResourcesReloaded() {
        biomeCodes.clear()

        val resources = findAllResources(
            "bewisclient/biome_codes"
        ) { it.path.endsWith(".json") }

        resources.entries.forEach {
            it.value.forEach { resource: Resource ->
                catch {
                    (Gson().fromJson(resource.openAsReader(), JsonElement::class.java).asJsonObject)?.let { jsonObject ->
                        jsonObject.keySet().forEach { key ->
                            val biomeCode = jsonObject[key]
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

    fun getText(colorCoded: Boolean) = applyColor(Component.translatable(getBiomeID().toLanguageKey("biome")), colorCoded)

    fun applyColor(text: Component, colorCoded: Boolean): Component {
        if (!colorCoded) return text

        val biome = getBiomeID()
        val color = TextColor.parseColor(biomeCodes[biome] ?: return text)
        if (color.isSuccess) return text.setColor(color.getOrThrow().value)
        return text
    }

    fun getBiomeID(): Identifier {
        return if (isInWorld()) createIdentifier(getBiomeString() ?: "minecraft:plains") else altBiome
    }

    fun getBiomeString(): String? {
        return (client.level?.getBiome(
            client.cameraEntity?.onPos ?: BlockPos(0, 0, 0)
        ))?.unwrap()?.map({ biomeKey -> biomeKey.id().toString() }, null)
    }

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.menu(colorCodeBiome, "Color Code Biome", "Whether to color code the biome name")
        super.appendSettingsRenderables(list)
    }

    override fun isEnabledByDefault(): Boolean = false

    override fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf(
        CustomWidget.WidgetStringData("biome_name", "Biome Name", "The name of the biome you are currently in", { getText(false).string }),
        CustomWidget.WidgetStringData("biome_id", "Biome ID", "The ID of the biome you are currently in", { getBiomeID().toString() })
    )
}
