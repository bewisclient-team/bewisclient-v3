package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.core.pop
import net.bewis09.bewisclient.core.push
import net.bewis09.bewisclient.core.rotate
import net.bewis09.bewisclient.core.scale
import net.bewis09.bewisclient.core.translate
import net.bewis09.bewisclient.logic.BewisclientInterface
import net.bewis09.bewisclient.logic.color.Color
import net.bewis09.bewisclient.logic.createIdentifier
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier

interface ScreenDrawingInterface : BewisclientInterface {
    val drawContext: DrawContext
    val textRenderer: TextRenderer

    /**
     * Translates the drawing context by the specified x and y offsets.
     *
     * @param x The x offset to net.bewis09.bewisclient.core.translate the context by.
     * @param y The y offset to net.bewis09.bewisclient.core.translate the context by.
     */
    fun translate(x: Float, y: Float) = drawContext.translate(x, y)

    /**
     * Scales the drawing context by the specified x and y factors.
     *
     * @param x The x factor to net.bewis09.bewisclient.core.scale the context by.
     * @param y The y factor to net.bewis09.bewisclient.core.scale the context by.
     */
    fun scale(x: Float, y: Float) = drawContext.scale(x, y)

    /**
     * Rotates the drawing context by the specified angle in degrees.
     *
     * @param angle The angle in degrees to net.bewis09.bewisclient.core.rotate the context by.
     */
    fun rotateDegrees(angle: Float) = drawContext.rotate(Math.toRadians(angle.toDouble()).toFloat())

    /**
     * Rotates the drawing context by the specified angle in radians.
     *
     * @param angle The angle in radians to net.bewis09.bewisclient.core.rotate the context by.
     */
    fun rotate(angle: Float) = drawContext.rotate(angle)

    /**
     * Pushes a new matrix onto the drawing context's matrix stack. This is used to save the current
     * transformation state so that it can be restored later.
     */
    fun push() = drawContext.push()

    /**
     * Pops the last matrix from the drawing context's matrix stack. This restores the previous
     * transformation state that was saved by a net.bewis09.bewisclient.core.push operation.
     */
    fun pop() = drawContext.pop()

    fun applyAlpha(color: Color): Int = (getCurrentColorModifier() * color).argb

    class AfterDraw(val layer: Int, val func: () -> Unit)

    var overwrittenFont: Identifier
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

    fun setBewisclientFont() = setFont(BEWISCLIENT_FONT)

    companion object {
        val DEFAULT_FONT: Identifier = createIdentifier("minecraft", "default")
        val BEWISCLIENT_FONT: Identifier = createIdentifier("bewisclient", "screen")
    }

    fun setFont(font: Identifier) {
        this.overwrittenFont = font
    }

    fun defaultFont() {
        this.overwrittenFont = DEFAULT_FONT
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

    fun enableScissors(x: Int, y: Int, width: Int, height: Int) = drawContext.enableScissor(x, y, x + width, y + height)

    fun disableScissors() = drawContext.disableScissor()

    fun scissorContains(x: Int, y: Int) = drawContext.scissorContains(x, y)
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