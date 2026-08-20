package net.bewis09.bewisclient.drawable.renderables.components.button

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.logic.Color
import net.minecraft.network.chat.Component

open class ImageButton(val image: Identifier, val onClick: (ImageButton) -> Unit, tooltip: Component? = null, val small: Boolean = false) : TooltipHoverable(tooltip) {
    constructor(image: Identifier, onClick: (ImageButton) -> Unit) : this(image, onClick, null, false)

    var imageColor: () -> Color = { Color.WHITE }
    var imagePadding: Int = 8

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        usePointer(screenDrawing, mouseX, mouseY)
        SelectiveScreenDrawer.renderButtonBackground(screenDrawing, hoverFactor, 0f, x, y, width, height, 1f, mouseX, mouseY, small = small)
        screenDrawing.drawTexture(image, x + imagePadding, y + imagePadding, width - imagePadding * 2, height - imagePadding * 2, General.getTextThemeColor())
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean = onClick(this).let { true }

    fun setImageColor(color: () -> Color): ImageButton = apply { imageColor = color }

    fun setImagePadding(padding: Int): ImageButton = apply { imagePadding = padding }
}