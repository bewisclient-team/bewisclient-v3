package net.bewis09.renderite.drawer

import net.bewis09.renderite.logic.Color

/**
 * A class representing a screen drawing context in Bewisclient. This class is used to encapsulate
 * the drawing context
 */
abstract class RenderiteDrawer<I, T, F>(override var overwrittenFont: F) : TextDrawing<I, T, F>, RoundedDrawing<I, T, F> {
    override val afterDrawStack: HashMap<String, ScreenDrawingInterface.AfterDraw> = hashMapOf()
    override val colorStack: MutableList<Color> = mutableListOf()
}