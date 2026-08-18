package net.bewis09.bewisclient.drawable.renderables.components.element

import net.bewis09.bewisclient.common.Color
import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.drawable.renderables.components.logic.Hoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing

class RainbowImage(val image: Identifier, val alpha: Float) : Hoverable() {
    val colors = listOf(!0xCC3333, !0xCC8833, !0xCCCC33, !0x33CC66, !0x3366CC, !0x7F33A6)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        for (i in 0 until 6) {
            val offsetY = i * height / 6
            screenDrawing.drawTextureRegion(image, x, y + offsetY, 0f, offsetY.toFloat(), width, (i + 1) * height / 6 - offsetY, width, height / 6, width, height, hoverFactor within (Color.WHITE to colors[i]) alpha alpha * (1 - hoverFactor) + 1f * hoverFactor)
        }
    }
}