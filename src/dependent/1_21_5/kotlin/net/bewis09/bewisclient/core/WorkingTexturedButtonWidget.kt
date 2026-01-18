package net.bewis09.bewisclient.core

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.color.alpha
import net.bewis09.bewisclient.util.logic.BewisclientInterface
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.util.Identifier

/**
 * A bit of trivia: When I first made this class, I named it TexturedButtonWidgetThatFuckingWorksBecauseMojangMadeTheirsWeird
 * Like seriously why tf does the texture need to be like a real sprite or something I just want to fucking use my own textures
 */
open class WorkingTexturedButtonWidget(x: Int, y: Int, width: Int, height: Int, val normalTexture: Identifier, val selectedTexture: Identifier, pressAction: PressAction, val text: net.minecraft.text.Text?) : ButtonWidget(x, y, width, height, ScreenTexts.EMPTY, pressAction, DEFAULT_NARRATION_SUPPLIER), BewisclientInterface {
    constructor(x: Int, y: Int, width: Int, height: Int, normalTexture: Identifier, selectedTexture: Identifier, pressAction: PressAction) : this(x, y, width, height, normalTexture, selectedTexture, pressAction, null)

    override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        ScreenDrawing(context, MinecraftClient.getInstance().textRenderer).drawTexture(if(this.isSelected) selectedTexture else normalTexture, this.x, this.y, 0f, 0f, this.width, this.height, this.width, this.height, (0xFFFFFF alpha this.alpha))
        text?.let { context.drawCenteredTextWithShadow(client.textRenderer, text, this.x + this.width / 2, this.y + 5, -0x1) }
    }
}

