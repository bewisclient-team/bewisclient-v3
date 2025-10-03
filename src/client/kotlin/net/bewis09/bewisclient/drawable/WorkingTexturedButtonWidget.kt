package net.bewis09.bewisclient.drawable

import net.bewis09.bewisclient.core.BewisclientID
import net.bewis09.bewisclient.core.DrawingCore
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.logic.BewisclientInterface
import net.bewis09.bewisclient.logic.color.color
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.screen.ScreenTexts

/**
 * A bit of trivia: When I first made this class, I named it TexturedButtonWidgetThatFuckingWorksBecauseMojangMadeTheirsWeird
 * Like seriously why tf does the texture need to be like a real sprite or something I just want to fucking use my own textures
 */
class WorkingTexturedButtonWidget(x: Int, y: Int, width: Int, height: Int, val normalTexture: BewisclientID, val selectedTexture: BewisclientID, pressAction: PressAction) : ButtonWidget(x, y, width, height, ScreenTexts.EMPTY, pressAction, DEFAULT_NARRATION_SUPPLIER), BewisclientInterface {
    override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        ScreenDrawing(DrawingCore(context, client.textRenderer)).drawTexture(if(this.isSelected) selectedTexture else normalTexture, this.x, this.y, 0f, 0f, this.width, this.height, this.width, this.height, withAlpha(0xFFFFFF, this.alpha).color)
    }
}

