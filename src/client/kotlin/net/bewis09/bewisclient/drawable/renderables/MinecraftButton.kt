package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.version.Identifier
import net.bewis09.bewisclient.version.drawGuiTexture
import net.bewis09.bewisclient.version.setCursorPointer
import net.minecraft.network.chat.Component

class MinecraftButton(var text: Component, val onClick: (MinecraftButton) -> Unit) : Renderable() {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.drawGuiTexture(
            if (isMouseOver(mouseX.toDouble(), mouseY.toDouble())) Identifier.withDefaultNamespace("widget/button_highlighted") else Identifier.withDefaultNamespace("widget/button"),
            x, y, width, height
        )
        screenDrawing.drawCenteredTextWithShadow(text, centerX, centerY - 4, Color.WHITE)
        if (isMouseOver(mouseX.toDouble(), mouseY.toDouble())) {
            screenDrawing.setCursorPointer()
        }
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        onClick(this)
        playClickSound()
        return true
    }
}