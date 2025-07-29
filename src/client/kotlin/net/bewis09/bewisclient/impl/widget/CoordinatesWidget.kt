package net.bewis09.bewisclient.impl.widget

import com.google.gson.JsonObject
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.catch
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier

object CoordinatesWidget: LineWidget() {
    var colorCodeBiome: Boolean? = null
    var showBiome: Boolean? = null

    val coordinatesWidgetTranslation = Translation("widget.coordinates_widget.name", "Coordinates Widget")
    val coordinatesWidgetDescription = Translation("widget.coordinates_widget.description", "Displays your current coordinates in the world")

    override fun getTranslation(): Translation = coordinatesWidgetTranslation
    override fun getDescription(): Translation = coordinatesWidgetDescription

    override fun getLines(): List<String> = listOf(
        "X: ${MinecraftClient.getInstance().cameraEntity?.blockPos?.x ?: 0}",
        "Y: ${MinecraftClient.getInstance().cameraEntity?.blockPos?.y ?: 0}",
        "Z: ${MinecraftClient.getInstance().cameraEntity?.blockPos?.z ?: 0}",
        if (showBiome == true) BiomeWidget.getText(colorCodeBiome == true) else null,
    ).filter { it != null }.map { it!! }

    override fun defaultPosition(): WidgetPosition = SidedPosition(5,5, SidedPosition.TransformerType.END, SidedPosition.TransformerType.START)

    override fun getId(): Identifier = Identifier.of("bewisclient", "coordinates_widget")

    override fun getWidth(): Int = if (showBiome == true) 120 else 100

    override fun isCentered(): Boolean = false

    override fun saveProperties(properties: JsonObject) {
        super.saveProperties(properties)

        colorCodeBiome?.let { properties.addProperty("color_code_biome", it) }
        showBiome?.let { properties.addProperty("show_biome", it) }
    }

    override fun loadProperties(properties: JsonObject) {
        super.loadProperties(properties)

        colorCodeBiome = catch { properties.get("color_code_biome")?.asBoolean }
        showBiome = catch { properties.get("show_biome")?.asBoolean }
    }
}