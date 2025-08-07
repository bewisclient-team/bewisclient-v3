package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.drawable.renderables.Fader
import net.bewis09.bewisclient.drawable.renderables.Hoverable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.number.Precision
import net.bewis09.bewisclient.settings.types.Setting

open class FaderSettingRenderable<T: Number>(val title: Translation, val description: Translation?, val setting: Setting<T>, val precision: Precision, val parser: (original: Float) -> T): Hoverable() {
    val fader = Fader(setting.get().toFloat(), precision) { new ->
        setting.set(parser(new))
    }

    init {
        height = 22u
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.fillRounded(getX(), getY(), getWidth(), getHeight(), 5, 0xFFFFFF, hoverAnimation["hovering"] * 0.1f + 0.05f)//,0xFFFFFF, hoverAnimation["hovering"] * 0.1f + 0.05f)
        screenDrawing.push()
        screenDrawing.translate(0f,getHeight() / 2f - screenDrawing.getTextHeight() / 2f + 0.5f)
        screenDrawing.drawText(title.getTranslatedString(), getX() + 8, getY(), 0xFFFFFF, 1.0F)
        screenDrawing.pop()
        renderRenderables(screenDrawing, mouseX, mouseY)
        screenDrawing.push()
        screenDrawing.translate(0f,getHeight() / 2f - screenDrawing.getTextHeight() / 2f)
        screenDrawing.drawRightAlignedText(precision.roundToString(fader.value), getX() + getWidth() - fader.getWidth() - 8, getY(), 0xFFFFFF, 1.0F)
        screenDrawing.pop()
    }

    override fun init() {
        addRenderable(fader.setPosition(getX() + getWidth() - fader.getWidth() - 4, getY() + 4))
    }
}