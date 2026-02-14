package net.bewis09.bewisclient.drawable.renderables.options_structure

import net.bewis09.bewisclient.drawable.*
import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.util.color.*
import net.bewis09.bewisclient.util.createIdentifier
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.minecraft.util.Identifier

open class ImageSettingCategory(val image: Identifier, text: Translation, setting: Array<Renderable>, enableSetting: BooleanSetting? = null) : SettingCategory(text, setting, enableSetting) {
    constructor(image: String, text: Translation, setting: Array<Renderable>, enableSetting: BooleanSetting? = null) : this(createIdentifier("bewisclient", "textures/gui/functionalities/$image.png"), text, setting, enableSetting)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)

        val s = (state["state"].coerceAtLeast(hoverAnimation["hovering"] / 3)) * 1f

        val textHeight = (screenDrawing.wrapText(text.getTranslatedString(), width - 10).size - 1) * screenDrawing.getTextHeight()

        screenDrawing.fillWithBorderRounded(x, y, width, height, 5, OptionsMenuSettings.getThemeColor(black = (state["state"] + 0.5f) / 1.5f, alpha = hoverAnimation["hovering"] * 0.15f + 0.15f), OptionsMenuSettings.getThemeColor(alpha = hoverAnimation["hovering"] * 0.15f + 0.15f))

        val t = 1 - (1 - s) / 2.5f

        screenDrawing.pushColor(t, t, t, 1f)
        screenDrawing.drawCenteredWrappedText(text.getTranslatedString(), centerX, y2 - 27 - textHeight / 3 * 2, width - 10, OptionsMenuSettings.getThemeColor(white = state["state"] / 2))

        screenDrawing.drawTexture(image, centerX - 20, y + 14, 40, 40, OptionsMenuSettings.getThemeColor(white = state["state"]))

        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.popColor()
    }
}

open class DescriptionSettingCategory(text: Translation, val description: Translation, setting: Array<Renderable>, enableSetting: BooleanSetting? = null) : SettingCategory(text, setting, enableSetting) {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)

        val s = (state["state"].coerceAtLeast(hoverAnimation["hovering"] / 3)) * 1f

        val textHeight = (screenDrawing.wrapText(text.getTranslatedString(), width - 10).size - 1) * screenDrawing.getTextHeight()
        val descriptionHeight = (screenDrawing.wrapText(description.getTranslatedString(), width - 10).size - 1) * screenDrawing.getTextHeight()

        screenDrawing.fillWithBorderRounded(x, y, width, height, 5, OptionsMenuSettings.getThemeColor(black = (state["state"] + 0.5f) / 1.5f, alpha = hoverAnimation["hovering"] * 0.15f + 0.15f), OptionsMenuSettings.getThemeColor(alpha = hoverAnimation["hovering"] * 0.15f + 0.15f))

        val t = 1 - (1 - s) / 2.5f

        screenDrawing.pushColor(t, t, t, 1f)

        screenDrawing.drawCenteredWrappedText(text.getTranslatedString(), centerX, y + 14 - textHeight / 2, width - 10, OptionsMenuSettings.getThemeColor(state["state"] / 2))
        screenDrawing.drawCenteredWrappedText(description.getTranslatedString(), centerX, y2 - 42 - descriptionHeight / 2, width - 10, OptionsMenuSettings.getThemeColor(state["state"] / 2, 0.65f))

        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.popColor()
    }
}

abstract class SettingCategory(val text: Translation, val setting: Array<Renderable>, val enableSetting: BooleanSetting?) : Hoverable() {
    val state = Animator(200, Animator.EASE_IN_OUT, "state" to if (enableSetting?.get() != false) 1f else 0f)

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (setting.isEmpty()) {
            enableSetting?.toggle()
            resize()
            return true
        }

        OptionScreen.currentInstance?.openPage(
            getHeader(), getPane(), enableSetting
        )

        return true
    }

    fun getHeader(): Renderable {
        return Plane { x, y, width, height -> listOf(TextElement(text(), OptionsMenuSettings.getTextThemeColor(), centered = true)(x, y, width, 13)) }.setHeight(14)
    }

    open fun getPane(): Renderable {
        return VerticalAlignScrollPlane(setting.toList(), 1)
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        state["state"] = if (enableSetting?.get() != false) 1f else 0f
        super.render(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        super.init()
        if (enableSetting == null) return
        addRenderable(
            TooltipHoverableText(
                if (enableSetting.get()) Translations.ENABLED() else Translations.DISABLED(), 0xAAAAAA.color, Color.WHITE, if (enableSetting.get()) Translations.CLICK_TO_DISABLE() else Translations.CLICK_TO_ENABLE(), true
            ) { enableSetting.toggle(); resize() }(
                x, y2 - 14, width, 14
            )
        )
    }
}