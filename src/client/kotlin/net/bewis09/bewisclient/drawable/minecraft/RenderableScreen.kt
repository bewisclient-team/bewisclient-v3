// @VersionReplacement

package net.bewis09.bewisclient.drawable.minecraft

import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.BackgroundEffectProvider
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.notification.NotificationManager
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.version.GuiGraphics
import net.minecraft.client.gui.screens.Screen

class RenderableScreen(val renderable: Renderable) : Screen("".toText()) {
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

    // @[1.21.11] render @[] extractRenderState
    override fun /*[@]*/extractRenderState/*[!@]*/(graphics: GuiGraphics, mouseX: Int, mouseY: Int, deltaTicks: Float) = this.render(graphics, mouseX, mouseY)

    fun render(context: GuiGraphics, mouseX: Int, mouseY: Int) {
        renderIndependentBackground(context, mouseX, mouseY, deltaTicks)
        val screenDrawing = ScreenDrawing(context, font)
        renderable.render(screenDrawing, mouseX, mouseY)
        screenDrawing.runAfterDraw()
        NotificationManager.renderNotifications(screenDrawing, mouseX, mouseY)
    }

    fun renderBackground(guiGraphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int) {
        if (renderable is BackgroundEffectProvider) {
            guiGraphics.fill(x, y, x + width, y + height, 0x000000 or (renderable.getBackgroundEffectFactor() * 64).toInt() shl 24)
        } else {
            // @[1.21.11] renderMenuBackground @[] extractMenuBackground
            super./*[@]*/extractMenuBackground/*[!@]*/(guiGraphics, x, y, width, height)
        }
    }

    fun onMouseClick(x: Double, y: Double, button: Int): Boolean {
        startX = x
        startY = y
        return renderable.mouseClick(x, y, button)
    }

    // @[1.21.8] (mouseX: Double, mouseY: Double, button: Int): Boolean = onMouseClick(mouseX, mouseY, button) @[] (click: net.minecraft.client.input.MouseButtonEvent, doubled: Boolean): Boolean = onMouseClick(click.x, click.y, click.button())
    override fun mouseClicked/*[@]*/(click: net.minecraft.client.input.MouseButtonEvent, doubled: Boolean): Boolean = onMouseClick(click.x, click.y, click.button())/*[!@]*/
    // @[1.21.8] (mouseX: Double, mouseY: Double, button: Int): Boolean = onMouseRelease(mouseX, mouseY, button) @[] (click: net.minecraft.client.input.MouseButtonEvent): Boolean = onMouseRelease(click.x, click.y, click.button())
    override fun mouseReleased/*[@]*/(click: net.minecraft.client.input.MouseButtonEvent): Boolean = onMouseRelease(click.x, click.y, click.button())/*[!@]*/
    // @[1.21.8] (mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean = onMouseDrag(mouseX, mouseY, button) @[] (click: net.minecraft.client.input.MouseButtonEvent, offsetX: Double, offsetY: Double): Boolean = onMouseDrag(click.x, click.y, click.button())
    override fun mouseDragged/*[@]*/(click: net.minecraft.client.input.MouseButtonEvent, offsetX: Double, offsetY: Double): Boolean = onMouseDrag(click.x, click.y, click.button())/*[!@]*/
    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean = onMouseScroll(mouseX, mouseY, horizontalAmount, verticalAmount)
    // @[1.21.8] (keyCode: Int, scanCode: Int, modifiers: Int): Boolean = onKeyPress(keyCode, scanCode, modifiers) @[26.2] (input: net.minecraft.client.input.KeyEvent): Boolean = onKeyPress(input.key, input.scancode, input.modifiers) @[] (input: net.minecraft.client.input.KeyEvent): Boolean = onKeyPress(input.key, input.keycode, input.modifiers)
    override fun keyPressed/*[@]*/(input: net.minecraft.client.input.KeyEvent): Boolean = onKeyPress(input.key, input.keycode, input.modifiers)/*[!@]*/
    // @[1.21.8] (keyCode: Int, scanCode: Int, modifiers: Int): Boolean = onKeyRelease(keyCode, scanCode, modifiers) @[26.2] (input: net.minecraft.client.input.KeyEvent): Boolean = onKeyRelease(input.key, input.scancode, input.modifiers) @[] (input: net.minecraft.client.input.KeyEvent): Boolean = onKeyRelease(input.key, input.keycode, input.modifiers)
    override fun keyReleased/*[@]*/(input: net.minecraft.client.input.KeyEvent): Boolean = onKeyRelease(input.key, input.keycode, input.modifiers)/*[!@]*/
    // @[1.21.8] (chr: Char, modifiers: Int): Boolean = onCharTyped(chr, modifiers) @[] (input: net.minecraft.client.input.CharacterEvent): Boolean = onCharTyped(input.codepoint.toChar(), 0)
    override fun charTyped/*[@]*/(input: net.minecraft.client.input.CharacterEvent): Boolean = onCharTyped(input.codepoint.toChar(), 0)/*[!@]*/

    fun onMouseRelease(x: Double, y: Double, button: Int) = renderable.mouseRelease(x, y, button).let { true }
    fun onMouseDrag(x: Double, y: Double, button: Int): Boolean = renderable.mouseDrag(x, y, startX, startY, button)
    fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean = renderable.mouseScroll(mouseX, mouseY, horizontalAmount, verticalAmount)
    fun onKeyPress(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = renderable.keyPress(keyCode, scanCode, modifiers)
    fun onKeyRelease(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = renderable.keyRelease(keyCode, scanCode, modifiers)
    fun onCharTyped(chr: Char, modifiers: Int): Boolean = renderable.charTyped(chr, modifiers)

    @Suppress("unused")
    // @[1.21.5] = super.renderBackground(context, mouseX, mouseY, deltaTicks) @[] {}
    fun renderIndependentBackground(context: GuiGraphics, mouseX: Int, mouseY: Int, deltaTicks: Float) /*[@]*/{}/*[!@]*/
}