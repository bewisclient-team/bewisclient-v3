package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.common.Color
import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.version.setFont
import net.bewis09.bewisclient.version.string
import net.minecraft.network.chat.Component

interface TextDrawing : RectDrawing {
    fun drawText(text: String, x: Number, y: Number, color: Color, font: Identifier? = this.overwrittenFont) {
        drawText(text.toText(), x, y, color, font)
    }

    fun drawText(text: Component, x: Number, y: Number, color: Color, font: Identifier? = this.overwrittenFont, shadow: Boolean = false) {
        if ((font == ScreenDrawingInterface.BEWISCLIENT_FONT || (font == null && this.overwrittenFont == ScreenDrawingInterface.BEWISCLIENT_FONT)) && isMinecrafty) {
            val color = applyAlpha(color)
            if (color.toLong().color.alpha < 4) return
            transform(x.toFloat(), y.toFloat() + getTextHeight() / 2f + 0.7f, 0.85f, 0.85f) {
                translate(0f, -getTextHeight() / 2f)
                guiGraphics.string(textRenderer, text.copy().setFont(ScreenDrawingInterface.DEFAULT_FONT), 0, 0, color, shadow)
            }
        } else {
            val color = applyAlpha(color)
            if (color.toLong().color.alpha < 4) return
            translate(x.toFloat(), y.toFloat()) {
                guiGraphics.string(textRenderer, text.copy().setFont(font), 0, 0, color, shadow)
            }
        }
    }

    fun drawTextWithShadow(text: String, x: Number, y: Number, color: Color, font: Identifier? = this.overwrittenFont) {
        drawTextWithShadow(text.toText(), x, y, color, font)
    }

    fun drawTextWithShadow(text: Component, x: Number, y: Number, color: Color, font: Identifier? = this.overwrittenFont) {
        drawText(text, x, y, color, font, shadow = true)
    }

    fun drawText(text: String, x: Number, y: Number, color: Color, shadow: Boolean, font: Identifier? = this.overwrittenFont) {
        if (shadow) {
            drawTextWithShadow(text, x, y, color, font)
        } else {
            drawText(text, x, y, color, font)
        }
    }

    fun drawText(text: Component, x: Number, y: Number, color: Color, shadow: Boolean, font: Identifier? = this.overwrittenFont) {
        if (shadow) {
            drawTextWithShadow(text, x, y, color, font)
        } else {
            drawText(text, x, y, color, font)
        }
    }

    fun drawCenteredText(text: String, centerX: Number, y: Number, color: Color, shadow: Boolean, font: Identifier? = this.overwrittenFont) {
        if (shadow) {
            drawCenteredTextWithShadow(text, centerX, y, color, font)
        } else {
            drawCenteredText(text, centerX, y, color, font)
        }
    }

    fun drawCenteredText(text: Component, centerX: Number, y: Number, color: Color, shadow: Boolean, font: Identifier? = this.overwrittenFont) {
        if (shadow) {
            drawCenteredTextWithShadow(text, centerX, y, color, font)
        } else {
            drawCenteredText(text, centerX, y, color, font)
        }
    }

    fun drawCenteredText(text: String, centerX: Number, y: Number, color: Color, font: Identifier? = this.overwrittenFont) {
        drawCenteredText(text.toText(), centerX, y, color, font)
    }

    fun drawCenteredText(text: Component, centerX: Number, y: Number, color: Color, font: Identifier? = this.overwrittenFont, shadow: Boolean = false) {
        val textWidth = getTextWidth(text, font)
        translate(-textWidth / 2f + if ((font == ScreenDrawingInterface.BEWISCLIENT_FONT || (font == null && this.overwrittenFont == ScreenDrawingInterface.BEWISCLIENT_FONT)) && isMinecrafty) 1f else 0f, 0f) {
            drawText(text.copy(), centerX, y, color, font, shadow)
        }
    }

    fun drawCenteredTextWithShadow(text: Component, centerX: Number, y: Number, color: Color, font: Identifier? = this.overwrittenFont) {
        drawCenteredText(text, centerX, y, color, font, true)
    }

    fun drawCenteredTextWithShadow(text: String, centerX: Number, y: Number, color: Color, font: Identifier? = this.overwrittenFont) {
        drawCenteredTextWithShadow(text.toText(), centerX, y, color, font)
    }

    fun drawRightAlignedText(text: String, rightX: Number, y: Number, color: Color, font: Identifier? = this.overwrittenFont) {
        val textWidth = getTextWidth(text, font)
        drawText(text, rightX.toFloat() - textWidth, y, color, font)
    }

    fun drawRightAlignedTextWithShadow(text: String, rightX: Number, y: Number, color: Color, font: Identifier? = this.overwrittenFont) {
        val textWidth = getTextWidth(text)
        drawTextWithShadow(text, rightX.toFloat() - textWidth, y, color, font)
    }

    fun drawRightAlignedText(text: Component, rightX: Number, y: Number, color: Color, font: Identifier? = this.overwrittenFont) {
        val textWidth = getTextWidth(text, font)
        drawText(text, rightX.toFloat() - textWidth, y, color, font)
    }

    fun drawRightAlignedTextWithShadow(text: Component, rightX: Number, y: Number, color: Color, font: Identifier? = this.overwrittenFont) {
        val textWidth = getTextWidth(text, font)
        drawTextWithShadow(text, rightX.toFloat() - textWidth, y, color, font)
    }

    fun drawWrappedText(text: String, x: Number, y: Number, maxWidth: Int, color: Color, font: Identifier? = this.overwrittenFont): List<String> {
        return wrapText(text, maxWidth, font).also { drawWrappedText(it, x, y, color, font) }
    }

    fun drawWrappedText(lines: List<String>, x: Number, y: Number, color: Color, font: Identifier? = this.overwrittenFont) {
        val lineHeight = getTextHeight()
        for (i in lines.indices) {
            drawText(lines[i], x, y.toFloat() + i * lineHeight, color, font)
        }
    }

    fun drawWrappedText(text: Component, x: Number, y: Number, maxWidth: Int, color: Color, font: Identifier? = this.overwrittenFont): List<String> {
        return drawWrappedText(text.string, x, y, maxWidth, color, font)
    }

    fun drawCenteredWrappedText(lines: List<String>, centerX: Number, y: Number, color: Color, font: Identifier? = this.overwrittenFont, shadow: Boolean = false) {
        val lineHeight = getTextHeight()
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

    fun drawCenteredWrappedText(text: String, centerX: Int, y: Int, maxWidth: Int, color: Color, font: Identifier? = this.overwrittenFont, shadow: Boolean = false): List<String> {
        return wrapText(text, maxWidth, font).also { drawCenteredWrappedText(it, centerX, y, color, font, shadow) }
    }

    fun drawCenteredWrappedText(text: Component, centerX: Int, y: Int, maxWidth: Int, color: Color, font: Identifier? = this.overwrittenFont, shadow: Boolean = false): List<String> {
        return drawCenteredWrappedText(text.string, centerX, y, maxWidth, color, font, shadow)
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
    fun getTextWidth(text: Component, font: Identifier? = this.overwrittenFont): Int {
        if ((font == ScreenDrawingInterface.BEWISCLIENT_FONT || (font == null && this.overwrittenFont == ScreenDrawingInterface.BEWISCLIENT_FONT)) && isMinecrafty) {
            return textRenderer.width(text.setFont(ScreenDrawingInterface.DEFAULT_FONT)) * 85 / 100
        }
        return textRenderer.width(text.setFont(font))
    }

    /**
     * Gets the height of a line of text.
     *
     * @return The height of a line of text in pixels.
     */
    fun getTextHeight(): Int {
        return textRenderer.lineHeight
    }
}