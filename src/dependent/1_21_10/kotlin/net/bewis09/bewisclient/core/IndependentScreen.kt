package net.bewis09.bewisclient.core

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.CharacterEvent
import net.minecraft.client.input.KeyEvent
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component

open class IndependentScreen(title: Component): Screen(title) {
    override fun mouseClicked(click: MouseButtonEvent, doubled: Boolean): Boolean = onMouseClick(click.x, click.y, click.button())
    override fun mouseReleased(click: MouseButtonEvent): Boolean = onMouseRelease(click.x, click.y, click.button())
    override fun mouseDragged(click: MouseButtonEvent, offsetX: Double, offsetY: Double): Boolean = onMouseDrag(click.x, click.y, offsetX, offsetY, click.button())
    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean = onMouseScroll(mouseX, mouseY, horizontalAmount, verticalAmount)
    override fun keyPressed(input: KeyEvent): Boolean = onKeyPress(input.key, input.scancode, input.modifiers)
    override fun keyReleased(input: KeyEvent): Boolean = onKeyRelease(input.key, input.scancode, input.modifiers)
    override fun charTyped(input: CharacterEvent): Boolean = onCharTyped(input.codepoint.toChar(), input.modifiers)

    open fun onMouseClick(x: Double, y: Double, button: Int): Boolean = false
    open fun onMouseRelease(x: Double, y: Double, button: Int): Boolean = false
    open fun onMouseDrag(x: Double, y: Double, deltaX: Double, deltaY: Double, button: Int): Boolean = false
    open fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean = false
    open fun onKeyPress(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = false
    open fun onKeyRelease(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = false
    open fun onCharTyped(chr: Char, modifiers: Int): Boolean = false

    @Suppress("unused")
    fun renderIndependentBackground(context: GuiGraphics, mouseX: Int, mouseY: Int, deltaTicks: Float) {}
}