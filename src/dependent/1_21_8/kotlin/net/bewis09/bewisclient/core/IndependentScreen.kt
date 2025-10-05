package net.bewis09.bewisclient.core

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

open class IndependentScreen(title: Text): Screen(title) {
    override fun charTyped(chr: Char, modifiers: Int): Boolean = onCharTyped(chr, modifiers)
    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = onKeyRelease(keyCode, scanCode, modifiers)
    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean = onMouseScroll(mouseX, mouseY, horizontalAmount, verticalAmount)
    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean = onMouseDrag(mouseX, mouseY, deltaX, deltaY, button)
    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean = onMouseRelease(mouseX, mouseY, button)
    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean = onMouseClick(mouseX, mouseY, button)
    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = onKeyPress(keyCode, scanCode, modifiers)

    open fun onMouseClick(x: Double, y: Double, button: Int): Boolean = false
    open fun onMouseRelease(x: Double, y: Double, button: Int): Boolean = false
    open fun onMouseDrag(x: Double, y: Double, deltaX: Double, deltaY: Double, button: Int): Boolean = false
    open fun onMouseScroll(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean = false
    open fun onKeyPress(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = false
    open fun onKeyRelease(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = false
    open fun onCharTyped(chr: Char, modifiers: Int): Boolean = false

    fun renderIndependentBackground(context: DrawContext, mouseX: Int, mouseY: Int, deltaTicks: Float) {}
}