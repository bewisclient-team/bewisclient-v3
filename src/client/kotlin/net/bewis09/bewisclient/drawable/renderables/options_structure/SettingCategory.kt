package net.bewis09.bewisclient.drawable.renderables.options_structure

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.Translations
import net.bewis09.bewisclient.drawable.renderables.Hoverable
import net.bewis09.bewisclient.drawable.renderables.Plane
import net.bewis09.bewisclient.drawable.renderables.Text
import net.bewis09.bewisclient.drawable.renderables.TooltipHoverableText
import net.bewis09.bewisclient.drawable.renderables.VerticalAlignScrollPlane
import net.bewis09.bewisclient.settings.types.BooleanSetting
import net.minecraft.util.Identifier

open class ImageSettingCategory(val image: Identifier, text: Translation, setting: Array<Renderable>, enableSetting: BooleanSetting? = null): SettingCategory(text, setting, enableSetting) {
    constructor(image: String, text: Translation, setting: Array<Renderable>, enableSetting: BooleanSetting? = null): this(Identifier.of("bewisclient", "textures/gui/functionalities/$image.png"), text, setting, enableSetting)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)

        val s = (state["state"].coerceAtLeast(hoverAnimation["hovering"])) * 1f

        val height = (screenDrawing.wrapText(text.getTranslatedString(), getWidth() - 10).size - 1) * screenDrawing.getTextHeight()

        screenDrawing.pushColor(s, s, s, 1f)
        screenDrawing.fillRounded(getX(), getY(), getWidth(), getHeight(), 5, 0xFFFFFF, hoverAnimation["hovering"] * 0.15f + 0.15f )

        screenDrawing.popColor()

        val t = 1 - (1 - s) / 2.5f

        screenDrawing.pushColor(t, t, t, 1f)
        screenDrawing.drawCenteredWrappedText(text.getTranslatedString(), getX() + getWidth() / 2, getY() + getHeight() - 27 - height / 3, getWidth() - 10, -1)

        screenDrawing.drawTexture(image, getX() + getWidth() / 2 - 20, getY() + 14, 40, 40, 0xFFFFFF, 1f)

        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.popColor()
    }
}

open class DescriptionSettingCategory(text: Translation, val description: Translation, setting: Array<Renderable>, enableSetting: BooleanSetting? = null): SettingCategory(text, setting, enableSetting) {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)

        val s = (state["state"].coerceAtLeast(hoverAnimation["hovering"])) * 1f

        val height = (screenDrawing.wrapText(text.getTranslatedString(), getWidth() - 10).size - 1) * screenDrawing.getTextHeight()
        val descriptionHeight = (screenDrawing.wrapText(description.getTranslatedString(), getWidth() - 10).size - 1) * screenDrawing.getTextHeight()

        screenDrawing.pushColor(s, s, s, 1f)
        screenDrawing.fillRounded(getX(), getY(), getWidth(), getHeight(), 5, 0xFFFFFF, hoverAnimation["hovering"] * 0.15f + 0.15f)
        screenDrawing.popColor()

        val t = 1 - (1 - s) / 2.5f

        screenDrawing.pushColor(t, t, t, 1f)

        screenDrawing.drawCenteredWrappedText(text.getTranslatedString(), getX() + getWidth() / 2, getY() + 14 - height / 2, getWidth() - 10, -1)
        screenDrawing.drawCenteredWrappedText(description.getTranslatedString(), getX() + getWidth() / 2, getY() + getHeight() - 42 - descriptionHeight / 2, getWidth() - 10, 0xFFAAAAAA)

        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.popColor()
    }
}

abstract class SettingCategory(val text: Translation, val setting: Array<Renderable>, val enableSetting: BooleanSetting?): Hoverable() {
    val state = Animator(200, Animator.EASE_IN_OUT, "state" to if (enableSetting?.get() != false) 1f else 0f)

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        OptionScreen.currentInstance?.transformInside(
            getHeader(),
            getPane(),
            enableSetting
        )

        return true
    }

    fun getHeader(): Renderable {
        return Plane { x, y, width, height -> listOf(Text(text.getTranslatedString(), centered = true)(x, y, width, 13)) }.setHeight(14)
    }

    fun getPane(): Renderable {
        return VerticalAlignScrollPlane({ setting.toList() }, 1)
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        state["state"] = if (enableSetting?.get() != false) 1f else 0f
        super.render(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        super.init()
        if (enableSetting == null) return
        addRenderable(TooltipHoverableText(
            if (enableSetting.get()) Translations.ENABLED else Translations.DISABLED,
            0xAAAAAA,
            0xFFFFFF,
            if (enableSetting.get()) Translations.CLICK_TO_DISABLE else Translations.CLICK_TO_ENABLE,
            true
        ) { enableSetting.toggle(); resize() }(
            getX(),
            getY() + getHeight() - 14,
            getWidth(),
            14
        ))
    }
}