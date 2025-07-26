package net.bewis09.bewisclient.impl.screen

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.Animator.Companion.EASE_IN_OUT
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.drawable.combineInt
import net.bewis09.bewisclient.drawable.renderables.Button
import net.bewis09.bewisclient.drawable.renderables.Rectangle
import net.bewis09.bewisclient.drawable.renderables.VerticalAlignScrollPlane
import net.bewis09.bewisclient.interfaces.BackgroundEffectProvider
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW

class OptionScreen : Renderable(), BackgroundEffectProvider {
    val alphaMainAnimation = Animator(getSettings().optionsMenu.animationTime.get().toLong(), EASE_IN_OUT, "alpha" to 0f)

    val editHUDButton = Button( "Edit HUD") {
        info("Edit HUD button clicked")
    }
    val sectionVerticalLine = Rectangle(combineInt(0xFFFFFF, 0.15f))
    val sectionHorizontalLine = Rectangle(combineInt(0xFFFFFF, 0.15f))
    val sidebarPlane = VerticalAlignScrollPlane(
        { width ->
            settings.sidebarCategories
        }, 5
    )

    var optionsPane: Renderable? = null
    val settings = SettingsStructure(this)

    init {
        alphaMainAnimation["alpha"] = 1f
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.pushAlpha(alphaMainAnimation["alpha"])
        screenDrawing.setFont(Identifier.of("bewisclient", "screen"))
        screenDrawing.fillWithBorderRounded(30, 30, getWidth() - 60, getHeight() - 60, 10, 0x000000, 0.5f, 0xFFFFFF, 0.15f)
        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.defaultFont()
        screenDrawing.popColor()
    }

    override fun init() {
        addRenderable(editHUDButton(37, getHeight()-51, 120, 14))
        addRenderable(sectionVerticalLine(163,37,1, getHeight() - 74))
        addRenderable(sectionHorizontalLine(37, getHeight() - 57, 120, 1))
        addRenderable(sidebarPlane(37, 37, 120, getHeight() - 99))
        optionsPane?.invoke(170, 37, getWidth() - 207, getHeight() - 74)?.let { addRenderable(it) }
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