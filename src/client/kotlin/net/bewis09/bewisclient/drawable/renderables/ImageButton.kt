package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.screen_drawing.RgbColor
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.minecraft.util.Identifier

class ImageButton(val image: Identifier, val onClick: (ImageButton) -> Unit, tooltip: Translation?) : TooltipHoverable(tooltip) {
    constructor(image: Identifier, onClick: (ImageButton) -> Unit) : this(image, onClick, null)

    var imageColor: Int = 0xFFFFFF
    var imageAlpha: Float = 1.0f
    var imagePadding: Int = 8

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)

        screenDrawing.fillRounded(getX(), getY(), getWidth(), getHeight(), 5, 0xFFFFFF, hoverAnimation["hovering"] * 0.15f + 0.15f)

        screenDrawing.drawTexture(image, getX() + imagePadding, getY() + imagePadding, getWidth() - imagePadding * 2, getHeight() - imagePadding * 2, imageColor, imageAlpha)
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        onClick(this)
        return true
    }

    fun setImageColor(@RgbColor color: Int): ImageButton {
        this.imageColor = color
        return this
    }

    fun setImageAlpha(alpha: Float): ImageButton {
        this.imageAlpha = alpha
        return this
    }

    fun setImagePadding(padding: Int): ImageButton {
        this.imagePadding = padding
        return this
    }
}