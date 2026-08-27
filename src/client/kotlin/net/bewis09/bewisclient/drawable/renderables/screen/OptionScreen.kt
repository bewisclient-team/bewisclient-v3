package net.bewis09.bewisclient.drawable.renderables.screen

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.common.Util
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.then
import net.bewis09.bewisclient.data.Constants
import net.bewis09.bewisclient.drawable.BackgroundEffectProvider
import net.bewis09.bewisclient.drawable.ImageIdentifier.setRenderableScreen
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.SimpleRenderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.ImageButton
import net.bewis09.bewisclient.drawable.renderables.components.button.MinecraftButton
import net.bewis09.bewisclient.drawable.renderables.components.button.ThemeButton
import net.bewis09.bewisclient.drawable.renderables.components.element.RainbowImage
import net.bewis09.bewisclient.drawable.renderables.components.setting.Switch
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing.Companion.DEFAULT_FONT
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.features.sidebar.Home
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.generated.BuildInfo
import net.bewis09.bewisclient.server.Security
import net.bewis09.bewisclient.settings.structure.SidebarFeature
import net.bewis09.bewisclient.settings.types.Setting
import net.bewis09.bewisclient.util.Bewisclient
import net.bewis09.bewisclient.version.setScreen
import net.bewis09.renderite.drawer.transform
import net.bewis09.renderite.logic.Animator
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.FitType
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import org.lwjgl.glfw.GLFW

class OptionScreen(startBlur: Float = 0f, startAlpha: Float = 0f) : PopupScreen(), BackgroundEffectProvider {
    val editHudTranslation = Translation("options.edit_hud", "Edit HUD")

    var category = "bewisclient:home"

    val alphaMainAnimation = Animator({ General.animationDuration }, Animator.EASE_IN_OUT, startAlpha)
    val insideMainAnimation = Animator({ General.animationDuration }, Animator.EASE_IN_OUT, 1f)
    val blurMainAnimation = Animator({ General.animationDuration }, Animator.EASE_IN_OUT, startBlur)

    init {
        colorModifier = { Color(1f, 1f, 1f, alphaMainAnimation.get()) }
    }

    val backIdentifier = createIdentifier("bewisclient", "textures/gui/sprites/back.png")
    val closeIdentifier = createIdentifier("bewisclient", "textures/gui/sprites/remove.png")

    companion object {
        var currentInstance: OptionScreen? = null
        val modrinthButtonText = Translation("menu.pack.modrinth", "Modrinth")

        fun getOrCreateInstance(startBlur: Float = 0f, startAlpha: Float = 0f): OptionScreen {
            if (General.restoreTab()) {
                return this.currentInstance?.apply {
                    alphaMainAnimation.setInstant(startAlpha)
                    blurMainAnimation.setInstant(startBlur)
                    insideMainAnimation.setInstant(1f)

                    alphaMainAnimation.set(1f)
                    blurMainAnimation.set(1f)
                    width = General.screenWidth
                    height = General.screenHeight
                    resize()
                } ?: OptionScreen(startBlur, startAlpha)
            }

            return OptionScreen(startBlur, startAlpha)
        }
    }

    val pageStack = mutableListOf(Page(Home.title(), Home.getRenderable(), null))

    val page
        get() = pageStack.last()

    init {
        currentInstance = this
        alphaMainAnimation.set(1f)
        blurMainAnimation.set(1f)
        width = General.screenWidth
        height = General.screenHeight
        resize()
    }

    override fun renderLogic(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        checkValidVersion()
        screenDrawing.setBewisclientFont()
    }

    override fun cleanup(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.setDefaultFont()
    }

    override fun renderScreen(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        renderVersionText(screenDrawing)
    }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderMenuBackground(screenDrawing, width, height)
    }

    fun renderVersionText(screenDrawing: ScreenDrawing) {
        screenDrawing.transform(width - 5f, height - 11f, 0.7f) {
            screenDrawing.drawRightAlignedText("Bewisclient ${BuildInfo.VERSION} by Bewis09", 0, 0, if (General.isMinecrafty) Color.WHITE alpha 0.5f else General.getThemeColor(alpha = 0.5f))
        }
    }

    fun checkValidVersion() {
        if (!Security.verificationState.allowed) setRenderableScreen(VersionInvalidScreen)
    }

    object VersionInvalidScreen : SimpleRenderable() {
        const val SECURITY_MESSAGE =
            "Your version of Bewisclient could not be verified. This probably means that the file your are using was changed after downloading or the version you are using was removed from Modrinth due to a critical bug.\n\nPlease download the newest version from Modrinth to ensure you are using a safe version.\n\nIf you believe this is an error, please let us know on GitHub."

        override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.wrapText(SECURITY_MESSAGE + "\n\nError message: ${(Security.verificationState as? Security.ILLEGAL)?.reason ?: "Unknown"}", 300).let {
                screenDrawing.drawCenteredWrappedText(it, width / 2, height / 2 - it.size * 9 / 2 - 30, Color.WHITE, DEFAULT_FONT, true)
            }

            screenDrawing.transform(width - 5f, height - 11f, 0.7f) {
                screenDrawing.drawRightAlignedText("Bewisclient ${BuildInfo.VERSION} by Bewis09", 0, 0, General.getThemeColor(alpha = 0.5f))
            }
        }

        override fun Init.init() {
            MinecraftButton {
                text = CommonComponents.GUI_BACK
                onClick = { setScreen(null) }
            }(width / 2 - 102, height / 2 + 50, 100, 20)
            MinecraftButton {
                text = modrinthButtonText()
                onClick = { Util.getPlatform().openUri(Constants.MODRINTH_URL) }
            }(width / 2 + 2, height / 2 + 50, 100, 20)
        }

        override fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean {
            if (key == GLFW.GLFW_KEY_ESCAPE) {
                setScreen(null)
                return true
            }
            return super.onKeyPress(key, scanCode, modifiers)
        }
    }

    override fun Init.init() {
        Div(0) {
            cacheChildren = true
            gap = (General.isMinecrafty then 2) ?: 5
            fitType = FitType.SCROLL
            onInit = {
                Home.createButton().let { button ->
                    object : SimpleRenderable() {
                        fun Init.createTopButton(identifier: Identifier, padding: Int, x: Int, y: Int, onClick: () -> Unit) = ImageButton {
                            image = identifier
                            this.onClick = { onClick() }
                            imagePadding = padding
                        }(x, y, SelectiveScreenDrawer.getSideButtonHeight(), SelectiveScreenDrawer.getSideButtonHeight())

                        override fun Init.init() {
                            createTopButton(backIdentifier, 1, x, y, ::goBack)
                            button(x + 19, y, 82, SelectiveScreenDrawer.getSideButtonHeight()).add()
                            createTopButton(closeIdentifier, 3, x + 106 - ((General.isMinecrafty then 4) ?: 0), y, ::close)
                        }
                    }.updateHeight(SelectiveScreenDrawer.getSideButtonHeight())
                }.add()
                Rectangle {
                    colorProvider = { General.getThemeColor(alpha = 0.3f) }
                }.updateHeight(1)
                APIEntrypointLoader.mapEntrypoint { a -> a.getSidebarCategories().forEach { b -> b.createButton().add() } }
                Rectangle {
                    colorProvider = { General.getThemeColor(alpha = 0.3f) }
                }.updateHeight(1)
                ThemeButton {
                    text = editHudTranslation()
                    onClick = { alphaMainAnimation.set(0f) { Bewisclient.setRenderableScreen(HudEditScreen()) } }
                }.updateHeight(SelectiveScreenDrawer.getSideButtonHeight())
            }
        }(37, 37, 120, height - 101)

        RainbowImage()(37, height - 59, 120, 22)

        if (page.setting != null) {
            Switch {
                colorModifier = { Color(1f, 1f, 1f, insideMainAnimation.get()) }
                state = { page.setting?.get() ?: false }
                onChange = { page.setting?.set(it) }
            }.updatePosition(width - 61, 37)
        }

        page.header.updatePosition(175, 37).updateWidth(width - 211).add()
        page.pane.invoke(175, 37 + (page.header.height + 5), width - 211, height - 74 - (page.header.height + 5)).add()

        page.header.colorModifier = { Color(1f, 1f, 1f, insideMainAnimation.get()) }
        page.pane.colorModifier = { Color(1f, 1f, 1f, insideMainAnimation.get()) }
    }

    fun changeCategory(category: SidebarFeature, instant: Boolean = false) {
        this.category = category.id.toString()

        if (instant) {
            pageStack.removeAll { pageStack[0] != it }
            pageStack.add(Page(category.title(), category.getRenderable()))
            return resize()
        }

        insideMainAnimation.set(0f) {
            pageStack.removeAll { pageStack[0] != it }
            pageStack.add(Page(category.title(), category.getRenderable()))
            resize()
            insideMainAnimation.set(1f)
        }
    }

    fun openPage(afterHeader: Component, afterPane: Renderable, setting: Setting<Boolean>? = null, instant: Boolean = false) {
        if (instant) {
            pageStack.add(Page(afterHeader, afterPane, setting))
            return resize()
        }

        insideMainAnimation.set(0f) {
            pageStack.add(Page(afterHeader, afterPane, setting))
            resize()
            insideMainAnimation.set(1f)
        }
    }

    fun goBack(instant: Boolean = false) {
        if (pageStack.size == 1) return close()
        if (pageStack.size == 2) category = "bewisclient:home"

        if (instant) {
            pageStack.removeLast()
            return resize()
        }

        insideMainAnimation.set(0f) {
            pageStack.removeLast()
            resize()
            insideMainAnimation.set(1f)
        }
    }

    fun close() {
        blurMainAnimation.set(0f)
        alphaMainAnimation.set(0f) {
            setScreen(null)
        }
    }

    class Page(header: Component, val pane: Renderable, val setting: Setting<Boolean>? = null) {
        val header = Header(header).updateHeight(if(General.isMinecrafty) 18 else 14)
    }

    class Header(val header: Component): SimpleRenderable() {
        override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.transform(exactCenterX, screenDrawing.getTextYCenter(this) - if (General.isMinecrafty) 2 else 0, if(General.isMinecrafty) 1.3f else 1f) {
                screenDrawing.drawCenteredText(header, 0, 0, General.getTextThemeColor())
            }
        }
    }

    override fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean {
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            if (General.goBackEscape())
                goBack()
            else
                close()
            return true
        }
        return super.onKeyPress(key, scanCode, modifiers)
    }

    override fun getBackgroundEffectFactor(): Float {
        return blurMainAnimation.get()
    }
}