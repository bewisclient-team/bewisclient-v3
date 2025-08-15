package net.bewis09.bewisclient.drawable

import net.bewis09.bewisclient.drawable.renderables.Button
import net.bewis09.bewisclient.drawable.renderables.RainbowImage
import net.bewis09.bewisclient.drawable.renderables.Rectangle
import net.bewis09.bewisclient.drawable.renderables.VerticalAlignScrollPlane
import net.bewis09.bewisclient.drawable.renderables.hud_edit_screen.HudEditScreen
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.interfaces.BackgroundEffectProvider
import net.bewis09.bewisclient.screen.RenderableScreen
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW

class OptionScreen : Renderable(), BackgroundEffectProvider {
    val alphaMainAnimation = Animator(OptionsMenuSettings.animationTime.get().toLong(), Animator.Companion.EASE_IN_OUT, "alpha" to 0f, "inside" to 1f)

    val homeButton = Button("Home") {
        info("Home button clicked")
    }
    val editHUDButton = Button("Edit HUD") {
        alphaMainAnimation.set("alpha", 0f) {
            getClient().setScreen(RenderableScreen(HudEditScreen()))
        }
    }
    val sectionVerticalLine = Rectangle(combineInt(0xFFFFFF, 0.15f))
    val sectionHorizontalLine = Rectangle(combineInt(0xFFFFFF, 0.15f))
    val sectionHorizontalLine2 = Rectangle(combineInt(0xFFFFFF, 0.15f))
    val sidebarPlane = VerticalAlignScrollPlane(
        { width ->
            arrayListOf<Renderable>().also {
                it.add(homeButton.setHeight(14))
                it.add(sectionHorizontalLine.setHeight(1))
                it.addAll(settings.sidebarCategories)
                it.add(sectionHorizontalLine2.setHeight(1))
                it.add(editHUDButton.setHeight(14))
            }
        }, 5
    )

    companion object {
        var currentInstance: OptionScreen? = null
    }

    var popup: Popup? = null

    var optionsHeader: Renderable? = null
    var optionsPane: Renderable? = null
    val settings = SettingStructure(this)

    val image = RainbowImage(Identifier.of("bewisclient", "icon_long.png"), 0xFFFFFF, 0.5f)

    init {
        currentInstance = this
        alphaMainAnimation["alpha"] = 1f
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val mx = if (popup != null) Integer.MIN_VALUE else mouseX
        val my = if (popup != null) Integer.MAX_VALUE else mouseY

        screenDrawing.pushAlpha(alphaMainAnimation["alpha"])
        screenDrawing.setBewisclientFont()
        screenDrawing.fillWithBorderRounded(30, 30, getWidth() - 60, getHeight() - 60, 10, 0x000000, 0.5f, 0xFFFFFF, 0.15f)
        sectionVerticalLine.render(screenDrawing, mx, my)
        sidebarPlane.render(screenDrawing, mx, my)
        image.render(screenDrawing, mx, my)
        screenDrawing.pushAlpha(alphaMainAnimation["inside"])
        optionsHeader?.render(screenDrawing, mx, my)
        optionsPane?.render(screenDrawing, mx, my)
        screenDrawing.popColor()
        screenDrawing.popColor()
        popup?.render(screenDrawing, mouseX, mouseY)
        screenDrawing.defaultFont()
    }

    override fun init() {
        popup?.invoke(0,0,getWidth(), getHeight())?.let { addRenderable(it) }
        addRenderable(sectionVerticalLine(163,37,1, getHeight() - 74))
        addRenderable(sidebarPlane(37, 37, 120, getHeight() - 101))
        addRenderable(image(37, getHeight() - 59, 120, 22))
        optionsHeader?.setPosition(170, 37)?.setWidth(getWidth() - 207)?.let { addRenderable(it) }
        optionsPane?.invoke(170, 37 + (optionsHeader?.let { it.getHeight() + 5 } ?: 0), getWidth() - 207, getHeight() - 74 - (optionsHeader?.let { it.getHeight() + 5 } ?: 0))?.let { addRenderable(it) }
    }

    fun transformInside(afterHeader: Renderable?, afterPane: Renderable?) {
        if (alphaMainAnimation["inside"] != 1f) {
            return
        }

        alphaMainAnimation.set("inside", 0f) {
            optionsPane = afterPane
            optionsHeader = afterHeader
            resize()
            alphaMainAnimation["inside"] = 1f
        }
    }

    class Popup(val child: Renderable): Renderable() {
        val alphaAnimation = Animator(200, Animator.EASE_IN_OUT, "alpha" to 0f)

        init {
            alphaAnimation["alpha"] = 1f
        }

        override fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean {
            if (key == GLFW.GLFW_KEY_ESCAPE) {
                alphaAnimation.set("alpha", 0f) {
                    currentInstance?.popup?.let { a ->
                        currentInstance?.renderables?.remove(a)
                    }
                    currentInstance?.popup = null
                }
                return true
            }
            return super.onKeyPress(key, scanCode, modifiers)
        }

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.pushAlpha(alphaAnimation["alpha"])
            screenDrawing.fill(0,0,getWidth(),getHeight(), 0x000000, 0.5f)
            child.render(screenDrawing, mouseX, mouseY)
            screenDrawing.popColor()
        }

        override fun init() {
            addRenderable(child(0, 0, getWidth(), getHeight()))
        }

        override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int) = true

        override fun onMouseRelease(mouseX: Double, mouseY: Double, button: Int) = true

        override fun onMouseDrag(mouseX: Double, mouseY: Double, startX: Double, startY: Double, button: Int) = true

        override fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double) = true

        override fun onKeyRelease(key: Int, scanCode: Int, modifiers: Int) = true

        override fun onCharTyped(character: Char, modifiers: Int) = true
    }

    fun closePopup() {
        if (popup != null) {
            popup?.alphaAnimation?.set("alpha", 0f) {
                currentInstance?.popup?.let { a ->
                    currentInstance?.renderables?.remove(a)
                }
                currentInstance?.popup = null
            }
        }
    }

    fun openPopup(popupRenderable: Renderable) {
        if (popup != null) {
            popup?.let { renderables.remove(it) }
        }
        popup = Popup(popupRenderable)
        renderables.addFirst(popup!!)
        popup?.invoke(0,0,getWidth(),getHeight())?.resize()
    }

    override fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean {
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            alphaMainAnimation.set("alpha", 0f) {
                getClient().setScreen(null)
            }
            return true
        }
        return super.onKeyPress(key, scanCode, modifiers)
    }

    override fun getBackgroundEffectFactor(): Float {
        return alphaMainAnimation["alpha"]
    }
}