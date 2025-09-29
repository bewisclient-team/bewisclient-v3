package net.bewis09.bewisclient.core

import net.minecraft.client.gui.Click
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.input.CharInput
import net.minecraft.client.input.KeyInput
import net.minecraft.text.Text

open class IndependentScreen(title: Text): Screen(title) {
    override fun mouseClicked(click: Click, doubled: Boolean): Boolean = onMouseClick(click.x, click.y, click.keycode)

    override fun mouseReleased(click: Click): Boolean = onMouseRelease(click.x, click.y, click.keycode)

    override fun mouseDragged(click: Click, offsetX: Double, offsetY: Double): Boolean = onMouseDrag(click.x, click.y, offsetX, offsetY, click.keycode)

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean = onMouseScroll(mouseX, mouseY, horizontalAmount, verticalAmount)

    override fun keyPressed(input: KeyInput): Boolean = onKeyPress(input.key, input.scancode, input.modifiers)

    override fun keyReleased(input: KeyInput): Boolean = onKeyRelease(input.key, input.scancode, input.modifiers)

    override fun charTyped(input: CharInput): Boolean = onCharTyped(input.codepoint.toChar(), input.modifiers)

    open fun onMouseClick(x: Double, y: Double, button: Int): Boolean = false
    open fun onMouseRelease(x: Double, y: Double, button: Int): Boolean = false
    open fun onMouseDrag(x: Double, y: Double, deltaX: Double, deltaY: Double, button: Int): Boolean = false
    open fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean = false
    open fun onKeyPress(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = false
    open fun onKeyRelease(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = false
    open fun onCharTyped(chr: Char, modifiers: Int): Boolean = false
}