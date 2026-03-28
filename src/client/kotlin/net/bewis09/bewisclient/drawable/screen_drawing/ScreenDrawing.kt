package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.util.color.Color
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.Identifier

/**
 * A class representing a screen drawing context in Bewisclient. This class is used to encapsulate
 * the drawing context
 */
@Suppress("Unused")
class ScreenDrawing(override val guiGraphics: GuiGraphics, override val textRenderer: Font) : TextDrawing, RoundedDrawing, ItemDrawing {
    override var overwrittenFont: Identifier = ScreenDrawingInterface.DEFAULT_FONT
    override val afterDrawStack: HashMap<String, ScreenDrawingInterface.AfterDraw> = hashMapOf()
    override val colorStack: MutableList<Color> = mutableListOf()

    fun copy(): ScreenDrawing = ScreenDrawing(guiGraphics, textRenderer)
}