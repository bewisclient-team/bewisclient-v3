package net.bewis09.renderite

import net.bewis09.renderite.drawer.RenderiteDrawer
import net.bewis09.renderite.drawer.pushColor
import net.bewis09.renderite.logic.Color

abstract class RenderiteElement<S: RenderiteDrawer<*, *, *>, P: RenderiteElement<S, P>>(val props: Props<P> = {}) {
    typealias Props<P> = P.() -> Unit

    companion object {
        operator fun <P> Props<P>.plus(p: Props<P>): Props<P> = { this@plus(); p() }
    }

    var minWidth: Int = 0
    var minHeight: Int = 0
    var maxWidth: Int = Int.MAX_VALUE
    var maxHeight: Int = Int.MAX_VALUE
    var widthProvider: (RenderiteElement<*, *>.() -> Int)? = null
    var heightProvider: (RenderiteElement<*, *>.() -> Int)? = null

    var shouldUsePointer = false
    var overflowVisible = false

    var colorModifier = { Color(1f, 1f, 1f, 1f) }

    val internalWidthProvider = { it: Int, s: RenderiteElement<*, *> -> widthProvider?.invoke(s) ?: it }
    val internalHeightProvider = { it: Int, s: RenderiteElement<*, *> -> heightProvider?.invoke(s) ?: it }

    var x: Int = 0
    var y: Int = 0
    var width: Int = 0
        get() = this.internalWidthProvider(field, this).coerceAtLeast(minWidth).coerceAtMost(maxWidth)
        set(value) = run { field = value }
    var height: Int = 0
        get() = this.internalHeightProvider(field, this).coerceAtLeast(minHeight).coerceAtMost(maxHeight)
        set(value) = run { field = value }
    val x2: Int
        get() = x + width
    val y2: Int
        get() = y + height
    val centerX: Int
        get() = x + width / 2
    val centerY: Int
        get() = y + height / 2
    val exactCenterX: Float
        get() = x + width / 2f
    val exactCenterY: Float
        get() = y + height / 2f

    val renderables = mutableListOf<RenderiteElement<S, *>>()

    var selectedElement: RenderiteElement<S, *>? = null

    open fun render(screenDrawing: S, mouseX: Int, mouseY: Int) {
        screenDrawing.push()
        if (!overflowVisible) {
            screenDrawing.enableScissors(x, y, width, height)
        }

        val color = colorModifier()
        screenDrawing.pushColor(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f) {
            this.renderLogic(screenDrawing, mouseX, mouseY)
            this.renderBackground(screenDrawing, mouseX, mouseY)
            this.renderElement(screenDrawing, mouseX, mouseY)
            this.renderRenderables(screenDrawing, mouseX, mouseY)
            this.renderAccessories(screenDrawing, mouseX, mouseY)
            this.cleanup(screenDrawing, mouseX, mouseY)
        }

        if (!overflowVisible) {
            screenDrawing.disableScissors()
        }
        screenDrawing.pop()

        if (shouldUsePointer) usePointer(screenDrawing, mouseX, mouseY)
    }

    open fun renderElement(screenDrawing: S, mouseX: Int, mouseY: Int) {}

    open fun renderBackground(screenDrawing: S, mouseX: Int, mouseY: Int) {}

    open fun renderLogic(screenDrawing: S, mouseX: Int, mouseY: Int) {}

    open fun renderAccessories(screenDrawing: S, mouseX: Int, mouseY: Int) {}

    open fun cleanup(screenDrawing: S, mouseX: Int, mouseY: Int) {}

    /**
     * Renders all the renderables in this Renderable.
     * Should be called at some point in the rendering process.
     */
    open fun renderRenderables(screenDrawing: S, mouseX: Int, mouseY: Int) {
        ArrayList(renderables).forEach { it.render(screenDrawing, mouseX, mouseY) }
//        renderables.forEach { screenDrawing.fill(it.x, it.y, it.width, it.height, Color.RED alpha 0.2f) }
    }

    fun <T : RenderiteElement<S, *>> addRenderable(renderable: T): T = renderable.also { renderables.add(it) }

    fun resize() {
        renderables.clear()
        init()
        ArrayList(renderables).forEach { it.resize() }
    }

    fun updateX(x: Int): RenderiteElement<S, P> {
        if (x == this.x) return this

        this.x = x
        resize()
        return this
    }

    fun updateY(y: Int): RenderiteElement<S, P> {
        if (y == this.y) return this

        this.y = y
        resize()
        return this
    }

    fun updatePosition(x: Int, y: Int): RenderiteElement<S, P> {
        if (x == this.x && y == this.y) return this

        this.x = x
        this.y = y
        resize()
        return this
    }

    fun updateSize(width: Int, height: Int): RenderiteElement<S, P> {
        if (width < 0) throw IllegalArgumentException("Width cannot be negative")
        if (height < 0) throw IllegalArgumentException("Height cannot be negative")
        if (width == this.width && height == this.height) return this

        this.width = width
        this.height = height
        resize()
        return this
    }

    fun updateWidth(width: Int): RenderiteElement<S, P> {
        if (width < 0) throw IllegalArgumentException("Width cannot be negative")
        if (width == this.width) return this

        this.width = width
        resize()
        return this
    }

    fun updateHeight(height: Int): RenderiteElement<S, P> {
        if (height < 0) throw IllegalArgumentException("Height cannot be negative")
        if (height == this.height) return this

        this.height = height
        resize()
        return this
    }

    fun setBounds(x: Int, y: Int, width: Int, height: Int): RenderiteElement<S, P> {
        if (x == this.x && y == this.y && width == this.width && height == this.height) return this

        if (width < 0) throw IllegalArgumentException("Width cannot be negative")
        if (height < 0) throw IllegalArgumentException("Height cannot be negative")

        this.x = x
        this.y = y

        this.width = width
        this.height = height

        resize()

        return this
    }

    open fun init() {

    }

    fun mouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!isMouseOver(mouseX, mouseY)) return false

        renderables.firstOrNull { it.isMouseOver(mouseX, mouseY) }?.also {
            selectedElement = it
            it.mouseClick(mouseX, mouseY, button).let { a ->
                if (a) return true
            }
        }

        selectedElement = null
        return onMouseClick(mouseX, mouseY, button)
    }

    fun mouseRelease(mouseX: Double, mouseY: Double, button: Int) {
        if (!isMouseOver(mouseX, mouseY)) return

        renderables.filter { it.isMouseOver(mouseX, mouseY) }.forEach { it.mouseRelease(mouseX, mouseY, button) }

        onMouseRelease(mouseX, mouseY, button)
    }

    fun mouseDrag(mouseX: Double, mouseY: Double, startX: Double, startY: Double, button: Int): Boolean {
        if (!isMouseOver(startX, startY)) return false

        renderables.firstOrNull { it.isMouseOver(startX, startY) }?.mouseDrag(mouseX, mouseY, startX, startY, button)?.let { if (it) return true }

        return onMouseDrag(mouseX, mouseY, startX, startY, button)
    }

    fun mouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        if (!isMouseOver(mouseX, mouseY)) return false

        renderables.firstOrNull { it.isMouseOver(mouseX, mouseY) }?.mouseScroll(mouseX, mouseY, horizontalAmount, verticalAmount)?.let { if (it) return true }

        return onMouseScroll(mouseX, mouseY, horizontalAmount, verticalAmount)
    }

    fun keyPress(key: Int, scanCode: Int, modifiers: Int): Boolean {
        selectedElement?.keyPress(key, scanCode, modifiers)?.let { if (it) return true }
        return onKeyPress(key, scanCode, modifiers)
    }

    fun keyRelease(key: Int, scanCode: Int, modifiers: Int): Boolean {
        selectedElement?.keyRelease(key, scanCode, modifiers)?.let { if (it) return true }
        return onKeyRelease(key, scanCode, modifiers)
    }

    fun charTyped(character: Char, modifiers: Int): Boolean {
        selectedElement?.charTyped(character, modifiers)?.let { if (it) return true }
        return onCharTyped(character, modifiers)
    }

    open fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean = false
    open fun onMouseRelease(mouseX: Double, mouseY: Double, button: Int) {}
    open fun onMouseDrag(mouseX: Double, mouseY: Double, startX: Double, startY: Double, button: Int): Boolean = false
    open fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean = false
    open fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean = false
    open fun onKeyRelease(key: Int, scanCode: Int, modifiers: Int): Boolean = false
    open fun onCharTyped(character: Char, modifiers: Int): Boolean = false

    fun isMouseOver(mouseX: Number, mouseY: Number): Boolean {
        return mouseX.toInt() >= this.x && mouseX.toInt() <= this.x2 && mouseY.toInt() >= this.y && mouseY.toInt() <= this.y2
    }

    fun usePointer(screenDrawing: S, mouseX: Number, mouseY: Number) = if (isMouseOver(mouseX, mouseY) && screenDrawing.scissorContains(mouseX.toInt(), mouseY.toInt())) screenDrawing.setCursorPointer() else Unit

    operator fun invoke(x: Int, y: Int, width: Int, height: Int): RenderiteElement<S, P> {
        setBounds(x, y, width, height)
        return this
    }

    fun isMouseOver(mouseX: Int, mouseY: Int, x: Int, y: Int, width: Int, height: Int): Boolean {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height
    }

    fun <T> require(p: P.() -> T?): T =
        @Suppress("UNCHECKED_CAST")
        p(this as P) ?: throw IllegalArgumentException("Required property is missing")
}