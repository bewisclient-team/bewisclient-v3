package net.bewis09.renderite

import net.bewis09.renderite.drawer.RenderiteDrawer

abstract class RenderiteElement<S: RenderiteDrawer<*, *, *>>(
    val minWidth: Int = 0,
    val minHeight: Int = 0,
    widthProvider: (RenderiteElement<S>.() -> Int)? = null,
    heightProvider: (RenderiteElement<S>.() -> Int)? = null
) {
    protected var internalX: Int = 0
    protected var internalY: Int = 0
    protected var internalWidth: Int = 0
    protected var internalHeight: Int = 0

    val widthProvider = widthProvider ?: { this.internalWidth }
    val heightProvider = heightProvider ?: { this.internalHeight }

    val x: Int
        get() = internalX
    val y: Int
        get() = internalY
    val width: Int
        get() = this.widthProvider().coerceAtLeast(minWidth)
    val height: Int
        get() = this.heightProvider().coerceAtLeast(minHeight)
    val x2: Int
        get() = internalX + width
    val y2: Int
        get() = internalY + height
    val centerX: Int
        get() = internalX + width / 2
    val centerY: Int
        get() = internalY + height / 2
    val exactCenterX: Float
        get() = internalX + width / 2f
    val exactCenterY: Float
        get() = internalY + height / 2f

    val renderables = mutableListOf<RenderiteElement<S>>()

    var selectedElement: RenderiteElement<S>? = null

    abstract fun render(screenDrawing: S, mouseX: Int, mouseY: Int)

    /**
     * Renders all the renderables in this Renderable.
     * Should be called at some point in the rendering process.
     */
    open fun renderRenderables(screenDrawing: S, mouseX: Int, mouseY: Int) {
        ArrayList(renderables).forEach { it.render(screenDrawing, mouseX, mouseY) }
    }

    fun <T : RenderiteElement<S>> addRenderable(renderable: T): T = renderable.also { renderables.add(it) }

    fun resize() {
        renderables.clear()
        init()
        ArrayList(renderables).forEach { it.resize() }
    }

    fun setX(x: Int): RenderiteElement<S> {
        if (x == this.x) return this

        this.internalX = x
        resize()
        return this
    }

    fun setY(y: Int): RenderiteElement<S> {
        if (y == this.y) return this

        this.internalY = y
        resize()
        return this
    }

    fun setPosition(x: Int, y: Int): RenderiteElement<S> {
        if (x == this.x && y == this.y) return this

        this.internalX = x
        this.internalY = y
        resize()
        return this
    }

    fun setSize(width: Int, height: Int): RenderiteElement<S> {
        if (width < 0) throw IllegalArgumentException("Width cannot be negative")
        if (height < 0) throw IllegalArgumentException("Height cannot be negative")
        if (width == this.width && height == this.height) return this

        this.internalWidth = width
        this.internalHeight = height
        resize()
        return this
    }

    fun setWidth(width: Int): RenderiteElement<S> {
        if (width < 0) throw IllegalArgumentException("Width cannot be negative")
        if (width == this.width) return this

        this.internalWidth = width
        resize()
        return this
    }

    fun setHeight(height: Int): RenderiteElement<S> {
        if (height < 0) throw IllegalArgumentException("Height cannot be negative")
        if (height == this.height) return this

        this.internalHeight = height
        resize()
        return this
    }

    fun setBounds(x: Int, y: Int, width: Int, height: Int): RenderiteElement<S> {
        if (x == this.x && y == this.y && width == this.width && height == this.height) return this

        if (width < 0) throw IllegalArgumentException("Width cannot be negative")
        if (height < 0) throw IllegalArgumentException("Height cannot be negative")

        this.internalX = x
        this.internalY = y

        this.internalWidth = width
        this.internalHeight = height

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

    operator fun invoke(x: Int, y: Int, width: Int, height: Int): RenderiteElement<S> {
        setBounds(x, y, width, height)
        return this
    }

    fun isMouseOver(mouseX: Int, mouseY: Int, x: Int, y: Int, width: Int, height: Int): Boolean {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height
    }
}