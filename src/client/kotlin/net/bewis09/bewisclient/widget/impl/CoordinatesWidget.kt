package net.bewis09.bewisclient.widget.impl

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.widget.logic.SidedPosition
import net.bewis09.bewisclient.widget.logic.WidgetPosition
import net.bewis09.bewisclient.widget.types.LineWidget
import net.minecraft.network.chat.Component

object CoordinatesWidget : LineWidget(
    createIdentifier("bewisclient", "coordinates_widget"),
    "Coordinates Widget",
    "Displays your current coordinates in the world"
) {
    val colorCodeBiome = boolean("color_code_biome", true)
    val showBiome = boolean("show_biome", true)
    val showDirection = boolean("show_direction", false)
    val showCoordinateChange = boolean("show_coordinate_change", false)

    override fun getLines(): List<Component> = listOfNotNull(
        "X: ${client.cameraEntity?.onPos?.x ?: 137} ${getAdditionString(0)}".toText(),
        "Y: ${client.cameraEntity?.onPos?.y ?: 69}".toText(),
        "Z: ${client.cameraEntity?.onPos?.z ?: 420} ${getAdditionString(2)}".toText(),
        if (showBiome.get()) BiomeWidget.getText(colorCodeBiome.get()) else null,
    )

    val dirAdditions = listOf(
        "", "(-)", "(--)", "(-)", "", "(+)", "(++)", "(+)"
    )

    fun getAdditionString(correct: Int): String {
        if (!showCoordinateChange.get()) return ""

        return dirAdditions.getOrElse((getYawPart() - correct + 8) % 8) { "" }
    }

    fun getYawPart(): Int = client.cameraEntity?.yRot?.let { a ->
        (((a / 45 - 112.5).toInt()) % 8).let {
            if (it < 0) 8 + it else it
        }
    } ?: 1

    override fun defaultPosition(): WidgetPosition = SidedPosition(
        5, 5, SidedPosition.END, SidedPosition.START
    )

    override fun getMinimumWidth(): Int = if (showBiome.get()) 140 else 100

    override fun getMaximumWidth(): Int = if (showBiome.get()) 200 else 100

    override fun isCentered(): Boolean = false

    override fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        list.menuQuick(showBiome, "Show Biome")
        list.menuQuick(colorCodeBiome, "Color Code Biome", "Whether to color code the biome name")
        list.menuQuick(showDirection, "Show Direction")
        list.menuQuick(showCoordinateChange, "Show Coordinate Change", "Shows how your coordinates will change if you move forward")
        super.appendSettingsRenderables(list)
    }

    override fun render(screenDrawing: ScreenDrawing) {
        super.render(screenDrawing)
        if (showDirection.get()) {
            val direction = getCardinalDirection()
            val text = "- $direction -"
            if (shadow.get()) screenDrawing.drawRightAlignedTextWithShadow(
                text, getWidth() - paddingSize.get(), paddingSize.get(), textColor.get().getColor()
            )
            else screenDrawing.drawRightAlignedText(
                text, getWidth() - paddingSize.get(), paddingSize.get(), textColor.get().getColor()
            )
        }
    }

    fun getCardinalDirection(long: Boolean = false): String {
        return when (getYawPart()) {
            0 -> if (long) "South" else "S"
            1 -> if (long) "Southwest" else "SW"
            2 -> if (long) "West" else "W"
            3 -> if (long) "Northwest" else "NW"
            4 -> if (long) "North" else "N"
            5 -> if (long) "Northeast" else "NE"
            6 -> if (long) "East" else "E"
            7 -> if (long) "Southeast" else "SE"
            else -> "?"
        }
    }

    override fun getCustomWidgetDataPoints(): List<CustomWidget.WidgetStringData> = listOf(
        CustomWidget.WidgetStringData("x", "X-Coordinate", "The current x coordinate of the player", { (client.cameraEntity?.onPos?.x ?: 137).toString() }),
        CustomWidget.WidgetStringData("y", "Y-Coordinate", "The current y coordinate of the player", { (client.cameraEntity?.onPos?.y ?: 69).toString() }),
        CustomWidget.WidgetStringData("z", "Z-Coordinate", "The current z coordinate of the player", { (client.cameraEntity?.onPos?.z ?: 420).toString() }),
        CustomWidget.WidgetStringData("direction", "Direction", "The cardinal direction the player is facing", { (getCardinalDirection(it == "long")) }, "\"long\" for full cardinal direction name"),
    )
}
