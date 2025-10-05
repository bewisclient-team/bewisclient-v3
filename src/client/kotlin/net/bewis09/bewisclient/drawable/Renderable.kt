package net.bewis09.bewisclient.drawable

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.logic.BewisclientInterface

abstract class Renderable : BewisclientInterface {
    protected var internalX: Int
    protected var internalY: Int
    protected var internalWidth: Int
    protected var internalHeight: Int

    val x: Int
        get() = internalX
    val y: Int
        get() = internalY
    val width: Int
        get() = internalWidth
    val height: Int
        get() = internalHeight
    val x2: Int
        get() = internalX + internalWidth
    val y2: Int
        get() = internalY + internalHeight
    val centerX: Int
        get() = internalX + internalWidth / 2
    val centerY: Int
        get() = internalY + internalHeight / 2

    constructor(x: Int, y: Int, width: Int, height: Int) {
        this.internalX = x
        this.internalY = y
        this.internalWidth = width
        this.internalHeight = height
        this.renderables = mutableListOf<Renderable>()
    }

    constructor() : this(0, 0, 0, 0)

    val renderables: MutableList<Renderable>

    var selectedElement: Renderable? = null

    abstract fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int)

    /**
     * Renders all the renderables in this Renderable.
     * Should be called at some point in the rendering process.
     */
    open fun renderRenderables(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        ArrayList(renderables).forEach { it.render(screenDrawing, mouseX, mouseY) }
    }

    fun <T : Renderable> addRenderable(renderable: T): T = renderable.also { renderables.add(it) }

    fun resize() {
        renderables.clear()
        init()
        ArrayList(renderables).forEach { it.resize() }
    }

    fun setX(x: Int): Renderable {
        if (x == this.x) return this

        this.internalX = x
        resize()
        return this
    }

    fun setY(y: Int): Renderable {
        if (y == this.y) return this

        this.internalY = y
        resize()
        return this
    }

    fun setPosition(x: Int, y: Int): Renderable {
        if (x == this.x && y == this.y) return this

        this.internalX = x
        this.internalY = y
        resize()
        return this
    }

    fun setSize(width: Int, height: Int): Renderable {
        if (width < 0) throw IllegalArgumentException("Width cannot be negative")
        if (height < 0) throw IllegalArgumentException("Height cannot be negative")
        if (width == this.width && height == this.height) return this

        this.internalWidth = width
        this.internalHeight = height
        resize()
        return this
    }

    fun setWidth(width: Int): Renderable {
        if (width < 0) throw IllegalArgumentException("Width cannot be negative")
        if (width == this.width) return this

        this.internalWidth = width
        resize()
        return this
    }

    fun setHeight(height: Int): Renderable {
        if (height < 0) throw IllegalArgumentException("Height cannot be negative")
        if (height == this.height) return this

        this.internalHeight = height
        resize()
        return this
    }

    fun setBounds(x: Int, y: Int, width: Int, height: Int): Renderable {
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

    fun mouseRelease(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!isMouseOver(mouseX, mouseY)) return false

        renderables.firstOrNull { it.isMouseOver(mouseX, mouseY) }?.mouseRelease(mouseX, mouseY, button)?.let { if (it) return true }

        return onMouseRelease(mouseX, mouseY, button)
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
    open fun onMouseRelease(mouseX: Double, mouseY: Double, button: Int): Boolean = false
    open fun onMouseDrag(mouseX: Double, mouseY: Double, startX: Double, startY: Double, button: Int): Boolean = false
    open fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean = false
    open fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean = false
    open fun onKeyRelease(key: Int, scanCode: Int, modifiers: Int): Boolean = false
    open fun onCharTyped(character: Char, modifiers: Int): Boolean = false

    fun isMouseOver(mouseX: Double, mouseY: Double): Boolean {
        return mouseX >= this.x && mouseX <= this.x2 && mouseY >= this.y && mouseY <= this.y2
    }

    operator fun invoke(x: Int, y: Int, width: Int, height: Int): Renderable {
        setBounds(x, y, width, height)
        return this
    }
}

