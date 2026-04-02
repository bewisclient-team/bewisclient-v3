package net.bewis09.bewisclient.core

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.color.alpha
import net.bewis09.bewisclient.util.logic.BewisclientInterface
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

/**
 * A bit of trivia: When I first made this class, I named it TexturedButtonWidgetThatFuckingWorksBecauseMojangMadeTheirsWeird
 * Like seriously why tf does the texture need to be like a real sprite or something I just want to fucking use my own textures
 */
open class WorkingTexturedButtonWidget(x: Int, y: Int, width: Int, height: Int, val normalTexture: Identifier, val selectedTexture: Identifier, pressAction: OnPress, val text: Component?) : Button(x, y, width, height, CommonComponents.EMPTY, pressAction, DEFAULT_NARRATION), BewisclientInterface {
    constructor(x: Int, y: Int, width: Int, height: Int, normalTexture: Identifier, selectedTexture: Identifier, pressAction: OnPress) : this(x, y, width, height, normalTexture, selectedTexture, pressAction, null)

    override fun renderWidget(context: GuiGraphics, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        ScreenDrawing(context, client.font).drawTexture(if(this.isHovered) selectedTexture else normalTexture, this.x, this.y, 0f, 0f, this.width, this.height, this.width, this.height, (0xFFFFFF alpha this.alpha))
        text?.let { context.drawCenteredString(client.font, text, this.x + this.width / 2, this.y + 5, -0x1) }
    }
}