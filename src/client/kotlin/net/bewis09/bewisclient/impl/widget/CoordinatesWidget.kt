package net.bewis09.bewisclient.impl.widget

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.widget.BiomeWidget.biomeCodes
import net.bewis09.bewisclient.impl.widget.BiomeWidget.getBiomeByMonth
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object CoordinatesWidget : LineWidget() {
    val colorCodeBiome = boolean("color_code_biome", true)
    val showBiome = boolean("show_biome", true)
    val showDirection = boolean("show_direction", false)
    val showCoordinateChange = boolean("show_coordinate_change", false)

    override fun hasMultipleLines(): Boolean = true

    val coordinatesWidgetTranslation = Translation("widget.coordinates_widget.name", "Coordinates Widget")
    val coordinatesWidgetDescription = Translation(
        "widget.coordinates_widget.description", "Displays your current coordinates in the world"
    )

    override fun getTranslation(): Translation = coordinatesWidgetTranslation
    override fun getDescription(): Translation = coordinatesWidgetDescription

    override fun getLines(): List<String> = listOf(
        "X: ${client.cameraEntity?.blockPos?.x ?: 0} ${getAdditionString(0)}",
        "Y: ${client.cameraEntity?.blockPos?.y ?: 0}",
        "Z: ${client.cameraEntity?.blockPos?.z ?: 0} ${getAdditionString(2)}",
        if (showBiome.get()) BiomeWidget.getText(colorCodeBiome.get())
        else null,
    ).filter { it != null }.map { it!! }

    override fun getOutOfWorldLines(): List<String> = listOf(
        "X: 137 ${if (showCoordinateChange.get()) "(-)" else ""}",
        "Y: 69",
        "Z: 420 ${if (showCoordinateChange.get()) "(+)" else ""}",
        if (showBiome.get()) let {
            val biome = getBiomeByMonth()

            return@let (if (colorCodeBiome.get()) biomeCodes[biome]
            else "") + Text.translatable(biome.toTranslationKey("biome")).string
        }
        else null,
    ).filter { it != null }.map { it!! }

    fun getAdditionString(correct: Int): String {
        if (!showCoordinateChange.get()) return ""

        val rel = (getYawPart() - correct + 8) % 8

        return when (rel) {
            0 -> ""
            1 -> "(-)"
            2 -> "(--)"
            3 -> "(-)"
            4 -> ""
            5 -> "(+)"
            6 -> "(++)"
            7 -> "(+)"
            else -> ""
        }
    }

    fun getYawPart(): Int = (((client.player!!.yaw / 45 - 112.5).toInt()) % 8).let {
        if (it < 0) 8 + it else it
    }

    override fun defaultPosition(): WidgetPosition = SidedPosition(
        5, 5, SidedPosition.TransformerType.END, SidedPosition.TransformerType.START
    )

    override fun getId(): Identifier = Identifier.of("bewisclient", "coordinates_widget")

    override fun getMinimumWidth(): Int = if (showBiome.get()) 140 else 100

    override fun getMaximumWidth(): Int = if (showBiome.get()) 200 else 100

    override fun isCentered(): Boolean = false

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.add(showBiome.createRenderable("widget.coordinates_widget.show_biome", "Show Biome"))
        list.add(
            colorCodeBiome.createRenderable(
                "widget.coordinates_widget.color_code_biome", "Color Code Biome"
            )
        )
        list.add(
            showDirection.createRenderable(
                "widget.coordinates_widget.show_direction", "Show Direction"
            )
        )
        list.add(
            showCoordinateChange.createRenderable(
                "widget.coordinates_widget.show_coordinate_change", "Show Coordinate Change", "Shows how your coordinates will change if you move forward"
            )
        )
        super.appendSettingsRenderables(list)
    }

    override fun render(screenDrawing: ScreenDrawing) {
        super.render(screenDrawing)
        if (showDirection.get()) {
            val direction = if (client.player == null) "SW"
            else when (getYawPart()) {
                0 -> "S"
                1 -> "SW"
                2 -> "W"
                3 -> "NW"
                4 -> "N"
                5 -> "NE"
                6 -> "E"
                7 -> "SE"
                else -> "?"
            }
            val text = "- $direction -"
            if (shadow.get()) screenDrawing.drawRightAlignedTextWithShadow(
                text, getWidth() - paddingSize.get(), paddingSize.get(), textColor.get().getColor()
            )
            else screenDrawing.drawRightAlignedText(
                text, getWidth() - paddingSize.get(), paddingSize.get(), textColor.get().getColor()
            )
        }
    }
}
