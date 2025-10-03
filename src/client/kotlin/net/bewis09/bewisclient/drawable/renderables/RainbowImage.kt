package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.core.BewisclientID
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.logic.color.*

class RainbowImage(val image: BewisclientID, val alpha: Float) : Hoverable() {
    val colors = listOf(
        0xCC3333.color, 0xCC8833.color, 0xCCCC33.color, 0x33CC66.color, 0x3366CC.color, 0x7F33A6.color
    )

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        for (i in 0 until 6) {
            val offsetY = i * height / 6
            screenDrawing.drawTextureRegion(image, x, y + offsetY, 0f, offsetY.toFloat(), width, (i + 1) * height / 6 - offsetY, width, height / 6, width, height, hoverAnimation["hovering"] within (Color.WHITE to colors[i]) alpha alpha * (1 - hoverAnimation["hovering"]) + 1f * hoverAnimation["hovering"])
        }
    }
}