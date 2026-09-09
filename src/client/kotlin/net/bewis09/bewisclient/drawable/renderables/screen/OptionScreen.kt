package net.bewis09.bewisclient.drawable.renderables.screen

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.then
import net.bewis09.bewisclient.drawable.BackgroundEffectProvider
import net.bewis09.bewisclient.drawable.ImageIdentifier.setRenderableScreen
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.ImageButton
import net.bewis09.bewisclient.drawable.renderables.components.button.SidebarButton
import net.bewis09.bewisclient.drawable.renderables.components.button.ThemeButton
import net.bewis09.bewisclient.drawable.renderables.components.element.RainbowImage
import net.bewis09.bewisclient.drawable.renderables.components.setting.Switch
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.features.sidebar.Home
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.server.Security
import net.bewis09.bewisclient.settings.structure.SidebarFeature
import net.bewis09.bewisclient.settings.types.Setting
import net.bewis09.bewisclient.util.Bewisclient
import net.bewis09.bewisclient.version.setScreen
import net.bewis09.renderite.components.TextElement
import net.bewis09.renderite.logic.*
import net.bewis09.renderite.style.RenderiteChild
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
        if (!Security.verificationState.allowed) setRenderableScreen(VersionInvalidScreen)
        screenDrawing.setBewisclientFont()
    }

    override fun cleanup(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.setDefaultFont()
    }

    @RenderiteChild
    fun Renderable.TopButton(identifier: Identifier, padding: Int, onClick: () -> Unit) = ImageButton {
        image = identifier
        this.onClick = { onClick() }
        imagePadding = padding
        width = SelectiveScreenDrawer.getSideButtonHeight()
        height = SelectiveScreenDrawer.getSideButtonHeight()
    }

    override fun init() {
        Div(0) {
            padding = 30
            cacheChildren = true
            gap = 4
            direction = Direction.HORIZONTAL
            fitType = FitType.FILL_ITEM
            onInit = {
                Div {
                    cacheChildren = true
                    gap = 5
                    fitType = FitType.FILL_ITEM
                    width = 134
                    padding = 7
                    background = { SelectiveScreenDrawer.renderMenuBackground(it, x, y, width, height) }
                    onInit = {
                        Div {
                            gap = (General.isMinecrafty then 2) ?: 5
                            fitType = FitType.SCROLL
                            fillParent = true
                            cacheChildren = true
                            onInit = {
                                Div {
                                    gap = if (General.isMinecrafty) 1 else 5
                                    direction = Direction.HORIZONTAL
                                    fitType = FitType.FILL_ITEM
                                    cacheChildren = true
                                    onInit = {
                                        TopButton(backIdentifier, 1, ::goBack)
                                        SidebarButton(Home) { fillParent = true }
                                        TopButton(closeIdentifier, 3, ::close)
                                    }
                                }.updateHeight(SelectiveScreenDrawer.getSideButtonHeight())
                                HorizontalLine { backgroundColor = { General.getThemeColor(alpha = 0.3f) } }
                                APIEntrypointLoader.mapEntrypoint { a -> a.getSidebarCategories().forEach(::SidebarButton) }
                                HorizontalLine { backgroundColor = { General.getThemeColor(alpha = 0.3f) } }
                                ThemeButton {
                                    text = editHudTranslation()
                                    onClick = { alphaMainAnimation.set(0f) { Bewisclient.setRenderableScreen(HudEditScreen()) } }
                                }
                            }
                        }
                        RainbowImage()
                    }
                }
                Div {
                    gap = 5
                    fitType = FitType.FILL_ITEM
                    fillParent = true
                    padding = 7
                    background = { SelectiveScreenDrawer.renderMenuBackground(it, x, y, width, height) }
                    onInit = {
                        page.header.add()
                        Div {
                            fillParent = true
                            cacheChildren = true
                            fitType = FitType.FIT
                            onInit = { page.pane.add() }
                        }
                    }
                }
            }
        }(x, y, width, height)

        if (page.setting != null) {
            Switch {
                colorModifier = { Color(1f, 1f, 1f, insideMainAnimation.get()) }
                state = { page.setting?.get() ?: false }
                onChange = { page.setting?.set(it) }
            }.updatePosition(width - 61, 37)
        }

        VersionText()
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
            if (category != Home)
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

    inner class Page(header: Component, val pane: Renderable, val setting: Setting<Boolean>? = null) {
        val header: Renderable = TextElement {
            text = header
            fontSize = if (General.isMinecrafty) 12f else 9f
            textAlign = TextAlign.CENTER
            height = if (General.isMinecrafty) 18 else 14
        }

        init {
            this.header.colorModifier = { Color(1f, 1f, 1f, insideMainAnimation.get()) }
            pane.colorModifier = { Color(1f, 1f, 1f, insideMainAnimation.get()) }
        }
    }

    override fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean {
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            if (General.goBackEscape()) goBack() else close()
            return true
        }
        return super.onKeyPress(key, scanCode, modifiers)
    }

    override fun getBackgroundEffectFactor(): Float = blurMainAnimation.get()
}