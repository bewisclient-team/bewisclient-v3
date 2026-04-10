package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.util.logic.BewisclientInterface
import net.bewis09.bewisclient.version.*
import net.minecraft.client.gui.Font

interface ScreenDrawingInterface : BewisclientInterface {
    val guiGraphics: GuiGraphics
    val textRenderer: Font

    fun translate(x: Float, y: Float) = guiGraphics.translate(x, y)

    fun scale(x: Float, y: Float) = guiGraphics.scale(x, y)

    fun rotateDegrees(angle: Float) = rotate(Math.toRadians(angle.toDouble()).toFloat())

    fun rotate(angle: Float) = guiGraphics.rotate(angle)

    fun push() = guiGraphics.push()

    fun pop() = guiGraphics.pop()

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

    fun setDefaultFont() {
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

    fun enableScissors(x: Int, y: Int, width: Int, height: Int) = guiGraphics.enableScissor(x, y, x + width, y + height)

    fun disableScissors() = guiGraphics.disableScissor()

    fun scissorContains(x: Int, y: Int) = guiGraphics.containsPointInScissor(x, y)
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

inline fun ScreenDrawingInterface.pushColor(r: Float, g: Float, b: Float, a: Float, func: () -> Unit) {
    pushColor(r, g, b, a)
    func()
    popColor()
}

inline fun ScreenDrawingInterface.pushAlpha(a: Float, func: () -> Unit) = pushColor(1f, 1f, 1f, a, func)