package net.bewis09.renderite.drawer

import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.RenderiteElement

interface TextDrawing<I, T, F> : RectDrawing<I, T, F> {
    fun drawText(text: String, x: Number, y: Number, color: Color, font: F? = this.overwrittenFont) {
        drawText(text.convert(), x, y, color, font)
    }

    fun drawText(text: T, x: Number, y: Number, color: Color, font: F? = this.overwrittenFont, shadow: Boolean = false) {
        translate(x.toFloat(), y.toFloat()) {
            drawTextIntern(text, color, font, shadow)
        }
    }

    fun String.convert(): T

    fun T.convert(): String
    
    fun drawTextIntern(text: T, color: Color, font: F?, shadow: Boolean = false)

    fun drawTextWithShadow(text: String, x: Number, y: Number, color: Color, font: F? = this.overwrittenFont) {
        drawTextWithShadow(text.convert(), x, y, color, font)
    }

    fun drawTextWithShadow(text: T, x: Number, y: Number, color: Color, font: F? = this.overwrittenFont) {
        drawText(text, x, y, color, font, shadow = true)
    }

    fun drawText(text: String, x: Number, y: Number, color: Color, shadow: Boolean, font: F? = this.overwrittenFont) {
        if (shadow) {
            drawTextWithShadow(text, x, y, color, font)
        } else {
            drawText(text, x, y, color, font)
        }
    }

    fun drawText(text: T, x: Number, y: Number, color: Color, shadow: Boolean, font: F? = this.overwrittenFont) {
        if (shadow) {
            drawTextWithShadow(text, x, y, color, font)
        } else {
            drawText(text, x, y, color, font)
        }
    }

    fun drawCenteredText(text: String, centerX: Number, y: Number, color: Color, shadow: Boolean, font: F? = this.overwrittenFont) {
        if (shadow) {
            drawCenteredTextWithShadow(text, centerX, y, color, font)
        } else {
            drawCenteredText(text, centerX, y, color, font)
        }
    }

    fun drawCenteredText(text: T, centerX: Number, y: Number, color: Color, shadow: Boolean, font: F? = this.overwrittenFont) {
        if (shadow) {
            drawCenteredTextWithShadow(text, centerX, y, color, font)
        } else {
            drawCenteredText(text, centerX, y, color, font)
        }
    }

    fun drawCenteredText(text: String, centerX: Number, y: Number, color: Color, font: F? = this.overwrittenFont) {
        drawCenteredText(text.convert(), centerX, y, color, font)
    }

    fun drawCenteredText(text: T, centerX: Number, y: Number, color: Color, font: F? = this.overwrittenFont, shadow: Boolean = false) {
        val textWidth = getTextWidth(text, font)
        translate(-textWidth / 2f, 0f) {
            drawText(text, centerX, y, color, font, shadow)
        }
    }

    fun drawCenteredTextWithShadow(text: T, centerX: Number, y: Number, color: Color, font: F? = this.overwrittenFont) {
        drawCenteredText(text, centerX, y, color, font, true)
    }

    fun drawCenteredTextWithShadow(text: String, centerX: Number, y: Number, color: Color, font: F? = this.overwrittenFont) {
        drawCenteredTextWithShadow(text.convert(), centerX, y, color, font)
    }

    fun drawRightAlignedText(text: String, rightX: Number, y: Number, color: Color, font: F? = this.overwrittenFont) {
        val textWidth = getTextWidth(text, font)
        drawText(text, rightX.toFloat() - textWidth, y, color, font)
    }

    fun drawRightAlignedTextWithShadow(text: String, rightX: Number, y: Number, color: Color, font: F? = this.overwrittenFont) {
        val textWidth = getTextWidth(text)
        drawTextWithShadow(text, rightX.toFloat() - textWidth, y, color, font)
    }

    fun drawRightAlignedText(text: T, rightX: Number, y: Number, color: Color, font: F? = this.overwrittenFont) {
        val textWidth = getTextWidth(text, font)
        drawText(text, rightX.toFloat() - textWidth, y, color, font)
    }

    fun drawRightAlignedTextWithShadow(text: T, rightX: Number, y: Number, color: Color, font: F? = this.overwrittenFont) {
        val textWidth = getTextWidth(text, font)
        drawTextWithShadow(text, rightX.toFloat() - textWidth, y, color, font)
    }

    fun drawWrappedText(text: String, x: Number, y: Number, maxWidth: Int, color: Color, font: F? = this.overwrittenFont, lineHeight: Int = getTextHeight()): List<String> {
        return wrapText(text, maxWidth, font).also { drawWrappedText(it.map { a -> a.convert() }, x, y, color, font, lineHeight) }
    }

    fun drawCenteredWrappedText(lines: List<T>, centerX: Number, y: Number, color: Color, font: F? = this.overwrittenFont, lineHeight: Int = getTextHeight()) {
        for (i in lines.indices) {
            drawCenteredText(lines[i], centerX, y.toFloat() + i * lineHeight, color, font)
        }
    }

    fun drawWrappedText(lines: List<T>, x: Number, y: Number, color: Color, font: F? = this.overwrittenFont, lineHeight: Int = getTextHeight()) {
        for (i in lines.indices) {
            drawText(lines[i], x, y.toFloat() + i * lineHeight, color, font)
        }
    }

    fun drawWrappedText(text: T, x: Number, y: Number, maxWidth: Int, color: Color, font: F? = this.overwrittenFont, lineHeight: Int = getTextHeight()): List<String> {
        return drawWrappedText(text.convert(), x, y, maxWidth, color, font, lineHeight)
    }

    fun drawCenteredWrappedText(lines: List<String>, centerX: Number, y: Number, color: Color, font: F? = this.overwrittenFont, shadow: Boolean = false, lineHeight: Int = getTextHeight()) {
        for (i in lines.indices) {
            if (shadow) {
                drawCenteredTextWithShadow(
                    lines[i], centerX, y.toFloat() + i * lineHeight, color, font
                )
                continue
            }
            drawCenteredText(
                lines[i], centerX, y.toFloat() + i * lineHeight, color, font
            )
        }
    }

    fun drawCenteredWrappedText(text: String, centerX: Int, y: Int, maxWidth: Int, color: Color, font: F? = this.overwrittenFont, shadow: Boolean = false, lineHeight: Int = getTextHeight()): List<String> {
        return wrapText(text, maxWidth, font).also { drawCenteredWrappedText(it, centerX, y, color, font, shadow, lineHeight) }
    }

    fun drawCenteredWrappedText(text: T, centerX: Int, y: Int, maxWidth: Int, color: Color, font: F? = this.overwrittenFont, shadow: Boolean = false, lineHeight: Int = getTextHeight()): List<String> {
        return drawCenteredWrappedText(text.convert(), centerX, y, maxWidth, color, font, shadow, lineHeight)
    }

    fun drawRightAlignedWrappedText(lines: List<T>, x: Number, y: Number, color: Color, font: F? = this.overwrittenFont, lineHeight: Int = getTextHeight()) {
        val lineHeight = getTextHeight()
        for (i in lines.indices) {
            drawRightAlignedText(lines[i], x, y.toFloat() + i * lineHeight, color, font)
        }
    }

    fun wrapText(text: T, maxWidth: Int, font: F? = this.overwrittenFont): List<T> { TODO() }

    /**
     * Wraps text to fit within the specified width.
     *
     * @param maxWidth The maximum width for each line.
     * @return A list of strings, each representing a line of wrapped text.
     */
    fun wrapText(text: String, maxWidth: Int, font: F? = this.overwrittenFont): List<String> {
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
    fun getTextWidth(text: String, font: F? = this.overwrittenFont): Int {
        return getTextWidth(text.convert(), font)
    }

    /**
     * Gets the width of the specified text when rendered.
     *
     * @return The width of the text in pixels.
     */
    fun getTextWidth(text: T, font: F? = this.overwrittenFont): Int

    /**
     * Gets the height of a line of text.
     *
     * @return The height of a line of text in pixels.
     */
    fun getTextHeight(): Int

    fun getTextYCenter(renderable: RenderiteElement<*, *, *, *, *>): Float = renderable.exactCenterY - getTextHeight() / 2f
}