package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier

object CoordinatesWidget: LineWidget() {
    var colorCodeBiome = BooleanSetting(false)
    var showBiome = BooleanSetting(false)

    init {
        create("color_code_biome", colorCodeBiome)
        create("show_biome", showBiome)
    }

    override fun hasMultipleLines(): Boolean = true

    val coordinatesWidgetTranslation = Translation("widget.coordinates_widget.name", "Coordinates Widget")
    val coordinatesWidgetDescription = Translation("widget.coordinates_widget.description", "Displays your current coordinates in the world")

    override fun getTranslation(): Translation = coordinatesWidgetTranslation
    override fun getDescription(): Translation = coordinatesWidgetDescription

    override fun getLines(): List<String> = listOf(
        "X: ${MinecraftClient.getInstance().cameraEntity?.blockPos?.x ?: 0}",
        "Y: ${MinecraftClient.getInstance().cameraEntity?.blockPos?.y ?: 0}",
        "Z: ${MinecraftClient.getInstance().cameraEntity?.blockPos?.z ?: 0}",
        if (showBiome.get()) BiomeWidget.getText(colorCodeBiome.get()) else null,
    ).filter { it != null }.map { it!! }

    override fun defaultPosition(): WidgetPosition = SidedPosition(5,5, SidedPosition.TransformerType.END, SidedPosition.TransformerType.START)

    override fun getId(): Identifier = Identifier.of("bewisclient", "coordinates_widget")

    override fun getWidth(): Int = if (showBiome.get()) 140 else 100

    override fun isCentered(): Boolean = false

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(showBiome.createRenderable("widget.coordinates_widget.show_biome", "Show Biome"))
        list.add(colorCodeBiome.createRenderable("widget.coordinates_widget.color_code_biome", "Color Code Biome"))
        super.appendSettingsRenderables(list)
    }
}