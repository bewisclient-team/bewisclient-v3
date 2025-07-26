package net.bewis09.bewisclient.screen

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.interfaces.BackgroundEffectProvider
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class RenderableScreen(val renderable: Renderable) : Screen(Text.empty()) {
    override fun init() {
        renderable(0, 0, width, height)
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        renderable.render(ScreenDrawing(context, textRenderer), mouseX, mouseY)
    }

    override fun renderDarkening(context: DrawContext, x: Int, y: Int, width: Int, height: Int) {
        if (renderable is BackgroundEffectProvider) {
            context.fill(x,y, x + width, y + height, 0x000000 or (renderable.getBackgroundEffectFactor() * 64).toInt() shl 24)
        } else {
            super.renderDarkening(context, x, y, width, height)
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return renderable.mouseClick(mouseX, mouseY, button) || super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return renderable.mouseRelease(mouseX, mouseY, button) || super.mouseReleased(mouseX, mouseY, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        return renderable.mouseDrag(mouseX, mouseY, mouseX - deltaX, mouseY - deltaY, button) || super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        return renderable.mouseScroll(mouseX, mouseY, horizontalAmount, verticalAmount) || super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return renderable.keyPress(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return renderable.keyRelease(keyCode, scanCode, modifiers) || super.keyReleased(keyCode, scanCode, modifiers)
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        return renderable.charTyped(chr, modifiers) || super.charTyped(chr, modifiers)
    }
}

