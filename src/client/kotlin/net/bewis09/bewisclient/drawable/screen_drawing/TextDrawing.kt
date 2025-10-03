package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.core.BewisclientID
import net.bewis09.bewisclient.core.toText
import net.bewis09.bewisclient.core.wrapper.TextWrapper
import net.bewis09.bewisclient.logic.color.Color

interface TextDrawing : RectDrawing {
    fun drawText(text: String, x: Int, y: Int, color: Color, font: BewisclientID? = this.font) {
        drawText(text.toText(), x, y, color, font)
    }

    fun drawText(text: TextWrapper, x: Int, y: Int, color: Color, font: BewisclientID? = this.font) {
        drawingCore.drawText(text.copy().setFont(font), x, y, applyAlpha(color), false)
    }

    fun drawTextWithShadow(text: String, x: Int, y: Int, color: Color, font: BewisclientID? = this.font) {
        drawTextWithShadow(text.toText(), x, y, color, font)
    }

    fun drawTextWithShadow(text: TextWrapper, x: Int, y: Int, color: Color, font: BewisclientID? = this.font) {
        drawingCore.drawText(text.copy().setFont(font), x, y, applyAlpha(color), true)
    }

    fun drawText(text: String, x: Int, y: Int, color: Color, shadow: Boolean, font: BewisclientID? = this.font) {
        if (shadow) {
            drawTextWithShadow(text, x, y, color, font)
        } else {
            drawText(text, x, y, color, font)
        }
    }

    fun drawText(text: TextWrapper, x: Int, y: Int, color: Color, shadow: Boolean, font: BewisclientID? = this.font) {
        if (shadow) {
            drawTextWithShadow(text, x, y, color, font)
        } else {
            drawText(text, x, y, color, font)
        }
    }

    fun drawCenteredText(text: String, centerX: Int, y: Int, color: Color, shadow: Boolean, font: BewisclientID? = this.font) {
        if (shadow) {
            drawCenteredTextWithShadow(text, centerX, y, color, font)
        } else {
            drawCenteredText(text, centerX, y, color, font)
        }
    }

    fun drawCenteredText(text: TextWrapper, centerX: Int, y: Int, color: Color, shadow: Boolean, font: BewisclientID? = this.font) {
        if (shadow) {
            drawCenteredTextWithShadow(text, centerX, y, color, font)
        } else {
            drawCenteredText(text, centerX, y, color, font)
        }
    }

    fun drawCenteredText(text: String, centerX: Int, y: Int, color: Color, font: BewisclientID? = this.font) {
        drawCenteredText(text.toText(), centerX, y, color, font)
    }

    fun drawCenteredText(text: TextWrapper, centerX: Int, y: Int, color: Color, font: BewisclientID? = this.font) {
        val textWidth = getTextWidth(text, font)
        translate(-textWidth / 2f, 0f) {
            drawText(text.copy().setFont(font), centerX, y, color, font)
        }
    }

    fun drawCenteredTextWithShadow(text: TextWrapper, centerX: Int, y: Int, color: Color, font: BewisclientID? = this.font) {
        val textWidth = getTextWidth(text.copy().setFont(font), font)
        drawTextWithShadow(text.copy().setFont(font), centerX - textWidth / 2, y, color, font)
    }

    fun drawCenteredTextWithShadow(text: String, centerX: Int, y: Int, color: Color, font: BewisclientID? = this.font) {
        drawCenteredTextWithShadow(text.toText(), centerX, y, color, font)
    }

    fun drawRightAlignedText(text: String, rightX: Int, y: Int, color: Color, font: BewisclientID? = this.font) {
        val textWidth = getTextWidth(text, font)
        drawText(text, rightX - textWidth, y, color, font)
    }

    fun drawRightAlignedTextWithShadow(text: String, rightX: Int, y: Int, color: Color, font: BewisclientID? = this.font) {
        val textWidth = getTextWidth(text)
        drawTextWithShadow(text, rightX - textWidth, y, color, font)
    }

    fun drawWrappedText(text: String, x: Int, y: Int, maxWidth: Int, color: Color, font: BewisclientID? = this.font): List<String> {
        return wrapText(text, maxWidth, font).also { drawWrappedText(it, x, y, color, font) }
    }

    fun drawWrappedText(lines: List<String>, x: Int, y: Int, color: Color, font: BewisclientID? = this.font) {
        val lineHeight = getTextHeight()
        for (i in lines.indices) {
            drawText(literalWithStyle(lines[i]), x, y + i * lineHeight, color, font)
        }
    }

    fun drawWrappedText(text: TextWrapper, x: Int, y: Int, maxWidth: Int, color: Color, font: BewisclientID? = this.font): List<String> {
        return drawWrappedText(text.string, x, y, maxWidth, color, font)
    }

    fun drawCenteredWrappedText(text: String, centerX: Int, y: Int, maxWidth: Int, color: Color, font: BewisclientID? = this.font): List<String> {
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

    fun drawCenteredWrappedText(text: TextWrapper, centerX: Int, y: Int, maxWidth: Int, color: Color, font: BewisclientID? = this.font): List<String> {
        return drawCenteredWrappedText(text.string, centerX, y, maxWidth, color, font)
    }

    /**
     * Wraps text to fit within the specified width.
     *
     * @param maxWidth The maximum width for each line.
     * @return A list of strings, each representing a line of wrapped text.
     */
    fun wrapText(text: String, maxWidth: Int, font: BewisclientID? = this.font): List<String> {
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
    fun getTextWidth(text: String, font: BewisclientID? = this.font): Int {
        return getTextWidth(text.toText(), font)
    }

    /**
     * Gets the width of the specified text when rendered.
     *
     * @return The width of the text in pixels.
     */
    fun getTextWidth(text: TextWrapper, font: BewisclientID? = this.font): Int {
        return drawingCore.getTextWidth(text.setFont(font))
    }

    /**
     * Gets the height of a line of text.
     *
     * @return The height of a line of text in pixels.
     */
    fun getTextHeight(): Int {
        return drawingCore.getFontHeight()
    }

    fun literalWithStyle(text: String, font: BewisclientID? = this.font): TextWrapper = text.toText().setFont(font)
}