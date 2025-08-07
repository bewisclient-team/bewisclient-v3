package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.screen.OptionScreen
import net.minecraft.util.Identifier

class ImageSettingCategory(val image: Identifier, text: Translation, setting: Array<Renderable>): SettingCategory(text, setting) {
    constructor(image: String, text: Translation, setting: Array<Renderable>): this(Identifier.of("bewisclient", "textures/gui/settings/$image.png"), text, setting)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)

        val height = (screenDrawing.wrapText(text.getTranslatedString(), getWidth() - 10).size - 1) * screenDrawing.getTextHeight()

        screenDrawing.drawTexture(image, getX() + getWidth() / 2 - 20, getY() + 14, 40, 40, 0xFFFFFF, 1f)

        screenDrawing.fillRounded(getX(), getY(), getWidth(), getHeight(), 5, 0xFFFFFF, hoverAnimation["hovering"] * 0.15f + 0.15f)
        screenDrawing.drawCenteredWrappedText(text.getTranslatedString(), getX() + getWidth() / 2, getY() + getHeight() - 20 - height / 2, getWidth() - 10, -1)
    }
}

class DescriptionSettingCategory(text: Translation, val description: Translation, setting: Array<Renderable>): SettingCategory(text, setting) {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)

        val height = (screenDrawing.wrapText(text.getTranslatedString(), getWidth() - 10).size - 1) * screenDrawing.getTextHeight()
        val descriptionHeight = (screenDrawing.wrapText(description.getTranslatedString(), getWidth() - 10).size - 1) * screenDrawing.getTextHeight()

        screenDrawing.fillRounded(getX(), getY(), getWidth(), getHeight(), 5, 0xFFFFFF, hoverAnimation["hovering"] * 0.15f + 0.15f)
        screenDrawing.drawCenteredWrappedText(text.getTranslatedString(), getX() + getWidth() / 2, getY() + 15 - height / 2, getWidth() - 10, -1)
        screenDrawing.drawCenteredWrappedText(description.getTranslatedString(), getX() + getWidth() / 2, getY() + getHeight() - 40 - descriptionHeight / 2, getWidth() - 10, 0xFFAAAAAA)
    }
}

abstract class SettingCategory(val text: Translation, val setting: Array<Renderable>): Hoverable() {
    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        OptionScreen.currentInstance?.transformInside(
            Text(text.getTranslatedString(), centered = true).setHeight(12),
            VerticalAlignScrollPlane({ setting.toList() }, 1)
        )

        return true
    }
}