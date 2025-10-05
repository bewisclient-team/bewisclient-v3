package net.bewis09.bewisclient.screen

import net.bewis09.bewisclient.core.IndependentScreen
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.interfaces.BackgroundEffectProvider
import net.bewis09.bewisclient.util.toText
import net.minecraft.client.gui.DrawContext

class RenderableScreen(val renderable: Renderable) : IndependentScreen("".toText()) {
    var deltaTicks: Float = 0f
        private set
    var startX = 0.0
    var startY = 0.0

    fun getSelectedRenderable(): Renderable? {
        var current: Renderable? = renderable
        while (current != null) {
            val selected = current.selectedElement ?: break
            current = selected
        }

        return current
    }

    override fun init() {
        renderable(0, 0, width, height)
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        renderIndependentBackground(context, mouseX, mouseY, deltaTicks)
        this.deltaTicks = deltaTicks
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

    override fun onMouseClick(x: Double, y: Double, button: Int): Boolean {
        startX = x
        startY = y
        return renderable.mouseClick(x, y, button)
    }

    override fun onMouseRelease(x: Double, y: Double, button: Int): Boolean = renderable.mouseRelease(x, y, button)

    override fun onMouseDrag(x: Double, y: Double, deltaX: Double, deltaY: Double, button: Int): Boolean = renderable.mouseDrag(x, y, startX, startY, button)

    override fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean = renderable.mouseScroll(mouseX, mouseY, horizontalAmount, verticalAmount)

    override fun onKeyPress(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = renderable.keyPress(keyCode, scanCode, modifiers)

    override fun onKeyRelease(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = renderable.keyRelease(keyCode, scanCode, modifiers)

    override fun onCharTyped(chr: Char, modifiers: Int): Boolean = renderable.charTyped(chr, modifiers)
}

