package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.core.DrawingCore
import net.bewis09.bewisclient.core.toStyleFont
import net.bewis09.bewisclient.logic.BewisclientInterface
import net.bewis09.bewisclient.logic.color.Color
import net.minecraft.text.Style
import net.minecraft.util.Identifier
import org.joml.Matrix3x2f
import org.joml.Matrix3x2fStack

interface ScreenDrawingInterface : BewisclientInterface {
    val core: DrawingCore

    /**
     * Translates the drawing context by the specified x and y offsets.
     *
     * @param x The x offset to translate the context by.
     * @param y The y offset to translate the context by.
     */
    fun translate(x: Float, y: Float): Matrix3x2f = core.translate(x, y)

    /**
     * Scales the drawing context by the specified x and y factors.
     *
     * @param x The x factor to scale the context by.
     * @param y The y factor to scale the context by.
     */
    fun scale(x: Float, y: Float): Matrix3x2f = core.scale(x, y)

    /**
     * Rotates the drawing context by the specified angle in degrees.
     *
     * @param angle The angle in degrees to rotate the context by.
     */
    fun rotateDegrees(angle: Float): Matrix3x2f = core.rotate(Math.toRadians(angle.toDouble()).toFloat())

    /**
     * Rotates the drawing context by the specified angle in radians.
     *
     * @param angle The angle in radians to rotate the context by.
     */
    fun rotate(angle: Float): Matrix3x2f = core.rotate(angle)

    /**
     * Pushes a new matrix onto the drawing context's matrix stack. This is used to save the current
     * transformation state so that it can be restored later.
     */
    fun push(): Matrix3x2fStack = core.push()

    /**
     * Pops the last matrix from the drawing context's matrix stack. This restores the previous
     * transformation state that was saved by a push operation.
     */
    fun pop(): Matrix3x2fStack = core.pop()

    fun applyAlpha(color: Color): Int = (getCurrentColorModifier() * color).argb

    class AfterDraw(val layer: Int, val func: () -> Unit)

    var style: Style
    val colorStack: MutableList<Color>
    val afterDrawStack: HashMap<String, AfterDraw>

    fun pushAlpha(alpha: Float) = colorStack.add(Color(1f, 1f, 1f, alpha))

    fun pushColor(r: Float, g: Float, b: Float, a: Float) = colorStack.add(Color(r, g, b, a))

    fun popColor(): Color = if (colorStack.isNotEmpty()) {
        colorStack.removeLast()
    } else {
        Color.WHITE
    }

    fun getCurrentColorModifier(): Color = colorStack.reduceOrNull { acc, alpha ->
        acc * alpha
    } ?: Color.WHITE

    fun setBewisclientFont() = setFont(Identifier.of("bewisclient", "screen"))

    fun setFont(font: Identifier) {
        style = font.toStyleFont()
    }

    fun defaultFont() {
        style = Style.EMPTY
    }

    fun afterDraw(id: String, func: () -> Unit, layer: Int = 0) {
        afterDrawStack[id] = AfterDraw(layer, func)
    }

    fun runAfterDraw() {
        for (function in afterDrawStack.values.sortedBy { it.layer }) {
            push()
            function.func()
            pop()
        }
    }

    fun enableScissors(x: Int, y: Int, width: Int, height: Int) = core.enableScissors(x, y, x + width, y + height)

    fun disableScissors() = core.disableScissors()

    fun scissorContains(x: Int, y: Int) = core.scissorContains(x, y)
}

inline fun ScreenDrawingInterface.onNewLayer(apply: () -> Unit, transform: () -> Unit) {
    push()
    transform()
    apply()
    pop()
}

inline fun ScreenDrawingInterface.transform(translateX: Float, translateY: Float, scale: Float, func: () -> Unit) = transform(translateX, translateY, scale, scale, func)

inline fun ScreenDrawingInterface.transform(translateX: Float, translateY: Float, scaleX: Float, scaleY: Float, func: () -> Unit) = onNewLayer(func) {
    translate(translateX, translateY)
    scale(scaleX, scaleY)
}

inline fun ScreenDrawingInterface.translate(x: Float, y: Float, func: () -> Unit) = onNewLayer(func) { translate(x, y) }

inline fun ScreenDrawingInterface.scale(x: Float, y: Float, func: () -> Unit) = onNewLayer(func) { scale(x, y) }