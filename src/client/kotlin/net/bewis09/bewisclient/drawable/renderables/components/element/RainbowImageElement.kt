package net.bewis09.bewisclient.drawable.renderables.components.element

import net.bewis09.bewisclient.drawable.ImageIdentifier.iconIdentifier
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.not
import net.bewis09.renderite.logic.within
import net.bewis09.renderite.style.RenderiteChild

val colors = listOf(!0xCC3333, !0xCC8833, !0xCCCC33, !0x33CC66, !0x3366CC, !0x7F33A6)

@RenderiteChild
fun Renderable.RainbowImage() = Rectangle {
    width = 120
    height = 22
    foreground = {
        for (i in 0 until 6) {
            val offsetY = i * height / 6
            it.drawTextureRegion(iconIdentifier, x, y + offsetY, 0f, offsetY.toFloat(), width, (i + 1) * height / 6 - offsetY, width, height / 6, width, height, hoverFactor within (Color.WHITE to colors[i]) alpha 0.5f * (1 - hoverFactor) + 1f * hoverFactor)
        }
    }
}