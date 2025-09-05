package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.logic.BewisclientInterface
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Style
import net.minecraft.text.StyleSpriteSource
import net.minecraft.util.Identifier
import kotlin.math.roundToInt

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

    fun applyAlpha(color: Number): Int {
        return (getCurrentColorModifier() * Color(
            (color.toInt() shr 16 and 0xFF) / 255f, (color.toInt() shr 8 and 0xFF) / 255f, (color.toInt() and 0xFF) / 255f, (color.toInt() shr 24 and 0xFF) / 255f
        )).toInt()
    }

    class AfterDraw(val layer: Int, val func: () -> Unit)

    data class Color(val r: Float, val g: Float, val b: Float, val a: Float) {
        operator fun times(other: Color): Color {
            return Color(
                r * other.r, g * other.g, b * other.b, a * other.a
            )
        }

        fun toInt(): Int {
            return ((r * 255).roundToInt() shl 16) or ((g * 255).roundToInt() shl 8) or (b * 255).roundToInt() or ((a * 255).roundToInt() shl 24)
        }
    }

    var style: Style
    val colorStack: MutableList<Color>
    val afterDrawStack: HashMap<String, AfterDraw>

    fun pushAlpha(alpha: Float) {
        colorStack.add(Color(1f, 1f, 1f, alpha))
    }

    fun pushColor(r: Float, g: Float, b: Float, a: Float) {
        colorStack.add(Color(r, g, b, a))
    }

    fun popColor(): Color {
        return if (colorStack.isNotEmpty()) {
            colorStack.removeLast()
        } else {
            Color(1f, 1f, 1f, 1f)
        }
    }

    fun getCurrentColorModifier(): Color {
        return colorStack.reduceOrNull { acc, alpha ->
            acc * alpha
        } ?: Color(1f, 1f, 1f, 1f)
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