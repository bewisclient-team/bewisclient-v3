package net.bewis09.bewisclient.drawable.renderables.components.button

import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.minecraft.network.chat.Component

class Button(var text: Component, val onClick: (Button) -> Unit, tooltip: Component? = null, val selected: (() -> Boolean)? = null, var dark: Boolean = false, val small: Boolean = false) : TooltipHoverable(tooltip) {
    constructor(text: Component, onClick: (Button) -> Unit) : this(text, onClick, null)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        SelectiveScreenDrawer.renderButtonBackground(screenDrawing, hoverAnimation.get(), if (selected?.invoke() == true) 1f else 0f, x, y, width, height, 1f, mouseX, mouseY, dark, small)
        usePointer(screenDrawing, mouseX, mouseY)
        screenDrawing.drawCenteredText(text, exactCenterX, screenDrawing.getTextYCenter(this), General.getTextThemeColor())
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean = onClick(this).let { true }
}