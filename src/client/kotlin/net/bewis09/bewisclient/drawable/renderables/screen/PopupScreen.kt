package net.bewis09.bewisclient.drawable.renderables.screen

import com.mojang.blaze3d.platform.InputConstants
import net.bewis09.bewisclient.common.toText
import net.bewis09.renderite.logic.Animator
import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.generated.BuildInfo
import net.bewis09.bewisclient.version.translateToTopOptional
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.TextAlign
import net.bewis09.renderite.style.RenderiteChild

abstract class PopupScreen(p: Props<PopupScreen> = {}) : PropedRenderable<PopupScreen>(p) {
    var popup: Popup? = null
    var popupBackground: Color = Color.BLACK alpha 0.5f

    init { props() }

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        renderScreen(screenDrawing, if (popup != null) Integer.MIN_VALUE else mouseX, if (popup != null) Integer.MIN_VALUE else mouseY)
    }

    override fun renderRenderables(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        ArrayList(renderables).forEach { if(it != popup) it.render(screenDrawing, if (popup != null) Integer.MIN_VALUE else mouseX, if (popup != null) Integer.MIN_VALUE else mouseY) }
        popup?.render(screenDrawing, mouseX, mouseY)
    }

    open fun renderScreen(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {}

    class Popup(p: Props<Popup>) : PropedRenderable<Popup>(p + {
        colorModifier = { Color.WHITE alpha alphaAnimation.get() }
    }) {
        lateinit var screen: PopupScreen
        lateinit var child: Renderable

        init { props() }

        val alphaAnimation = Animator({ General.animationDuration }, Animator.EASE_IN_OUT, 0f)

        init {
            alphaAnimation.set(1f)
        }

        override fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean {
            if (key == InputConstants.KEY_ESCAPE) {
                alphaAnimation.set(0f) {
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

        override fun renderLogic(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.guiGraphics.translateToTopOptional()
            screenDrawing.setBewisclientFont()
        }

        override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.fill(0, 0, width, height, screen.popupBackground)
        }

        override fun cleanup(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.setDefaultFont()
        }

        override fun init() {
            child.addPositioned((width - child.width) / 2, (height - child.height) / 2)
        }

        override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
            if (!child.isMouseOver(mouseX, mouseY)) {
                screen.closePopup()
                return true
            }
            return true
        }

        override fun onMouseDrag(mouseX: Double, mouseY: Double, startX: Double, startY: Double, button: Int) = true

        override fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double) = true

        override fun onKeyRelease(key: Int, scanCode: Int, modifiers: Int) = true

        override fun onCharTyped(character: Char, modifiers: Int) = true
    }

    override fun init() {
        popup?.invoke(0, 0, width, height)?.add()
    }

    fun closePopup() {
        val popup = this.popup
        popup?.alphaAnimation?.set(0f) {
            popup.let(renderables::remove)
            this@PopupScreen.popup = null
            selectedElement = null
        }
    }

    fun openPopup(popupRenderable: Renderable, backgroundColor: Color = Color.BLACK alpha 0.5f) {
        this.popupBackground = backgroundColor
        if (popup != null) {
            popup?.let { renderables.remove(it) }
        }
        popup = Popup {
            screen = this@PopupScreen
            child = popupRenderable
        }
        renderables.addFirst(popup!!)
        popup?.invoke(0, 0, width, height)?.resize()
        selectedElement = popup
    }

    @RenderiteChild
    fun VersionText() {
        Text {
            text = "Bewisclient ${BuildInfo.VERSION} by Bewis09".toText()
            color = General.getThemeColor(alpha = 0.5f)
            textAlign = TextAlign.END
            fontSize = 7f
            overflowVisible = true
            heightResize = true
            paddingRight = 5
        }.updatePosition(0, height - 11)
    }
}