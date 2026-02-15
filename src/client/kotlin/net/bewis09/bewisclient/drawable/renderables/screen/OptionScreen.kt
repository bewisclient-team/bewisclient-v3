package net.bewis09.bewisclient.drawable.renderables.screen

import kotlinx.atomicfu.atomic
import net.bewis09.bewisclient.data.Constants
import net.bewis09.bewisclient.drawable.*
import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen.ImageIdentifier.iconIdentifier
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawingInterface.Companion.DEFAULT_FONT
import net.bewis09.bewisclient.drawable.screen_drawing.transform
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.generated.BuildInfo
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.interfaces.BackgroundEffectProvider
import net.bewis09.bewisclient.screen.RenderableScreen
import net.bewis09.bewisclient.security.Security
import net.bewis09.bewisclient.settings.types.Setting
import net.bewis09.bewisclient.util.Bewisclient
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.createIdentifier
import net.minecraft.screen.ScreenTexts
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import org.lwjgl.glfw.GLFW
import kotlin.io.encoding.Base64

class OptionScreen(startBlur: Float = 0f) : PopupScreen(), BackgroundEffectProvider {
    val editHudTranslation = Translation("options.edit_hud", "Edit HUD")

    val category = atomic("bewisclient:home")

    val alphaMainAnimation = Animator({ OptionsMenuSettings.animationTime.get().toLong() }, Animator.EASE_IN_OUT, "alpha" to 0f, "inside" to 1f, "blur" to startBlur)

    val settings = SettingStructure(this)

    val backIdentifier = createIdentifier("bewisclient", "textures/gui/sprites/back.png")
    val closeIdentifier = createIdentifier("bewisclient", "textures/gui/sprites/remove.png")

    val sidebarPlane = VerticalAlignScrollPlane(
        arrayListOf<Renderable>().also {
            it.add(settings.homeCategory(this).let { button ->
                Plane { x, y, _, _ ->
                    listOf(
                        ImageButton(backIdentifier) {
                            if (category.value == "bewisclient:home" && isCategoryStart) {
                                alphaMainAnimation["blur"] = 0f
                                alphaMainAnimation["alpha"] = 0f then {
                                    client.setScreen(null)
                                }
                            } else if (category.value == "bewisclient:home" || isCategoryStart)
                                settings.homeCategory(this).onClick(button)
                            else
                                settings.sidebarCategories.mapNotNull { a -> a as? ThemeButton }.firstOrNull { a -> a.selected() }?.let { a -> a.onClick(a) }
                        }.setImagePadding(1).setImageColor { OptionsMenuSettings.getTextThemeColor() }(x, y, 14, 14), button(x + 19, y, 82, 14), ImageButton(closeIdentifier) {
                            alphaMainAnimation["blur"] = 0f
                            alphaMainAnimation["alpha"] = 0f then {
                                client.setScreen(null)
                            }
                        }.setImagePadding(3).setImageColor { OptionsMenuSettings.getTextThemeColor() }(x + 106, y, 14, 14)
                    )
                }.setHeight(14)
            })
            it.add(Rectangle { OptionsMenuSettings.getThemeColor(alpha = 0.3f) }.setHeight(1))
            it.addAll(settings.sidebarCategories)
            it.add(Rectangle { OptionsMenuSettings.getThemeColor(alpha = 0.3f) }.setHeight(1))
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

    object ImageIdentifier : EventEntrypoint {
        @Suppress("SpellCheckingInspection")
        val icon =
            "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"

        override fun onMinecraftClientInitFinished() {
            Bewisclient.createTexture(iconIdentifier, Base64.decode(icon))
        }

        val iconIdentifier: Identifier = createIdentifier("bewisclient", "native/icon_long")
    }

    var page = Page(
        settings.homeCategory.getHeader(),
        settings.homeCategory.renderable,
        null
    )

    var isCategoryStart = true

    var switch = Switch(state = { page.setting?.get() ?: false }, onChange = { page.setting?.set(it) })

    val image = RainbowImage(iconIdentifier, 0.5f)

    init {
        currentInstance = this
        alphaMainAnimation["alpha"] = 1f
        alphaMainAnimation["blur"] = 1f
    }

    override fun renderScreen(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        checkValidVersion()

        screenDrawing.pushAlpha(alphaMainAnimation["alpha"])
        screenDrawing.setBewisclientFont()

        renderBackground(screenDrawing)
        renderSidebar(screenDrawing, mouseX, mouseY)
        renderVersionText(screenDrawing)
        renderInner(screenDrawing, mouseX, mouseY)

        screenDrawing.popColor()
        screenDrawing.setDefaultFont()
    }

    fun renderBackground(screenDrawing: ScreenDrawing) {
        screenDrawing.fillWithBorderRounded(30, 30, 134, height - 60, 5, OptionsMenuSettings.getBackgroundColor(), OptionsMenuSettings.getThemeColor(alpha = 0.3f))
        screenDrawing.fillWithBorderRounded(169, 30, width - 199, height - 60, 5, OptionsMenuSettings.getBackgroundColor(), OptionsMenuSettings.getThemeColor(alpha = 0.3f))
    }

    fun renderSidebar(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        sidebarPlane.render(screenDrawing, mouseX, mouseY)
        image.render(screenDrawing, mouseX, mouseY)
    }

    fun renderVersionText(screenDrawing: ScreenDrawing) {
        screenDrawing.transform(width - 5f, height - 11f, 0.7f) {
            screenDrawing.drawRightAlignedText("Bewisclient ${BuildInfo.VERSION} by Bewis09", 0, 0, OptionsMenuSettings.getThemeColor(alpha = 0.5f))
        }
    }

    fun renderInner(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.pushAlpha(alphaMainAnimation["inside"])

        page.header.render(screenDrawing, mouseX, mouseY)
        page.pane.render(screenDrawing, mouseX, mouseY)
        if (page.setting != null) {
            switch.render(screenDrawing, mouseX, mouseY)
        }

        screenDrawing.popColor()
    }

    fun checkValidVersion() {
        if (!Security.verificationState.allowed) client.setScreen(RenderableScreen(VersionInvalidScreen))
    }

    object VersionInvalidScreen : Renderable() {
        const val SECURITY_MESSAGE = "Your version of Bewisclient could not be verified. This probably means that the file your are using was changed after downloading or the version you are using was removed from Modrinth due to a critical bug.\n\nPlease download the newest version from Modrinth to ensure you are using a safe version.\n\nIf you believe this is an error, please let us know on GitHub."

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.wrapText(SECURITY_MESSAGE + "\n\nError message: ${(Security.verificationState as? Security.ILLEGAL)?.reason ?: "Unknown"}", 300).let {
                screenDrawing.drawCenteredWrappedText(it, width / 2, height / 2 - it.size * 9 / 2 - 30, Color.WHITE, DEFAULT_FONT, true)
            }

            screenDrawing.transform(width - 5f, height - 11f, 0.7f) {
                screenDrawing.drawRightAlignedText("Bewisclient ${BuildInfo.VERSION} by Bewis09", 0, 0, OptionsMenuSettings.getThemeColor(alpha = 0.5f))
            }

            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            addRenderable(MinecraftButton(ScreenTexts.BACK) {
                client.setScreen(null)
            }(width / 2 - 102, height / 2 + 50, 100, 20))
            addRenderable(MinecraftButton(Translations.MODRINTH()) {
                Util.getOperatingSystem().open(Constants.MODRINTH_URL)
            }(width / 2 + 2, height / 2 + 50, 100, 20))
        }

        override fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean {
            if (key == GLFW.GLFW_KEY_ESCAPE) {
                client.setScreen(null)
                return true
            }
            return super.onKeyPress(key, scanCode, modifiers)
        }
    }

    override fun init() {
        super.init()
        addRenderable(sidebarPlane(37, 37, 120, height - 101))
        addRenderable(image(37, height - 59, 120, 22))
        if (page.setting != null) {
            addRenderable(switch.setPosition(width - 37 - switch.width, 37))
        }
        page.header.setPosition(175, 37).setWidth(width - 211).let { addRenderable(it) }
        addRenderable(page.pane.invoke(175, 37 + (page.header.height + 5), width - 211, height - 74 - (page.header.height + 5)))
    }

    fun openPage(afterHeader: Renderable, afterPane: Renderable, setting: Setting<Boolean>? = null, isCategoryStart: Boolean = false) {
        this.isCategoryStart = isCategoryStart

        alphaMainAnimation["inside"] = 0f then {
            page = Page(afterHeader, afterPane, setting)
            resize()
            alphaMainAnimation["inside"] = 1f
        }
    }

    class Page(val header: Renderable, val pane: Renderable, val setting: Setting<Boolean>? = null)

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