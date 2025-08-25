package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.minecraft.util.Identifier

open class Image(val image: Identifier, val color: Int, val alpha: Float) : Renderable() {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.drawTexture(image, getX(), getY(), getWidth(), getHeight(), color, alpha)
    }
}