package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.logic.color.Color
import net.minecraft.text.MutableText
import net.minecraft.text.Text

interface TextDrawing : RectDrawing {
    fun drawText(text: String, x: Int, y: Int, color: Color) {
        drawContext.drawText(textRenderer, Text.literal(text).fillStyle(style), x, y, applyAlpha(color), false)
    }

    fun drawText(text: Text, x: Int, y: Int, color: Color) {
        drawContext.drawText(textRenderer, text.copy().fillStyle(style), x, y, applyAlpha(color), false)
    }

    fun drawTextWithShadow(text: String, x: Int, y: Int, color: Color) {
        drawTextWithShadow(Text.literal(text), x, y, color)
    }

    fun drawTextWithShadow(text: Text, x: Int, y: Int, color: Color) {
        drawContext.drawText(textRenderer, text.copy().fillStyle(style), x, y, applyAlpha(color), true)
    }

    fun drawCenteredText(text: String, centerX: Int, y: Int, color: Color) {
        drawCenteredText(Text.literal(text), centerX, y, color)
    }

    fun drawCenteredText(text: Text, centerX: Int, y: Int, color: Color) {
        val textWidth = textRenderer.getWidth(text.copy().fillStyle(style))
        translate(-textWidth / 2f, 0f) {
            drawContext.drawText(textRenderer, text.copy().fillStyle(style), centerX, y, applyAlpha(color), false)
        }
    }

    fun drawCenteredTextWithShadow(text: Text, centerX: Int, y: Int, color: Color) {
        val textWidth = textRenderer.getWidth(text.copy().fillStyle(style))
        drawContext.drawText(textRenderer, text.copy().fillStyle(style), centerX - textWidth / 2, y, applyAlpha(color), true)
    }

    fun drawCenteredTextWithShadow(text: String, centerX: Int, y: Int, color: Color) {
        drawCenteredTextWithShadow(Text.literal(text), centerX, y, color)
    }

    fun drawRightAlignedText(text: String, rightX: Int, y: Int, color: Color) {
        val textWidth = textRenderer.getWidth(Text.literal(text).fillStyle(style))
        drawContext.drawText(textRenderer, Text.literal(text).fillStyle(style), rightX - textWidth, y, applyAlpha(color), false)
    }

    fun drawRightAlignedTextWithShadow(text: String, rightX: Int, y: Int, color: Color) {
        val textWidth = getTextWidth(text)
        drawContext.drawText(textRenderer, Text.literal(text).fillStyle(style), rightX - textWidth, y, applyAlpha(color), true)
    }

    fun drawWrappedText(text: String, x: Int, y: Int, maxWidth: Int, color: Color): List<String> {
        return wrapText(text, maxWidth).also { drawWrappedText(it, x, y, color) }
    }

    fun drawWrappedText(lines: List<String>, x: Int, y: Int, color: Color) {
        val lineHeight = textRenderer.fontHeight
        for (i in lines.indices) {
            drawContext.drawText(
                textRenderer, literalWithStyle(lines[i]), x, y + i * lineHeight, applyAlpha(color), false
            )
        }
    }

    fun drawWrappedText(text: Text, x: Int, y: Int, maxWidth: Int, color: Color): List<String> {
        return drawWrappedText(text.string, x, y, maxWidth, color)
    }

    fun drawCenteredWrappedText(text: String, centerX: Int, y: Int, maxWidth: Int, color: Color): List<String> {
        return wrapText(text, maxWidth).let { lines ->
            val lineHeight = textRenderer.fontHeight
            for (i in lines.indices) {
                drawCenteredText(
                    lines[i], centerX, y + i * lineHeight, color
                )
            }
            lines
        }
    }

    fun drawCenteredWrappedText(text: Text, centerX: Int, y: Int, maxWidth: Int, color: Color): List<String> {
        return drawCenteredWrappedText(text.string, centerX, y, maxWidth, color)
    }

    /**
     * Wraps text to fit within the specified width.
     *
     * @param maxWidth The maximum width for each line.
     * @return A list of strings, each representing a line of wrapped text.
     */
    fun wrapText(text: String, maxWidth: Int): List<String> {
        val lines = mutableListOf<String>()

        val paragraphs = text.split("\n")

        for (paragraph in paragraphs) {
            val words = paragraph.split(" ")
            var currentLine = ""

            for (word in words) {
                val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"

                if (textRenderer.getWidth(Text.literal(testLine).fillStyle(style)) <= maxWidth) {
                    currentLine = testLine
                } else {
                    if (currentLine.isNotEmpty()) {
                        lines.add(currentLine)
                        currentLine = word
                    }

                    while (textRenderer.getWidth(Text.literal(currentLine).fillStyle(style)) > maxWidth && currentLine.isNotEmpty()) {
                        var cutIndex = currentLine.length - 1

                        while (cutIndex > 0 && textRenderer.getWidth(Text.literal(currentLine.take(cutIndex)).fillStyle(style)) > maxWidth) {
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
    fun getTextWidth(text: String): Int {
        return textRenderer.getWidth(Text.literal(text).setStyle(style))
    }

    /**
     * Gets the width of the specified text when rendered.
     *
     * @return The width of the text in pixels.
     */
    fun getTextWidth(text: Text): Int {
        return textRenderer.getWidth(text.copy().setStyle(style))
    }

    /**
     * Gets the height of a line of text.
     *
     * @return The height of a line of text in pixels.
     */
    fun getTextHeight(): Int {
        return textRenderer.fontHeight
    }

    fun literalWithStyle(text: String): MutableText = Text.literal(text).fillStyle(style)
}