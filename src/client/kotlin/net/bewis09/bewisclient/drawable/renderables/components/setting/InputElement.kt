package net.bewis09.bewisclient.drawable.renderables.components.setting

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.Bewisclient
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.style.RenderiteChild
import net.minecraft.client.Minecraft
import org.lwjgl.glfw.GLFW

class InputElement(p: Props<InputElement>) : PropedRenderable<InputElement>(p) {
    var maxTextLength: Int = -1
    var text: String = ""
    var color: Color = Color.WHITE
    var font: Identifier? = null
    var onChange: ((String) -> Unit)? = null

    init { props() }

    var cursor: Int = text.length
    var scrollX = 0

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val textWidthToCurser = screenDrawing.getTextWidth(text.substring(0, cursor).toText()) {
            font = this@InputElement.font
        }.toInt()

        if (textWidthToCurser - scrollX > width - 5) {
            scrollX = textWidthToCurser - (width - 5)
        } else if (textWidthToCurser - scrollX < 0) {
            scrollX = textWidthToCurser - 5
            if (scrollX < 0) scrollX = 0
        }
        if (cursor == text.length) scrollX = scrollX.coerceAtMost(screenDrawing.getTextWidth(text.toText()) { font = this@InputElement.font }.toInt() - width + 5).coerceAtLeast(0)
        val shouldShow = System.currentTimeMillis() % 1000 < 500 && Bewisclient.getCurrentRenderableScreen()?.getSelectedRenderable() == this
        screenDrawing.drawText((text + if (cursor == text.length && shouldShow) "_" else "").toText(), x - scrollX, y + 1) {
            color = this@InputElement.color
            font = this@InputElement.font
        }
        if (cursor != text.length && shouldShow)
            screenDrawing.drawVerticalLine(textWidthToCurser + x - scrollX, y - 1, 12, color)
    }

    fun setText(text: String): InputElement {
        if (this.text == text) return this

        cursor = text.length
        this.text = text
        return this
    }

    fun insertChar(char: Char): InputElement {
        text = text.substring(0, cursor) + char + text.substring(cursor)
        cursor++
        return this
    }

    override fun onCharTyped(character: Char, modifiers: Int): Boolean {
        if (maxTextLength == 0 || Minecraft.getInstance().font.width(text + character) < maxTextLength) {
            insertChar(character)
            onChange?.invoke(text)
        }
        return true
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean = true

    override fun onKeyPress(key: Int, scanCode: Int, modifiers: Int): Boolean {
        when (key) {
            GLFW.GLFW_KEY_BACKSPACE -> {
                if (text.isNotEmpty()) {
                    text = (text.substring(0, (cursor - 1).coerceAtLeast(0)) + text.substring(cursor))
                    if (cursor > 0) cursor--
                    onChange?.invoke(text)
                }
            }
            GLFW.GLFW_KEY_DELETE -> {
                if (text.isNotEmpty()) {
                    text = (text.substring(0, cursor) + text.substring((cursor + 1).coerceAtMost(text.length)))
                    onChange?.invoke(text)
                }
            }
            GLFW.GLFW_KEY_LEFT -> { if (cursor > 0) cursor-- }
            GLFW.GLFW_KEY_RIGHT -> { if (cursor < text.length) cursor++ }
            else -> return false
        }

        return true
    }
}

@RenderiteChild
fun Renderable.Input(p: RenderiteElement.Props<InputElement>) { InputElement(p).also(::addRenderable)}