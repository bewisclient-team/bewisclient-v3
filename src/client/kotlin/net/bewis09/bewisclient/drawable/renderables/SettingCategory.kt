package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.minecraft.util.Identifier

class SettingCategory(val image: String, val text: Translation, val setting: Array<Renderable>): Hoverable() {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)

        val height = (screenDrawing.wrapText(text.getTranslatableString(), getWidth() - 10).size - 1) * screenDrawing.getTextHeight()

        screenDrawing.drawTexture(Identifier.of("bewisclient", "textures/gui/settings/$image.png"), getX() + getWidth() / 2 - 20, getY() + 14, 40, 40, 0xFFFFFF, 1f)

        screenDrawing.fillRounded(getX(), getY(), getWidth(), getHeight(), 5, 0xFFFFFF, hoverAnimation["hovering"] * 0.15f + 0.15f)
        screenDrawing.drawCenteredWrappedText(text.getTranslatableString(), getX() + getWidth() / 2, getY() + getHeight() - 20 - height / 2, getWidth() - 10, -1)
    }
}