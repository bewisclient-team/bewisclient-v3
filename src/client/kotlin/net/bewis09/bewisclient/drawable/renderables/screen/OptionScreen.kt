package net.bewis09.bewisclient.drawable.renderables.screen

import kotlinx.atomicfu.atomic
import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.SettingStructure
import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.then
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.interfaces.BackgroundEffectProvider
import net.bewis09.bewisclient.logic.color.alpha
import net.bewis09.bewisclient.logic.color.within
import net.bewis09.bewisclient.screen.RenderableScreen
import net.bewis09.bewisclient.settings.types.Setting
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW
import java.awt.Color

class OptionScreen(startBlur: Float = 0f) : PopupScreen(), BackgroundEffectProvider {
    val clickedButton = atomic<ThemeButton?>(null)

    val alphaMainAnimation = Animator({ OptionsMenuSettings.animationTime.get().toLong() }, Animator.EASE_IN_OUT, "alpha" to 0f, "inside" to 1f, "blur" to startBlur)

    val settings = SettingStructure(this)

    val sidebarPlane = VerticalAlignScrollPlane(
        arrayListOf<Renderable>().also {
//            it.add(ThemeButton("Home") {
//                info("Home button clicked")
//            }.setHeight(14))
//            it.add(Rectangle(combineInt(OptionsMenuSettings.themeColor.get().getColor(), 0.3f)).setHeight(1))
            it.addAll(settings.sidebarCategories)
            it.add(Rectangle { OptionsMenuSettings.themeColor.get().getColor() alpha 0.3f }.setHeight(1))
            it.add(ThemeButton("Edit HUD", clickedButton) {
                alphaMainAnimation["alpha"] = 0f then {
                    client.setScreen(RenderableScreen(HudEditScreen()))
                }
            }.setHeight(14))
        }, 5
    )

    companion object {
        var currentInstance: OptionScreen? = null
    }

    var optionsHeader: Renderable? = null
    var optionsPane: Renderable? = null
    var optionsHeaderBooleanSetting: Setting<Boolean>? = null

    var switch = Switch(state = { optionsHeaderBooleanSetting?.get() ?: false }, onChange = { optionsHeaderBooleanSetting?.set(it) })

    val image = RainbowImage(Identifier.of("bewisclient", "icon_long.png"), 0.5f)

    init {
        currentInstance = this
        alphaMainAnimation["alpha"] = 1f
        alphaMainAnimation["blur"] = 1f
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int, popupShown: Boolean) {
        screenDrawing.pushAlpha(alphaMainAnimation["alpha"])
        screenDrawing.setBewisclientFont()
        screenDrawing.fillWithBorderRounded(30, 30, getWidth() - 60, getHeight() - 60, 10, 0.15f within (Color.BLACK to OptionsMenuSettings.themeColor.get().getColor()) alpha 0.6f, OptionsMenuSettings.themeColor.get().getColor() alpha 0.3f)
        screenDrawing.fill(163, 31, 1, getHeight() - 62, OptionsMenuSettings.themeColor.get().getColor() alpha 0.3f)
        sidebarPlane.render(screenDrawing, mouseX, mouseY)
        image.render(screenDrawing, mouseX, mouseY)
        screenDrawing.pushAlpha(alphaMainAnimation["inside"])
        optionsHeader?.render(screenDrawing, mouseX, mouseY)
        optionsPane?.render(screenDrawing, mouseX, mouseY)
        if (optionsHeaderBooleanSetting != null) {
            switch.render(screenDrawing, mouseX, mouseY)
        }
        screenDrawing.popColor()
        screenDrawing.popColor()
        screenDrawing.defaultFont()
    }

    override fun init() {
        super.init()
        addRenderable(sidebarPlane(37, 37, 120, getHeight() - 101))
        addRenderable(image(37, getHeight() - 59, 120, 22))
        if (optionsHeaderBooleanSetting != null) {
            addRenderable(switch.setPosition(getWidth() - 37 - switch.getWidth(), 37))
        }
        optionsHeader?.setPosition(170, 37)?.setWidth(getWidth() - 207)?.let { addRenderable(it) }
        optionsPane?.invoke(170, 37 + (optionsHeader?.let { it.getHeight() + 5 } ?: 0), getWidth() - 207, getHeight() - 74 - (optionsHeader?.let { it.getHeight() + 5 } ?: 0))?.let { addRenderable(it) }
    }

    fun transformInside(afterHeader: Renderable?, afterPane: Renderable?, setting: Setting<Boolean>? = null) {
        if (optionsHeader == null && optionsPane == null && optionsHeaderBooleanSetting == null) alphaMainAnimation.pauseForOnce()

        alphaMainAnimation["inside"] = 0f then {
            optionsPane = afterPane
            optionsHeader = afterHeader
            optionsHeaderBooleanSetting = setting
            resize()
            alphaMainAnimation["inside"] = 1f
        }
    }

    override fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean {
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            alphaMainAnimation["blur"] = 0f
            alphaMainAnimation["alpha"] = 0f then {
                client.setScreen(null)
            }
            return true
        }
        return super.onKeyPress(key, scanCode, modifiers)
    }

    override fun getBackgroundEffectFactor(): Float {
        return alphaMainAnimation["blur"]
    }
}