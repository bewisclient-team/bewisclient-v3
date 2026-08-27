package net.bewis09.renderite

import net.bewis09.bewisclient.features.sidebar.Debug
import net.bewis09.renderite.components.Div
import net.bewis09.renderite.components.Image
import net.bewis09.renderite.components.Rectangle
import net.bewis09.renderite.components.Text
import net.bewis09.renderite.drawer.RenderiteDrawer
import net.bewis09.renderite.drawer.pushColor
import net.bewis09.renderite.logic.Color

abstract class RenderiteElement<S : RenderiteDrawer<I, T, F>, P : RenderiteElement<S, P, T, F, I>, T : Any, F, I: Any>(val props: Props<P> = {}) {
    typealias Props<P> = P.() -> Unit

    companion object {
        operator fun <P> Props<P>.plus(p: Props<P>): Props<P> = { this@plus(); p() }
    }

    var minWidth: Int = 0
    var minHeight: Int = 0
    var maxWidth: Int = Int.MAX_VALUE
    var maxHeight: Int = Int.MAX_VALUE
    var widthProvider: (RenderiteElement<S, *, T, F, I>.() -> Int)? = null
    var heightProvider: (RenderiteElement<S, *, T, F, I>.() -> Int)? = null

    var shouldUsePointer = false
    var pointerProvider = { shouldUsePointer }
    var overflowVisible = false

    var background: ((S) -> Unit) = {}
    var colorModifier = { Color(1f, 1f, 1f, 1f) }

    val internalWidthProvider = { it: Int, s: RenderiteElement<S, *, T, F, I> -> widthProvider?.invoke(s) ?: it }
    val internalHeightProvider = { it: Int, s: RenderiteElement<S, *, T, F, I> -> heightProvider?.invoke(s) ?: it }

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

    private var lastUpdateTime: Long = 0

    val renderables = mutableListOf<RenderiteElement<S, *, T, F, I>>()

    var selectedElement: RenderiteElement<S, *, T, F, I>? = null

    fun render(screenDrawing: S, mouseX: Int, mouseY: Int) {
        screenDrawing.push()
        if (!overflowVisible) {
            screenDrawing.enableScissors(x, y, width, height)
        }

        val color = colorModifier()
        screenDrawing.pushColor(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f) {
            this.renderLogic(screenDrawing, mouseX, mouseY)
            background(screenDrawing)
            this.renderBackground(screenDrawing, mouseX, mouseY)
            this.renderElement(screenDrawing, mouseX, mouseY)
            this.renderRenderables(screenDrawing, mouseX, mouseY)
            this.renderAccessories(screenDrawing, mouseX, mouseY)
            this.cleanup(screenDrawing, mouseX, mouseY)
            this.renderDebug(screenDrawing)
        }

        if (!overflowVisible) {
            screenDrawing.disableScissors()
        }
        screenDrawing.pop()

        if (pointerProvider()) usePointer(screenDrawing, mouseX, mouseY)
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
    }

    fun renderDebug(screenDrawing: S) {
        val showUpdates = (Debug.showUpdates.get() && System.currentTimeMillis() < lastUpdateTime + 200)

        if (Debug.elementHighlight.get() && !showUpdates)
            renderables.forEach { screenDrawing.fill(it.x, it.y, it.width, it.height, Debug.highlightColor.get().getColor() alpha Debug.highlightAlpha.get()) }

        if (Debug.elementBorders.get() && !showUpdates)
            renderables.forEach { screenDrawing.drawBorder(it.x, it.y, it.width, it.height, Debug.borderColor.get().getColor() alpha Debug.borderAlpha.get()) }

        if (Debug.elementHighlight.get() && showUpdates)
            renderables.forEach { screenDrawing.fill(it.x, it.y, it.width, it.height, Debug.updateColor.get().getColor() alpha Debug.updateAlpha.get()) }

        if (Debug.elementBorders.get() && showUpdates)
            renderables.forEach { screenDrawing.drawBorder(it.x, it.y, it.width, it.height, Debug.updateBorderColor.get().getColor() alpha Debug.updateBorderAlpha.get()) }
    }

    fun <A : RenderiteElement<S, *, T, F, I>> addRenderable(renderable: A): A = renderable.also { renderables.add(it) }

    fun resize() {
        renderables.clear()
        Init().init()
        lastUpdateTime = System.currentTimeMillis()
        ArrayList(renderables).forEach { it.resize() }
    }

    fun updateX(x: Int): RenderiteElement<S, P, T, F, I> {
        if (x == this.x) return this

        this.x = x
        resize()
        return this
    }

    fun updateY(y: Int): RenderiteElement<S, P, T, F, I> {
        if (y == this.y) return this

        this.y = y
        resize()
        return this
    }

    fun updatePosition(x: Int, y: Int): RenderiteElement<S, P, T, F, I> {
        if (x == this.x && y == this.y) return this

        this.x = x
        this.y = y
        resize()
        return this
    }

    fun updateSize(width: Int, height: Int): RenderiteElement<S, P, T, F, I> {
        if (width < 0) throw IllegalArgumentException("Width cannot be negative")
        if (height < 0) throw IllegalArgumentException("Height cannot be negative")
        if (width == this.width && height == this.height) return this

        this.width = width
        this.height = height
        resize()
        return this
    }

    fun updateWidth(width: Int): RenderiteElement<S, P, T, F, I> {
        if (width < 0) throw IllegalArgumentException("Width cannot be negative")
        if (width == this.width) return this

        this.width = width
        resize()
        return this
    }

    fun updateHeight(height: Int): RenderiteElement<S, P, T, F, I> {
        if (height < 0) throw IllegalArgumentException("Height cannot be negative")
        if (height == this.height) return this

        this.height = height
        resize()
        return this
    }

    fun updateBounds(x: Int, y: Int, width: Int, height: Int): RenderiteElement<S, P, T, F, I> {
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

    open fun initLogic() {}

    open fun Init.init() {}

    fun mouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!isMouseOver(mouseX, mouseY)) return false

        renderables.firstOrNull { it.isMouseOver(mouseX, mouseY) && it.mouseClick(mouseX, mouseY, button) }?.also {
            selectedElement = it
            return true
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

        renderables.firstOrNull { it.isMouseOver(startX, startY) && it.mouseDrag(mouseX, mouseY, startX, startY, button) }?.let { return true }

        return onMouseDrag(mouseX, mouseY, startX, startY, button)
    }

    fun mouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        if (!isMouseOver(mouseX, mouseY)) return false

        renderables.firstOrNull { it.isMouseOver(mouseX, mouseY) && it.mouseScroll(mouseX, mouseY, horizontalAmount, verticalAmount) }?.let { return true }

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

    operator fun invoke(x: Int, y: Int, width: Int, height: Int): RenderiteElement<S, P, T, F, I> {
        updateBounds(x, y, width, height)
        return this
    }

    fun isMouseOver(mouseX: Int, mouseY: Int, x: Int, y: Int, width: Int, height: Int): Boolean {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height
    }

    fun <T> require(p: P.() -> T?): T =
        @Suppress("UNCHECKED_CAST")
        p(this as P) ?: throw IllegalArgumentException("Required property is missing")

    inner class Init {
        operator fun invoke(init: Init.() -> Unit) {
            this.init()
        }

        fun addRenderable(renderable: RenderiteElement<S, *, T, F, I>): RenderiteElement<S, *, T, F, I> = renderable.also { renderables.add(it) }
        fun addRenderables(renderable: Collection<RenderiteElement<S, *, T, F, I>>) = renderable.also { renderables.addAll(it) }

        fun RenderiteElement<S, *, T, F, I>.add(x: Int, y: Int, width: Int, height: Int) = this@RenderiteElement.addRenderable(this).updateBounds(x, y, width, height)
        fun RenderiteElement<S, *, T, F, I>.addPositioned(x: Int, y: Int) = this@RenderiteElement.addRenderable(this).updatePosition(x, y)
        fun <A: RenderiteElement<S, *, T, F, I>> A.add(): A = this@RenderiteElement.addRenderable(this)
    }

    fun Init.Div(recreateId: Number, p: Props<Div<S, T, F, I>>): RenderiteElement<S, *, T, F, I> {
        if (recreationMap.containsKey(recreateId)) {
            return addRenderable(recreationMap[recreateId]!!)
        }

        return addRenderable(Div<S, T, F, I>(p)).apply {
            this@RenderiteElement.recreationMap[recreateId] = this
        }
    }

    fun Init.Div(p: Props<Div<S, T, F, I>>) = addRenderable(Div<S, T, F, I>(p))
    fun Init.Text(p: Props<Text<S, T, F, I>>) = addRenderable(Text<S, T, F, I>(fullSizeProps() + p))
    fun Init.Rectangle(p: Props<Rectangle<S, T, F, I>>) = addRenderable(Rectangle<S, T, F, I>(fullSizeProps() + p))
    fun Init.Image(p: Props<Image<S, T, F, I>>) = addRenderable(Image<S, T, F, I>(fullSizeProps() + p))
    fun <L> Init.Empty(p: Props<EmptyElement<S, L, T, F, I>> = {}) where L : RenderiteElement<S, L, T, F, I> = addRenderable(EmptyElement(fullSizeProps() + p))

    class EmptyElement<S: RenderiteDrawer<I, T, F>, L: RenderiteElement<S, L, T, F, I>, T: Any, F, I: Any>(p: Props<EmptyElement<S, L, T, F, I>> = {}) : RenderiteElement<S, EmptyElement<S, L, T, F, I>, T, F, I>(p) {
        init {
            this.props()
        }
    }

    val recreationMap = hashMapOf<Number, RenderiteElement<S, *, T, F, I>>()

    fun removeFromCache(i: Int) = recreationMap.remove(i)

    fun <T: RenderiteElement<*, *, *, *, *>> fullSizeProps() = fun T.() {
        x = this@RenderiteElement.x
        y = this@RenderiteElement.y
        width = this@RenderiteElement.width
        height = this@RenderiteElement.height
    }
}