package net.bewis09.bewisclient.drawable.renderables.components.button

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.logic.Color

open class ImageButton(p: Props<ImageButton>) : TooltipHoverable<ImageButton>(p + {
    shouldUsePointer = true
}) {
    lateinit var image: Identifier
    var onClick: (ImageButton) -> Unit = {}
    var small: Boolean = false
    var imageColor: () -> Color = { General.getTextThemeColor() }
    var imagePadding: Int = 8

    init { props() }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderButtonBackground(screenDrawing, hoverFactor, 0f, x, y, width, height, 1f, mouseX, mouseY, small = small)
    }

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.drawTexture(image, x + imagePadding, y + imagePadding, width - imagePadding * 2, height - imagePadding * 2, imageColor())
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean = onClick(this).let { true }
}