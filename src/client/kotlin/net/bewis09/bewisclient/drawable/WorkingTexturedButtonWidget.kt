package net.bewis09.bewisclient.drawable

import net.bewis09.bewisclient.core.DrawingCore
import net.bewis09.bewisclient.core.isEnabled
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.logic.BewisclientInterface
import net.bewis09.bewisclient.logic.color.color
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ButtonTextures
import net.minecraft.client.gui.widget.TexturedButtonWidget

/**
 * A bit of trivia: When I first made this class, I named it TexturedButtonWidgetThatFuckingWorksBecauseMojangMadeTheirsWeird
 * Like seriously why tf does the texture need to be like a real sprite or something I just want to fucking use my own textures
 */
class WorkingTexturedButtonWidget(x: Int, y: Int, width: Int, height: Int, buttonTextures: ButtonTextures, pressAction: PressAction) : TexturedButtonWidget(x, y, width, height, buttonTextures, pressAction), BewisclientInterface {
    override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        val identifier = this.textures.get(this.isEnabled(), this.isSelected)
        ScreenDrawing(DrawingCore(context, client.textRenderer)).drawTexture(identifier, this.x, this.y, 0f, 0f, this.width, this.height, this.width, this.height, withAlpha(0xFFFFFF, this.alpha).color)
    }
}

