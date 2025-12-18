package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.color.Color
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.cursor.StandardCursors
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class MinecraftButton(var text: Text, val onClick: (MinecraftButton) -> Unit): Renderable() {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.drawContext.drawGuiTexture(
            RenderPipelines.GUI_TEXTURED,
            if(isMouseOver(mouseX.toDouble(), mouseY.toDouble())) Identifier.ofVanilla("widget/button_highlighted") else Identifier.ofVanilla("widget/button"),
            x,y, width, height,
            -1
        )
        screenDrawing.drawCenteredTextWithShadow(text, centerX, centerY - 4, Color.WHITE)
        if (isMouseOver(mouseX.toDouble(), mouseY.toDouble())) {
            screenDrawing.setCursor(StandardCursors.POINTING_HAND)
        }
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        onClick(this)
        playClickSound()
        return true
    }
}