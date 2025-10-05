package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.toText
import org.lwjgl.glfw.GLFW

class Input : Renderable {
    var maxWidth: Int
    var text: String
        private set
    var color: Color
    var cursor: Int

    var scrollX = 0

    val onChange: ((String) -> Unit)?

    constructor(maxWidth: Int = 0, text: String = "", color: Color = Color.WHITE, onChange: ((String) -> Unit)? = null) : super() {
        this.maxWidth = maxWidth
        this.text = text
        this.color = color
        this.cursor = text.length
        this.onChange = onChange
    }

    init {
        internalHeight = 10
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        if (screenDrawing.getTextWidth(text.substring(0, cursor), null) - scrollX > width - 5) {
            scrollX = screenDrawing.getTextWidth(text.substring(0, cursor), null) - (width - 5)
        } else if (screenDrawing.getTextWidth(text.substring(0, cursor), null) - scrollX < 0) {
            scrollX = screenDrawing.getTextWidth(text.substring(0, cursor), null) - 5
            if (scrollX < 0) scrollX = 0
        }
        if (cursor == text.length) scrollX = scrollX.coerceAtMost(screenDrawing.getTextWidth(text, null) - width + 5).coerceAtLeast(0)
        screenDrawing.enableScissors(x, y, width, height)
        val shouldShow = System.currentTimeMillis() % 1000 < 500 && util.getCurrentRenderableScreen()?.getSelectedRenderable() == this
        screenDrawing.drawTextWithShadow((text + if (cursor == text.length && shouldShow) "_" else "").toText(), x - scrollX, y + 1, color, null)
        if (cursor != text.length && shouldShow)
            screenDrawing.drawVerticalLine(screenDrawing.getTextWidth(text.substring(0, cursor), null) + x - scrollX, y - 1, 12, color)
        screenDrawing.disableScissors()
    }

    fun setText(text: String): Input {
        if (this.text == text) return this

        cursor = text.length
        this.text = text
        return this
    }

    fun insertChar(char: Char): Input {
        text = text.substring(0, cursor) + char + text.substring(cursor)
        cursor++
        return this
    }

    override fun onCharTyped(character: Char, modifiers: Int): Boolean {
        if (maxWidth == 0 || client.textRenderer.getWidth(text + character) < maxWidth) {
            insertChar(character)
            onChange?.invoke(text)
        }
        return true
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return true
    }

    override fun onMouseRelease(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return true
    }

    override fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean {
        return when (key) {
            GLFW.GLFW_KEY_BACKSPACE -> {
                if (text.isNotEmpty()) {
                    text = (text.substring(0, (cursor - 1).coerceAtLeast(0)) + text.substring(cursor))
                    if (cursor > 0) cursor--
                    onChange?.invoke(text)
                }
                true
            }

            GLFW.GLFW_KEY_DELETE -> {
                if (text.isNotEmpty()) {
                    text = (text.substring(0, cursor) + text.substring((cursor + 1).coerceAtMost(text.length)))
                    onChange?.invoke(text)
                }
                true
            }

            GLFW.GLFW_KEY_LEFT -> {
                if (cursor > 0) cursor--
                true
            }

            GLFW.GLFW_KEY_RIGHT -> {
                if (cursor < text.length) cursor++
                true
            }

            else -> false
        }
    }
}