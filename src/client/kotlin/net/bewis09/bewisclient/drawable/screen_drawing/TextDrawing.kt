package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.logic.color.Color
import net.bewis09.bewisclient.logic.toText
import net.minecraft.text.*

interface TextDrawing : RectDrawing {
    fun drawText(text: String, x: Int, y: Int, color: Color, style: Style = this.style) {
        drawText(Text.literal(text), x, y, color, style)
    }

    fun drawText(text: Text, x: Int, y: Int, color: Color, style: Style = this.style) {
        core.drawText(text.copy().fillStyle(style), x, y, applyAlpha(color), false)
    }

    fun drawTextWithShadow(text: String, x: Int, y: Int, color: Color, style: Style = this.style) {
        drawTextWithShadow(Text.literal(text), x, y, color, style)
    }

    fun drawTextWithShadow(text: Text, x: Int, y: Int, color: Color, style: Style = this.style) {
        core.drawText(text.copy().fillStyle(style), x, y, applyAlpha(color), true)
    }

    fun drawText(text: String, x: Int, y: Int, color: Color, shadow: Boolean, style: Style = this.style) {
        if (shadow) {
            drawTextWithShadow(text, x, y, color, style)
        } else {
            drawText(text, x, y, color, style)
        }
    }

    fun drawText(text: Text, x: Int, y: Int, color: Color, shadow: Boolean, style: Style = this.style) {
        if (shadow) {
            drawTextWithShadow(text, x, y, color, style)
        } else {
            drawText(text, x, y, color, style)
        }
    }

    fun drawCenteredText(text: String, centerX: Int, y: Int, color: Color, shadow: Boolean, style: Style = this.style) {
        if (shadow) {
            drawCenteredTextWithShadow(text, centerX, y, color, style)
        } else {
            drawCenteredText(text, centerX, y, color, style)
        }
    }

    fun drawCenteredText(text: Text, centerX: Int, y: Int, color: Color, shadow: Boolean, style: Style = this.style) {
        if (shadow) {
            drawCenteredTextWithShadow(text, centerX, y, color, style)
        } else {
            drawCenteredText(text, centerX, y, color, style)
        }
    }

    fun drawCenteredText(text: String, centerX: Int, y: Int, color: Color, style: Style = this.style) {
        drawCenteredText(Text.literal(text), centerX, y, color, style)
    }

    fun drawCenteredText(text: Text, centerX: Int, y: Int, color: Color, style: Style = this.style) {
        val textWidth = getTextWidth(text, style)
        translate(-textWidth / 2f, 0f) {
            drawText(text.copy().fillStyle(style), centerX, y, color, style)
        }
    }

    fun drawCenteredTextWithShadow(text: Text, centerX: Int, y: Int, color: Color, style: Style = this.style) {
        val textWidth = getTextWidth(text, style)
        drawTextWithShadow(text.copy().fillStyle(style), centerX - textWidth / 2, y, color, style)
    }

    fun drawCenteredTextWithShadow(text: String, centerX: Int, y: Int, color: Color, style: Style = this.style) {
        drawCenteredTextWithShadow(Text.literal(text), centerX, y, color, style)
    }

    fun drawRightAlignedText(text: String, rightX: Int, y: Int, color: Color, style: Style = this.style) {
        val textWidth = getTextWidth(text, style)
        drawText(text, rightX - textWidth, y, color, style)
    }

    fun drawRightAlignedTextWithShadow(text: String, rightX: Int, y: Int, color: Color, style: Style = this.style) {
        val textWidth = getTextWidth(text)
        drawTextWithShadow(text, rightX - textWidth, y, color, style)
    }

    fun drawWrappedText(text: String, x: Int, y: Int, maxWidth: Int, color: Color, style: Style = this.style): List<String> {
        return wrapText(text, maxWidth, style).also { drawWrappedText(it, x, y, color, style) }
    }

    fun drawWrappedText(lines: List<String>, x: Int, y: Int, color: Color, style: Style = this.style) {
        val lineHeight = getTextHeight()
        for (i in lines.indices) {
            drawText(literalWithStyle(lines[i]), x, y + i * lineHeight, color, style)
        }
    }

    fun drawWrappedText(text: Text, x: Int, y: Int, maxWidth: Int, color: Color, style: Style = this.style): List<String> {
        return drawWrappedText(text.string, x, y, maxWidth, color, style)
    }

    fun drawCenteredWrappedText(text: String, centerX: Int, y: Int, maxWidth: Int, color: Color, style: Style = this.style): List<String> {
        return wrapText(text, maxWidth, style).let { lines ->
            val lineHeight = getTextHeight()
            for (i in lines.indices) {
                drawCenteredText(
                    lines[i], centerX, y + i * lineHeight, color, style
                )
            }
            lines
        }
    }

    fun drawCenteredWrappedText(text: Text, centerX: Int, y: Int, maxWidth: Int, color: Color, style: Style = this.style): List<String> {
        return drawCenteredWrappedText(text.string, centerX, y, maxWidth, color, style)
    }

    /**
     * Wraps text to fit within the specified width.
     *
     * @param maxWidth The maximum width for each line.
     * @return A list of strings, each representing a line of wrapped text.
     */
    fun wrapText(text: String, maxWidth: Int, style: Style = this.style): List<String> {
        val lines = mutableListOf<String>()

        val paragraphs = text.split("\n")

        for (paragraph in paragraphs) {
            val words = paragraph.split(" ")
            var currentLine = ""

            for (word in words) {
                val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"

                if (getTextWidth(testLine, style) <= maxWidth) {
                    currentLine = testLine
                } else {
                    if (currentLine.isNotEmpty()) {
                        lines.add(currentLine)
                        currentLine = word
                    }

                    while (getTextWidth(currentLine, style) > maxWidth && currentLine.isNotEmpty()) {
                        var cutIndex = currentLine.length - 1

                        while (cutIndex > 0 && getTextWidth(currentLine.take(cutIndex), style) > maxWidth) {
                            cutIndex--
                        }

                        if (cutIndex == 0) cutIndex = 1

                        lines.add(currentLine.take(cutIndex))
                        currentLine = currentLine.substring(cutIndex)
                    }
                }
            }

            lines.add(currentLine)
        }

        return lines
    }

    /**
     * Gets the width of the specified text when rendered.
     *
     * @return The width of the text in pixels.
     */
    fun getTextWidth(text: String, style: Style = this.style): Int {
        return getTextWidth(text.toText(), style)
    }

    /**
     * Gets the width of the specified text when rendered.
     *
     * @return The width of the text in pixels.
     */
    fun getTextWidth(text: Text, style: Style = this.style): Int {
        return core.getTextWidth(text.copy().setStyle(style))
    }

    /**
     * Gets the height of a line of text.
     *
     * @return The height of a line of text in pixels.
     */
    fun getTextHeight(): Int {
        return core.getFontHeight()
    }

    fun literalWithStyle(text: String, style: Style = this.style): MutableText = Text.literal(text).fillStyle(style)
}