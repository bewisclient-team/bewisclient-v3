package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.core.drawGuiTexture
import net.bewis09.bewisclient.core.setCursorPointer
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.color.Color
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class MinecraftButton(var text: Text, val onClick: (MinecraftButton) -> Unit): Renderable() {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.drawGuiTexture(
            if(isMouseOver(mouseX.toDouble(), mouseY.toDouble())) Identifier.ofVanilla("widget/button_highlighted") else Identifier.ofVanilla("widget/button"),
            x,y, width, height
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