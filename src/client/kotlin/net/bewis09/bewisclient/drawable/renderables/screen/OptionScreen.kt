package net.bewis09.bewisclient.drawable.renderables.screen

import kotlinx.atomicfu.atomic
import net.bewis09.bewisclient.drawable.*
import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.interfaces.BackgroundEffectProvider
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.screen.RenderableScreen
import net.bewis09.bewisclient.settings.types.Setting
import org.lwjgl.glfw.GLFW

class OptionScreen(startBlur: Float = 0f) : PopupScreen(), BackgroundEffectProvider {
    val editHudTranslation = Translation("bewisclient.options.edit_hud", "Edit HUD")

    val category = atomic<String?>(null)

    val alphaMainAnimation = Animator({ OptionsMenuSettings.animationTime.get().toLong() }, Animator.EASE_IN_OUT, "alpha" to 0f, "inside" to 1f, "blur" to startBlur)

    val settings = SettingStructure(this)

    val sidebarPlane = VerticalAlignScrollPlane(
        arrayListOf<Renderable>().also {
//            it.add(ThemeButton("Home") {
//                info("Home button clicked")
//            }.setHeight(14))
//            it.add(Rectangle(combineInt(OptionsMenuSettings.themeColor.get().net.bewis09.bewisclient.core.getColor(), 0.3f)).setHeight(1))
            it.addAll(settings.sidebarCategories)
            it.add(Rectangle { OptionsMenuSettings.themeColor.get().getColor() alpha 0.3f }.setHeight(1))
            it.add(ThemeButton("bewisclient:edit_hud", editHudTranslation(), category) {
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

    val image = RainbowImage(createIdentifier("bewisclient", "icon_long.png"), 0.5f)

    init {
        currentInstance = this
        alphaMainAnimation["alpha"] = 1f
        alphaMainAnimation["blur"] = 1f
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int, popupShown: Boolean) {
        screenDrawing.pushAlpha(alphaMainAnimation["alpha"])
        screenDrawing.setBewisclientFont()
        screenDrawing.fillWithBorderRounded(30, 30, width - 60, height - 60, 10, OptionsMenuSettings.getBackgroundColor(), OptionsMenuSettings.themeColor.get().getColor() alpha 0.3f)
        screenDrawing.fill(163, 31, 1, height - 62, OptionsMenuSettings.themeColor.get().getColor() alpha 0.3f)
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
        addRenderable(sidebarPlane(37, 37, 120, height - 101))
        addRenderable(image(37, height - 59, 120, 22))
        if (optionsHeaderBooleanSetting != null) {
            addRenderable(switch.setPosition(width - 37 - switch.width, 37))
        }
        optionsHeader?.setPosition(170, 37)?.setWidth(width - 207)?.let { addRenderable(it) }
        optionsPane?.invoke(170, 37 + (optionsHeader?.let { it.height + 5 } ?: 0), width - 207, height - 74 - (optionsHeader?.let { it.height + 5 } ?: 0))?.let { addRenderable(it) }
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