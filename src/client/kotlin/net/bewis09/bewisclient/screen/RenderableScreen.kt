package net.bewis09.bewisclient.screen

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.interfaces.BackgroundEffectProvider
import net.minecraft.client.gui.Click
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.input.CharInput
import net.minecraft.client.input.KeyInput
import net.minecraft.text.Text

class RenderableScreen(val renderable: Renderable) : Screen(Text.empty()) {
    var startX = 0.0
    var startY = 0.0

    override fun init() {
        renderable(0, 0, width, height)
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        val screenDrawing = ScreenDrawing(context, textRenderer)
        renderable.render(screenDrawing, mouseX, mouseY)
        screenDrawing.runAfterDraw()
    }

    override fun renderDarkening(context: DrawContext, x: Int, y: Int, width: Int, height: Int) {
        if (renderable is BackgroundEffectProvider) {
            context.fill(x, y, x + width, y + height, 0x000000 or (renderable.getBackgroundEffectFactor() * 64).toInt() shl 24)
        } else {
            super.renderDarkening(context, x, y, width, height)
        }
    }

    override fun mouseClicked(arg: Click, bl: Boolean): Boolean {
        startX = arg.x
        startY = arg.y
        return renderable.mouseClick(arg.x, arg.y, arg.keycode) || super.mouseClicked(arg, bl)
    }

    override fun mouseReleased(arg: Click): Boolean {
        return renderable.mouseRelease(arg.x, arg.y, arg.keycode) || super.mouseReleased(arg)
    }

    override fun mouseDragged(arg: Click, d: Double, e: Double): Boolean {
        return renderable.mouseDrag(arg.x, arg.y, startX, startY, arg.keycode) || super.mouseDragged(arg, d, e)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        return renderable.mouseScroll(mouseX, mouseY, horizontalAmount, verticalAmount) || super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }

    override fun keyPressed(arg: KeyInput): Boolean {
        return renderable.keyPress(arg.key, arg.scancode, arg.modifiers) || super.keyPressed(arg)
    }

    override fun keyReleased(arg: KeyInput): Boolean {
        return renderable.keyRelease(arg.key, arg.scancode, arg.modifiers) || super.keyReleased(arg)
    }

    override fun charTyped(arg: CharInput): Boolean {
        return renderable.charTyped(arg.codepoint.toChar(), arg.modifiers) || super.charTyped(arg)
    }
}

