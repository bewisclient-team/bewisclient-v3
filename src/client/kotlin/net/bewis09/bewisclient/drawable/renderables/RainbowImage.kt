package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.minecraft.util.Identifier

class RainbowImage(val image: Identifier, val color: Int, val alpha: Float) : Hoverable() {
    val colors = listOf(
        0xCC3333, 0xCC8833, 0xCCCC33, 0x33CC66, 0x3366CC, 0x7F33A6
    )

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        for (i in 0 until 6) {
            val offsetY = i * getHeight() / 6
            screenDrawing.drawTexture(image, getX(), getY() + offsetY, 0f, offsetY.toFloat(), getWidth(), (i + 1) * getHeight() / 6 - offsetY, getWidth(), getHeight(), combine(color, colors[i], hoverAnimation["hovering"]), alpha * (1 - hoverAnimation["hovering"]) + 1f * hoverAnimation["hovering"])
        }
    }

    fun combine(colorFirst: Int, colorSecond: Int, amount: Float): Int {
        val r = ((colorFirst shr 16 and 0xFF) * (1 - amount) + (colorSecond shr 16 and 0xFF) * amount).toInt()
        val g = ((colorFirst shr 8 and 0xFF) * (1 - amount) + (colorSecond shr 8 and 0xFF) * amount).toInt()
        val b = ((colorFirst and 0xFF) * (1 - amount) + (colorSecond and 0xFF) * amount).toInt()
        return (r shl 16) or (g shl 8) or b
    }
}