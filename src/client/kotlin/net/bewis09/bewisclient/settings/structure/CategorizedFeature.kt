package net.bewis09.bewisclient.settings.structure

import net.bewis09.bewisclient.common.Color
import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.element.TextElement
import net.bewis09.bewisclient.drawable.renderables.components.element.TooltipHoverableText
import net.bewis09.bewisclient.drawable.renderables.components.logic.Hoverable
import net.bewis09.bewisclient.drawable.renderables.components.structure.Plane
import net.bewis09.bewisclient.drawable.renderables.components.structure.VerticalAlignScrollPlane
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.renderables.settings.BooleanSettingRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.pushColor
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.logic.RenderableCreator
import net.bewis09.bewisclient.settings.types.ColorSetting
import net.bewis09.bewisclient.settings.types.FloatSetting
import net.bewis09.bewisclient.settings.types.Setting

abstract class CategorizedFeature(id: Identifier, titleText: String) : Feature(id) {
    companion object {
        val clickToEnableText = Translation("menu.general.enable", "Click to Enable")
        val clickToDisableText = Translation("menu.general.disable", "Click to Disable")
        val enabledText = Translation("menu.general.enabled", "Enabled")
        val disabledText = Translation("menu.general.disabled", "Disabled")
    }

    val title = Translation(id.namespace, "category.${id.path}", titleText)

    private val settingAppliers: MutableList<(ArrayList<Renderable>) -> Unit> = mutableListOf()

    open val enabledByDefault = false
    val enabledSetting = boolean("enabled", enabledByDefault) { oldValue, newValue -> enabledListener(oldValue, newValue) }
    var enabled by enabledSetting

    fun getSettingRenderables(): Array<Renderable> = arrayListOf<Renderable>().also(::appendSettingsRenderables).toTypedArray()

    open fun appendSettingsRenderables(list: ArrayList<Renderable>) {
        settingAppliers.forEach { it(list) }
    }

    fun <T> T.menu(title: String, description: String? = null, quickSetting: Boolean = false): T where T : Setting<*>, T : RenderableCreator<*> {
        settingAppliers.add { it.menu(this, title, description, quickSetting) }
        return this
    }

    infix fun <T> T.menu(title: String): T where T : Setting<*>, T : RenderableCreator<*> {
        return this.menu(title, null, false)
    }

    infix fun <T> T.menuQuick(title: String): T where T : Setting<*>, T : RenderableCreator<*> {
        return this.menu(title, null, true)
    }

    infix fun <T> T.menu(data: Pair<String, String>): T where T : Setting<*>, T : RenderableCreator<*> {
        return this.menu(data.first, data.second, false)
    }

    infix fun <T> T.menuQuick(data: Pair<String, String>): T where T : Setting<*>, T : RenderableCreator<*> {
        return this.menu(data.first, data.second, true)
    }

    open fun enabledListener(oldValue: Boolean?, newValue: Boolean?) {}

    abstract fun createRenderable(): SettingCategory

    open fun getPane(): Renderable {
        return VerticalAlignScrollPlane(getSettingRenderables().toList(), 1)
    }

    abstract inner class SettingCategory : Hoverable() {
        val state = Animator({ animationDuration }, Animator.EASE_IN_OUT, if (enabled) 1f else 0f)

        init {
            BooleanSettingRenderable(enabledText, null, enabledSetting).addToQuickSettings(this@CategorizedFeature, "enabled")
        }

        override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
            if (getSettingRenderables().isEmpty()) {
                enabled = !enabled
                resize()
                return true
            }

            OptionScreen.currentInstance?.openPage(title(), getPane(), enabledSetting)

            return true
        }

        abstract fun renderContent(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int)

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            state.set(if (enabled) 1f else 0f)
            super.render(screenDrawing, mouseX, mouseY)

            val s = (state.get().coerceAtLeast(hoverFactor / 3)) * 1f

            SelectiveScreenDrawer.renderSettingsCategoryBackground(screenDrawing, x, y, width, height, state.get(), hoverFactor, mouseX, mouseY)

            val t = 1 - (1 - s) / 2.5f

            screenDrawing.translate(0f, if (isMinecrafty) -3f else 0f) {
                screenDrawing.pushColor(t, t, t, 1f) {
                    renderContent(screenDrawing, mouseX, mouseY)
                    renderRenderables(screenDrawing, mouseX, mouseY)
                }
            }
        }

        override fun init() {
            super.init()
            addRenderable(
                TooltipHoverableText(
                    if (enabled) enabledText() else disabledText(), 0xAAAAAA.color, Color.WHITE, if (enabled) clickToDisableText() else clickToEnableText(), true
                ) { enabled = !enabled; resize() }(
                    x, y2 - 14, width, 14
                )
            )
        }
    }

    fun <T> ArrayList<Renderable>.menuQuick(setting: T, title: String, description: String? = null) where T : RenderableCreator<*>, T : Setting<*> {
        return menu(setting, title, description, true)
    }

    fun <T> ArrayList<Renderable>.menu(setting: T, title: String, description: String? = null, quickSettings: Boolean = false) where T : RenderableCreator<*>, T : Setting<*> {
        val id = idLookup[setting] ?: throw IllegalArgumentException("Setting not in id lookup: $setting")
        val renderable = setting.createRenderable(this@CategorizedFeature, id, title, description)
        if (quickSettings) renderable.addToQuickSettings(this@CategorizedFeature, id)
        this.add(renderable)
    }

    fun ArrayList<Renderable>.colorAlphaMenu(setting: ColorSetting, alpha: FloatSetting, id: String, title: String, description: String? = null, quickSettingsId: String? = null) {
        val renderable = setting.createRenderableWithFader(this@CategorizedFeature, id, title, description, alpha)
        if (quickSettingsId != null) renderable.addToQuickSettings(this@CategorizedFeature, quickSettingsId)
        this.add(renderable)
    }
}