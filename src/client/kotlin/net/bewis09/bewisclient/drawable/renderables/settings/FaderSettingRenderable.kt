package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.Translations
import net.bewis09.bewisclient.drawable.renderables.Fader
import net.bewis09.bewisclient.drawable.renderables.ImageButton
import net.bewis09.bewisclient.drawable.renderables.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.number.Precision
import net.bewis09.bewisclient.settings.types.Setting
import net.minecraft.util.Identifier

open class FaderSettingRenderable<T : Number>(val title: Translation, val description: Translation?, val setting: Setting<T>, val precision: Precision, val parser: (original: Float) -> T) : TooltipHoverable(description) {
    val fader = Fader(
        value = { setting.get().toFloat() }, onChange = { value ->
            setting.set(parser(value))
        }, precision = precision
    )

    val resetButton = ImageButton(Identifier.of("bewisclient", "textures/gui/sprites/reset.png"), {
        setting.set(null)
    }, Translations.RESET).setImagePadding(2).setSize(14, 14)

    init {
        height = 22u
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.fillRounded(getX(), getY(), getWidth(), getHeight(), 5, 0xFFFFFF, hoverAnimation["hovering"] * 0.1f + 0.05f)//,0xFFFFFF, hoverAnimation["hovering"] * 0.1f + 0.05f)
        screenDrawing.push()
        screenDrawing.translate(0f, getHeight() / 2f - screenDrawing.getTextHeight() / 2f + 0.5f)
        screenDrawing.drawText(title.getTranslatedString(), getX() + 8, getY(), 0xFFFFFF, 1.0F)
        screenDrawing.pop()
        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.push()
        screenDrawing.translate(0f, getHeight() / 2f - screenDrawing.getTextHeight() / 2f)
        screenDrawing.drawRightAlignedText(precision.roundToString(setting.get().toFloat()), getX() + getWidth() - fader.getWidth() - 12 - resetButton.getWidth(), getY(), 0xFFFFFF, 1.0F)
        screenDrawing.pop()
    }

    override fun init() {
        super.init()
        addRenderable(resetButton.setPosition(getX() + getWidth() - resetButton.getWidth() - 4, getY() + 4))
        addRenderable(fader.setPosition(getX() + getWidth() - fader.getWidth() - 8 - resetButton.getWidth(), getY() + 4))
    }
}