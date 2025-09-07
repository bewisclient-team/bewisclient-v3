package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.within
import net.minecraft.util.Identifier

class RainbowImage(val image: Identifier, val alpha: Float) : Hoverable() {
    val colors = listOf(
        0xCC3333, 0xCC8833, 0xCCCC33, 0x33CC66, 0x3366CC, 0x7F33A6
    )

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        for (i in 0 until 6) {
            val offsetY = i * getHeight() / 6
            screenDrawing.drawTexture(image, getX(), getY() + offsetY, 0f, offsetY.toFloat(), getWidth(), (i + 1) * getHeight() / 6 - offsetY, getWidth(), getHeight(), hoverAnimation["hovering"] within (-1 to colors[i]), alpha * (1 - hoverAnimation["hovering"]) + 1f * hoverAnimation["hovering"])
        }
    }
}