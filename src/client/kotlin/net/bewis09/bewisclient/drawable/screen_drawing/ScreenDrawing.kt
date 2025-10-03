package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.core.DrawingCore
import net.bewis09.bewisclient.logic.color.Color
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Style

/**
 * A class representing a screen drawing context in Bewisclient. This class is used to encapsulate
 * the drawing context
 */
@Suppress("Unused")
class ScreenDrawing(override val drawingCore: DrawingCore) : TextDrawing, RoundedDrawing, ItemDrawing {
    constructor(drawContext: DrawContext, textRenderer: TextRenderer): this(DrawingCore(drawContext, textRenderer))

    override var style: Style = Style.EMPTY
    override val afterDrawStack: HashMap<String, ScreenDrawingInterface.AfterDraw> = hashMapOf()
    override val colorStack: MutableList<Color> = mutableListOf()

    fun copy(): ScreenDrawing = ScreenDrawing(drawingCore)
}