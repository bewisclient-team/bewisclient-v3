package net.bewis09.bewisclient.drawable.screen_drawing

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Style
import java.awt.Color

/**
 * A class representing a screen drawing context in Bewisclient. This class is used to encapsulate
 * the drawing context
 *
 * @param drawContext The DrawContext used for drawing operations. Using it directly is extremely
 * discouraged.
 */
@Suppress("Unused")
class ScreenDrawing(override val drawContext: DrawContext, override val textRenderer: TextRenderer) : TextDrawing, RoundedDrawing, ItemDrawing {
    override var style: Style = Style.EMPTY
    override val afterDrawStack = hashMapOf<String, ScreenDrawingInterface.AfterDraw>()
    override val colorStack = mutableListOf<Color>()
}