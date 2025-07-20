package net.bewis09.bewisclient.drawable

abstract class Renderable {
    var x: Int
    var y: Int
    var width: Int = 0
        set(value) {if (field != value) { field = value; resize() }}
    var height: Int = 0
        set(value) {if (field != value) { field = value; resize() }}

    constructor(x: Int, y: Int, width: Int, height: Int) {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
        this.renderables = mutableListOf<Renderable>()
    }

    constructor(): this(0, 0, 0, 0)

    val renderables: MutableList<Renderable>

    abstract fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int)

    /**
     * Renders all the renderables in this Renderable.
     * Should be called at some point in the rendering process.
     */
    fun renderRenderables(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        renderables.forEach { it.render(screenDrawing, mouseX, mouseY) }
    }

    fun resize() {
        renderables.clear()
        init()
        renderables.forEach { it.resize() }
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
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height
    }
}

