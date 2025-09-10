package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.logic.BewisclientInterface
import net.bewis09.bewisclient.logic.color.createColor
import net.bewis09.bewisclient.logic.color.times
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Style
import net.minecraft.text.StyleSpriteSource
import net.minecraft.util.Identifier
import java.awt.Color

interface ScreenDrawingInterface : BewisclientInterface {
    val drawContext: DrawContext
    val textRenderer: TextRenderer

    /**
     * Translates the drawing context by the specified x and y offsets.
     *
     * @param x The x offset to translate the context by.
     * @param y The y offset to translate the context by.
     */
    fun translate(x: Float, y: Float) {
        drawContext.matrices.translate(x, y)
    }

    /**
     * Scales the drawing context by the specified x and y factors.
     *
     * @param x The x factor to scale the context by.
     * @param y The y factor to scale the context by.
     */
    fun scale(x: Float, y: Float) {
        drawContext.matrices.scale(x, y)
    }

    /**
     * Rotates the drawing context by the specified angle in degrees.
     *
     * @param angle The angle in degrees to rotate the context by.
     */
    fun rotateDegrees(angle: Float) {
        drawContext.matrices.rotate(Math.toRadians(angle.toDouble()).toFloat())
    }

    /**
     * Rotates the drawing context by the specified angle in radians.
     *
     * @param angle The angle in radians to rotate the context by.
     */
    fun rotate(angle: Float) {
        drawContext.matrices.rotate(angle)
    }

    /**
     * Pushes a new matrix onto the drawing context's matrix stack. This is used to save the current
     * transformation state so that it can be restored later.
     */
    fun push() {
        drawContext.matrices.pushMatrix()
    }

    /**
     * Pops the last matrix from the drawing context's matrix stack. This restores the previous
     * transformation state that was saved by a push operation.
     */
    fun pop() {
        drawContext.matrices.popMatrix()
    }

    fun applyAlpha(color: Color): Int {
        return (getCurrentColorModifier() * color).rgb
    }

    class AfterDraw(val layer: Int, val func: () -> Unit)

    var style: Style
    val colorStack: MutableList<Color>
    val afterDrawStack: HashMap<String, AfterDraw>

    fun pushAlpha(alpha: Float) {
        colorStack.add(createColor(1f, 1f, 1f, alpha))
    }

    fun pushColor(r: Float, g: Float, b: Float, a: Float) {
        colorStack.add(createColor(r, g, b, a))
    }

    fun popColor(): Color {
        return if (colorStack.isNotEmpty()) {
            colorStack.removeLast()
        } else {
            Color.WHITE
        }
    }

    fun getCurrentColorModifier(): Color {
        return colorStack.reduceOrNull { acc, alpha ->
            acc * alpha
        } ?: Color.WHITE
    }

    fun setBewisclientFont() {
        setFont(Identifier.of("bewisclient", "screen"))
    }

    fun setFont(font: Identifier) {
        style = Style.EMPTY.withFont(StyleSpriteSource.Font(font))
    }

    fun defaultFont() {
        style = Style.EMPTY
    }

    fun afterDraw(id: String, func: () -> Unit, layer: Int = 0) {
        afterDrawStack[id] = AfterDraw(layer, func)
    }

    fun runAfterDraw() {
        for (function in afterDrawStack.values.sortedBy { it.layer }) {
            function.func()
        }
    }

    fun enableScissors(x: Int, y: Int, width: Int, height: Int) {
        drawContext.enableScissor(x, y, x + width, y + height)
    }

    fun disableScissors() {
        drawContext.disableScissor()
    }
}