package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.logic.color.alpha
import net.bewis09.bewisclient.logic.color.color
import net.bewis09.bewisclient.logic.color.within
import net.minecraft.util.Identifier
import java.awt.Color

class RainbowImage(val image: Identifier, val alpha: Float) : Hoverable() {
    val colors = listOf(
        0xCC3333.color, 0xCC8833.color, 0xCCCC33.color, 0x33CC66.color, 0x3366CC.color, 0x7F33A6.color
    )

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        for (i in 0 until 6) {
            val offsetY = i * getHeight() / 6
            screenDrawing.drawTexture(image, getX(), getY() + offsetY, 0f, offsetY.toFloat(), getWidth(), (i + 1) * getHeight() / 6 - offsetY, getWidth(), getHeight(), hoverAnimation["hovering"] within (Color.WHITE to colors[i]) alpha alpha * (1 - hoverAnimation["hovering"]) + 1f * hoverAnimation["hovering"])
        }
    }
}