package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.drawable.renderables.Hoverable
import net.bewis09.bewisclient.drawable.renderables.Switch
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.settings.types.Setting

class BooleanSettingRenderable(val title: Translation, val description: Translation?, val setting: Setting<Boolean>): Hoverable() {
    val switch = Switch(setting.get()) { new ->
        setting.set(new)
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
    }

    override fun init() {
        addRenderable(switch.setPosition(getX() + getWidth() - switch.getWidth() - 4, getY() + 4))
    }
}