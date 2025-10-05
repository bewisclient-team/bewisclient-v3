package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.core.setFont
import net.bewis09.bewisclient.logic.color.Color
import net.bewis09.bewisclient.logic.color.color
import net.bewis09.bewisclient.logic.toText
import net.minecraft.text.Text
import net.minecraft.util.Identifier

interface TextDrawing : RectDrawing {
    fun drawText(text: String, x: Int, y: Int, color: Color, font: Identifier? = this.overwrittenFont) {
        drawText(text.toText(), x, y, color, font)
    }

    fun drawText(text: Text, x: Int, y: Int, color: Color, font: Identifier? = this.overwrittenFont) {
        val color = applyAlpha(color)
        if (color.toLong().color.alpha < 4) return
        drawContext.drawText(textRenderer, text.copy().setFont(font), x, y, color, false)
    }

    fun drawTextWithShadow(text: String, x: Int, y: Int, color: Color, font: Identifier? = this.overwrittenFont) {
        drawTextWithShadow(text.toText(), x, y, color, font)
    }

    fun drawTextWithShadow(text: Text, x: Int, y: Int, color: Color, font: Identifier? = this.overwrittenFont) {
        val color = applyAlpha(color)
        if (color.toLong().color.alpha < 4) return
        drawContext.drawText(textRenderer, text.copy().setFont(font), x, y, color, true)
    }

    fun drawText(text: String, x: Int, y: Int, color: Color, shadow: Boolean, font: Identifier? = this.overwrittenFont) {
        if (shadow) {
            drawTextWithShadow(text, x, y, color, font)
        } else {
            drawText(text, x, y, color, font)
        }
    }

    fun drawText(text: Text, x: Int, y: Int, color: Color, shadow: Boolean, font: Identifier? = this.overwrittenFont) {
        if (shadow) {
            drawTextWithShadow(text, x, y, color, font)
        } else {
            drawText(text, x, y, color, font)
        }
    }

    fun drawCenteredText(text: String, centerX: Int, y: Int, color: Color, shadow: Boolean, font: Identifier? = this.overwrittenFont) {
        if (shadow) {
            drawCenteredTextWithShadow(text, centerX, y, color, font)
        } else {
            drawCenteredText(text, centerX, y, color, font)
        }
    }

    fun drawCenteredText(text: Text, centerX: Int, y: Int, color: Color, shadow: Boolean, font: Identifier? = this.overwrittenFont) {
        if (shadow) {
            drawCenteredTextWithShadow(text, centerX, y, color, font)
        } else {
            drawCenteredText(text, centerX, y, color, font)
        }
    }

    fun drawCenteredText(text: String, centerX: Int, y: Int, color: Color, font: Identifier? = this.overwrittenFont) {
        drawCenteredText(text.toText(), centerX, y, color, font)
    }

    fun drawCenteredText(text: Text, centerX: Int, y: Int, color: Color, font: Identifier? = this.overwrittenFont) {
        val textWidth = getTextWidth(text, font)
        translate(-textWidth / 2f, 0f) {
            drawText(text.copy().setFont(font), centerX, y, color, font)
        }
    }

    fun drawCenteredTextWithShadow(text: Text, centerX: Int, y: Int, color: Color, font: Identifier? = this.overwrittenFont) {
        val textWidth = getTextWidth(text.copy().setFont(font), font)
        drawTextWithShadow(text.copy().setFont(font), centerX - textWidth / 2, y, color, font)
    }

    fun drawCenteredTextWithShadow(text: String, centerX: Int, y: Int, color: Color, font: Identifier? = this.overwrittenFont) {
        drawCenteredTextWithShadow(text.toText(), centerX, y, color, font)
    }

    fun drawRightAlignedText(text: String, rightX: Int, y: Int, color: Color, font: Identifier? = this.overwrittenFont) {
        val textWidth = getTextWidth(text, font)
        drawText(text, rightX - textWidth, y, color, font)
    }

    fun drawRightAlignedTextWithShadow(text: String, rightX: Int, y: Int, color: Color, font: Identifier? = this.overwrittenFont) {
        val textWidth = getTextWidth(text)
        drawTextWithShadow(text, rightX - textWidth, y, color, font)
    }

    fun drawWrappedText(text: String, x: Int, y: Int, maxWidth: Int, color: Color, font: Identifier? = this.overwrittenFont): List<String> {
        return wrapText(text, maxWidth, font).also { drawWrappedText(it, x, y, color, font) }
    }

    fun drawWrappedText(lines: List<String>, x: Int, y: Int, color: Color, font: Identifier? = this.overwrittenFont) {
        val lineHeight = getTextHeight()
        for (i in lines.indices) {
            drawText(literalWithStyle(lines[i]), x, y + i * lineHeight, color, font)
        }
    }

    fun drawWrappedText(text: Text, x: Int, y: Int, maxWidth: Int, color: Color, font: Identifier? = this.overwrittenFont): List<String> {
        return drawWrappedText(text.string, x, y, maxWidth, color, font)
    }

    fun drawCenteredWrappedText(text: String, centerX: Int, y: Int, maxWidth: Int, color: Color, font: Identifier? = this.overwrittenFont): List<String> {
        return wrapText(text, maxWidth, font).let { lines ->
            val lineHeight = getTextHeight()
            for (i in lines.indices) {
                drawCenteredText(
                    lines[i], centerX, y + i * lineHeight, color, font
                )
            }
            lines
        }
    }

    fun drawCenteredWrappedText(text: Text, centerX: Int, y: Int, maxWidth: Int, color: Color, font: Identifier? = this.overwrittenFont): List<String> {
        return drawCenteredWrappedText(text.string, centerX, y, maxWidth, color, font)
    }

    /**
     * Wraps text to fit within the specified width.
     *
     * @param maxWidth The maximum width for each line.
     * @return A list of strings, each representing a line of wrapped text.
     */
    fun wrapText(text: String, maxWidth: Int, font: Identifier? = this.overwrittenFont): List<String> {
        val lines = mutableListOf<String>()

        val paragraphs = text.split("\n")

        for (paragraph in paragraphs) {
            val words = paragraph.split(" ")
            var currentLine = ""

            for (word in words) {
                val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"

                if (getTextWidth(testLine, font) <= maxWidth) {
                    currentLine = testLine
                } else {
                    if (currentLine.isNotEmpty()) {
                        lines.add(currentLine)
                        currentLine = word
                    }

                    while (getTextWidth(currentLine, font) > maxWidth && currentLine.isNotEmpty()) {
                        var cutIndex = currentLine.length - 1

                        while (cutIndex > 0 && getTextWidth(currentLine.take(cutIndex), font) > maxWidth) {
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
    fun getTextWidth(text: String, font: Identifier? = this.overwrittenFont): Int {
        return getTextWidth(text.toText(), font)
    }

    /**
     * Gets the width of the specified text when rendered.
     *
     * @return The width of the text in pixels.
     */
    fun getTextWidth(text: Text, font: Identifier? = this.overwrittenFont): Int {
        return textRenderer.getWidth(text.setFont(font))
    }

    /**
     * Gets the height of a line of text.
     *
     * @return The height of a line of text in pixels.
     */
    fun getTextHeight(): Int {
        return textRenderer.fontHeight
    }

    fun literalWithStyle(text: String, font: Identifier? = this.overwrittenFont): Text = text.toText().setFont(font)
}