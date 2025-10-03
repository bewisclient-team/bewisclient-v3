package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.core.BewisclientID
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.color.Color

open class ImageButton(val image: BewisclientID, val onClick: (ImageButton) -> Unit, tooltip: Translation?) : TooltipHoverable(tooltip) {
    constructor(image: BewisclientID, onClick: (ImageButton) -> Unit) : this(image, onClick, null)

    var imageColor: Color = Color.WHITE
    var imagePadding: Int = 8

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)

        screenDrawing.fillRounded(x, y, width, height, 5, OptionsMenuSettings.themeColor.get().getColor() alpha (hoverAnimation["hovering"] * 0.15f + 0.15f))

        screenDrawing.drawTexture(image, x + imagePadding, y + imagePadding, width - imagePadding * 2, height - imagePadding * 2, imageColor)
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        onClick(this)
        return true
    }

    fun setImageColor(color: Color): ImageButton {
        this.imageColor = color
        return this
    }

    fun setImagePadding(padding: Int): ImageButton {
        this.imagePadding = padding
        return this
    }
}