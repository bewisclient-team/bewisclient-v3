package net.bewis09.bewisclient.impl.screen

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.Animator.Companion.EASE_IN_OUT
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.drawable.combineInt
import net.bewis09.bewisclient.drawable.renderables.Button
import net.bewis09.bewisclient.drawable.renderables.RainbowImage
import net.bewis09.bewisclient.drawable.renderables.Rectangle
import net.bewis09.bewisclient.drawable.renderables.VerticalAlignScrollPlane
import net.bewis09.bewisclient.interfaces.BackgroundEffectProvider
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW

class OptionScreen : Renderable(), BackgroundEffectProvider {
    val alphaMainAnimation = Animator(getSettings().optionsMenu.animationTime.get().toLong(), EASE_IN_OUT, "alpha" to 0f, "inside" to 1f)

    val homeButton = Button("Home") {
        info("Home button clicked")
    }
    val editHUDButton = Button( "Edit HUD") {
        info("Edit HUD button clicked")
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

    var optionsHeader: Renderable? = null
    var optionsPane: Renderable? = null
    val settings = OptionsScreenSettingStructure(this)

    val image = RainbowImage(Identifier.of("bewisclient", "icon_long.png"), 0xFFFFFF, 0.5f)

    init {
        currentInstance = this
        alphaMainAnimation["alpha"] = 1f
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.pushAlpha(alphaMainAnimation["alpha"])
        screenDrawing.setBewisclientFont()
        screenDrawing.fillWithBorderRounded(30, 30, getWidth() - 60, getHeight() - 60, 10, 0x000000, 0.5f, 0xFFFFFF, 0.15f)
        sectionVerticalLine.render(screenDrawing, mouseX, mouseY)
        sidebarPlane.render(screenDrawing, mouseX, mouseY)
        image.render(screenDrawing, mouseX, mouseY)
        screenDrawing.pushAlpha(alphaMainAnimation["inside"])
        optionsHeader?.render(screenDrawing, mouseX, mouseY)
        optionsPane?.render(screenDrawing, mouseX, mouseY)
        screenDrawing.popColor()
        screenDrawing.defaultFont()
        screenDrawing.popColor()
    }

    override fun init() {
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