package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.minecraft.util.Identifier
import java.awt.Color

open class Image(val image: Identifier, val color: Color) : Renderable() {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.drawTexture(image, getX(), getY(), getWidth(), getHeight(), color)
    }
}