package net.bewis09.bewisclient.drawable

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.logic.BewisclientInterface

abstract class Renderable : BewisclientInterface {
    protected var x: UInt
    protected var y: UInt
    protected var width: UInt
    protected var height: UInt

    constructor(x: Int, y: Int, width: Int, height: Int) {
        this.x = x.toUInt()
        this.y = y.toUInt()
        this.width = width.toUInt()
        this.height = height.toUInt()
        this.renderables = mutableListOf<Renderable>()
    }

    constructor() : this(0, 0, 0, 0)

    val renderables: MutableList<Renderable>

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
        if (x == getX()) return this

        this.x = x.toUInt()
        resize()
        return this
    }

    fun setY(y: Int): Renderable {
        if (y == getY()) return this

        this.y = y.toUInt()
        resize()
        return this
    }

    fun setPosition(x: Int, y: Int): Renderable {
        if (x == getX() && y == getY()) return this

        this.x = x.toUInt()
        this.y = y.toUInt()
        resize()
        return this
    }

    fun getX(): Int = x.toInt()
    fun getY(): Int = y.toInt()
    fun getWidth(): Int = width.toInt()
    fun getHeight(): Int = height.toInt()

    fun setSize(width: Int, height: Int): Renderable {
        if (width < 0) throw IllegalArgumentException("Width cannot be negative")
        if (height < 0) throw IllegalArgumentException("Height cannot be negative")
        if (width.toUInt() == this.width && height.toUInt() == this.height) return this

        this.width = width.toUInt()
        this.height = height.toUInt()
        resize()
        return this
    }

    fun setWidth(width: Int): Renderable {
        if (width < 0) throw IllegalArgumentException("Width cannot be negative")
        if (width.toUInt() == this.width) return this

        this.width = width.toUInt()
        resize()
        return this
    }

    fun setHeight(height: Int): Renderable {
        if (height < 0) throw IllegalArgumentException("Height cannot be negative")
        if (height.toUInt() == this.height) return this

        this.height = height.toUInt()
        resize()
        return this
    }

    fun setBounds(x: Int, y: Int, width: Int, height: Int): Renderable {
        if (x == getX() && y == getY() && width.toUInt() == this.width && height.toUInt() == this.height) return this

        if (width < 0) throw IllegalArgumentException("Width cannot be negative")
        if (height < 0) throw IllegalArgumentException("Height cannot be negative")

        this.x = x.toUInt()
        this.y = y.toUInt()

        this.width = width.toUInt()
        this.height = height.toUInt()

        resize()

        return this
    }

    open fun init() {

    }

    fun mouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!isMouseOver(mouseX, mouseY)) return false

        renderables.firstOrNull { it.isMouseOver(mouseX, mouseY) }?.mouseClick(mouseX, mouseY, button)?.let { if (it) return true }

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
        renderables.firstOrNull { it.keyPress(key, scanCode, modifiers) }?.let { return true }
        return onKeyPress(key, scanCode, modifiers)
    }

    fun keyRelease(key: Int, scanCode: Int, modifiers: Int): Boolean {
        renderables.firstOrNull { it.keyRelease(key, scanCode, modifiers) }?.let { return true }
        return onKeyRelease(key, scanCode, modifiers)
    }

    fun charTyped(character: Char, modifiers: Int): Boolean {
        renderables.firstOrNull { it.charTyped(character, modifiers) }?.let { return true }
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
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight()
    }

    operator fun invoke(x: Int, y: Int, width: Int, height: Int): Renderable {
        setBounds(x, y, width, height)
        return this
    }
}

