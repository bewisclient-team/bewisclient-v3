package net.bewis09.renderite.drawer

import net.bewis09.renderite.logic.Color

interface ScreenDrawingInterface<I, T, F> {
    fun translate(x: Float, y: Float)
    fun scale(x: Float, y: Float)
    fun rotate(angle: Float)
    fun push()
    fun pop()

    fun rotateDegrees(angle: Float) = rotate(Math.toRadians(angle.toDouble()).toFloat())
    fun applyAlpha(color: Color): Int = (getCurrentColorModifier() * color).argb

    class AfterDraw(val layer: Int, val func: () -> Unit)

    var overwrittenFont: F
    val colorStack: MutableList<Color>
    val afterDrawStack: HashMap<String, AfterDraw>

    fun pushAlpha(alpha: Float) = colorStack.add(Color(1f, 1f, 1f, alpha))

    fun pushColor(r: Float, g: Float, b: Float, a: Float) = colorStack.add(Color(r, g, b, a))

    fun darken(brightness: Float) = pushColor(brightness, brightness, brightness, 1f)

    fun popColor(): Color = if (colorStack.isNotEmpty()) {
        colorStack.removeLast()
    } else {
        Color.WHITE
    }

    fun getCurrentColorModifier(): Color = colorStack.reduceOrNull { acc, alpha ->
        acc * alpha
    } ?: Color.WHITE

    fun setFont(font: F) {
        this.overwrittenFont = font
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

    fun enableScissors(x: Int, y: Int, width: Int, height: Int)

    fun disableScissors()

    fun enableScissors(x: Int, y: Int, width: Int, height: Int, func: () -> Unit) {
        enableScissors(x, y, width, height)
        func()
        disableScissors()
    }

    fun scissorContains(x: Int, y: Int): Boolean

    fun pointerIfWithin(x: Int, y: Int, width: Int, height: Int, mouseX: Int, mouseY: Int) {
        if (isMouseOver(mouseX, mouseY, x, y, width, height))
            setCursorPointer()
    }

    fun isMouseOver(mouseX: Int, mouseY: Int, x: Int, y: Int, width: Int, height: Int): Boolean {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height
    }

    fun setCursorPointer()
}

inline fun ScreenDrawingInterface<*, *, *>.onNewLayer(apply: () -> Unit, transform: () -> Unit) {
    push()
    transform()
    apply()
    pop()
}

inline fun ScreenDrawingInterface<*, *, *>.transform(translateX: Float, translateY: Float, scale: Float, func: () -> Unit) = transform(translateX, translateY, scale, scale, func)

inline fun ScreenDrawingInterface<*, *, *>.transform(translateX: Float, translateY: Float, scaleX: Float, scaleY: Float, func: () -> Unit) = onNewLayer(func) {
    translate(translateX, translateY)
    scale(scaleX, scaleY)
}

inline fun ScreenDrawingInterface<*, *, *>.translate(x: Float, y: Float, func: () -> Unit) = onNewLayer(func) { translate(x, y) }

inline fun ScreenDrawingInterface<*, *, *>.scale(x: Float, y: Float, func: () -> Unit) = onNewLayer(func) { scale(x, y) }

inline fun ScreenDrawingInterface<*, *, *>.pushColor(r: Float, g: Float, b: Float, a: Float, func: () -> Unit) {
    pushColor(r, g, b, a)
    func()
    popColor()
}

inline fun ScreenDrawingInterface<*, *, *>.pushAlpha(a: Float, func: () -> Unit) = pushColor(1f, 1f, 1f, a, func)

inline fun ScreenDrawingInterface<*, *, *>.darken(brightness: Float, func: () -> Unit) = pushColor(brightness, brightness, brightness, 1f, func)