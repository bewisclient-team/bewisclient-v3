package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.logic.color.Color
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier

/**
 * A class representing a screen drawing context in Bewisclient. This class is used to encapsulate
 * the drawing context
 */
@Suppress("Unused")
class ScreenDrawing(override val drawContext: DrawContext, override val textRenderer: TextRenderer) : TextDrawing, RoundedDrawing, ItemDrawing {
    override var overwrittenFont: Identifier = ScreenDrawingInterface.DEFAULT_FONT
    override val afterDrawStack: HashMap<String, ScreenDrawingInterface.AfterDraw> = hashMapOf()
    override val colorStack: MutableList<Color> = mutableListOf()

    fun copy(): ScreenDrawing = ScreenDrawing(drawContext, textRenderer)
}