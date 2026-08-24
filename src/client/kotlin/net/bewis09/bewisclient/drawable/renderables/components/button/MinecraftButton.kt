package net.bewis09.bewisclient.drawable.renderables.components.button

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.Bewisclient
import net.bewis09.bewisclient.version.drawGuiTexture
import net.bewis09.renderite.logic.Color
import net.minecraft.network.chat.Component

class MinecraftButton(p: Props<MinecraftButton>) : PropedRenderable<MinecraftButton>(p + {
    shouldUsePointer = true
}) {
    lateinit var text: Component
    lateinit var onClick: (MinecraftButton) -> Unit

    init { props() }

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.drawGuiTexture(
            if (isMouseOver(mouseX, mouseY)) Identifier.withDefaultNamespace("widget/button_highlighted") else Identifier.withDefaultNamespace("widget/button"),
            x, y, width, height
        )
        screenDrawing.drawCenteredTextWithShadow(text, exactCenterX, exactCenterY - 4, Color.WHITE)
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        onClick(this)
        Bewisclient.playClickSound()
        return true
    }
}