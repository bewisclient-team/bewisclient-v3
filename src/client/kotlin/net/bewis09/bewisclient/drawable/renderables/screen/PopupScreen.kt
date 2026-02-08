package net.bewis09.bewisclient.drawable.renderables.screen

import net.bewis09.bewisclient.core.translateToTopOptional
import net.bewis09.bewisclient.drawable.*
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.color.Color
import org.lwjgl.glfw.GLFW

abstract class PopupScreen : Renderable() {
    var popup: Popup? = null
    var backgroundColor: Color = Color.BLACK alpha 0.5f

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val mx = if (popup != null) Integer.MIN_VALUE else mouseX
        val my = if (popup != null) Integer.MAX_VALUE else mouseY

        render(screenDrawing, mx, my, popup != null)
        popup?.render(screenDrawing, mouseX, mouseY)
    }

    abstract fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int, popupShown: Boolean)

    class Popup(val child: Renderable, val screen: PopupScreen) : Renderable() {
        val alphaAnimation = Animator(200, Animator.EASE_IN_OUT, "alpha" to 0f)

        init {
            alphaAnimation["alpha"] = 1f
        }

        override fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean {
            if (key == GLFW.GLFW_KEY_ESCAPE) {
                alphaAnimation["alpha"] = 0f then {
                    screen.popup?.let { a ->
                        screen.renderables.remove(a)
                        screen.popup = null
                        screen.selectedElement = null
                    }
                }
                return true
            }
            return super.onKeyPress(key, scanCode, modifiers)
        }

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.push()
            screenDrawing.drawContext.translateToTopOptional()
            screenDrawing.pushAlpha(alphaAnimation["alpha"])
            screenDrawing.fill(0, 0, width, height, screen.backgroundColor)
            screenDrawing.setBewisclientFont()
            child.render(screenDrawing, mouseX, mouseY)
            screenDrawing.defaultFont()
            screenDrawing.popColor()
            screenDrawing.pop()
        }

        override fun init() {
            addRenderable(child(0, 0, width, height))
        }

        override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int) = true

        override fun onMouseDrag(mouseX: Double, mouseY: Double, startX: Double, startY: Double, button: Int) = true

        override fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double) = true

        override fun onKeyRelease(key: Int, scanCode: Int, modifiers: Int) = true

        override fun onCharTyped(character: Char, modifiers: Int) = true
    }

    override fun init() {
        popup?.invoke(0, 0, width, height)?.let { addRenderable(it) }
    }

    fun closePopup() {
        val popup = this.popup
        popup?.alphaAnimation?.let {
            it["alpha"] = 0f then {
                popup.let(renderables::remove)
                this.popup = null
                selectedElement = null
            }
        }
    }

    fun openPopup(popupRenderable: Renderable, backgroundColor: Color = Color.BLACK alpha 0.5f) {
        this.backgroundColor = backgroundColor
        if (popup != null) {
            popup?.let { renderables.remove(it) }
        }
        popup = Popup(popupRenderable, this)
        renderables.addFirst(popup!!)
        popup?.invoke(0, 0, width, height)?.resize()
        selectedElement = popup
    }

    override fun renderRenderables(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        ArrayList(renderables).forEach { if (it == popup) return@forEach; it.render(screenDrawing, mouseX, mouseY) }
    }
}