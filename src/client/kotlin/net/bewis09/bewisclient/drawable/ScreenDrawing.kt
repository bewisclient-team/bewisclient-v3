package net.bewis09.bewisclient.drawable

import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.state.EntityRenderState
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.atan

/**
 * A class representing a screen drawing context in Bewisclient.
 * This class is used to encapsulate the drawing context
 *
 * @param drawContext The DrawContext used for drawing operations. Using it directly is extremely discouraged.
 */
@Suppress("Unused")
class ScreenDrawing(val drawContext: DrawContext) {
    private val textRenderer: TextRenderer
        get() = MinecraftClient.getInstance().textRenderer

    /**
     * Fills a rectangle on the screen with the specified color.
     *
     * @param color The color to fill the rectangle with, represented as an ARGB integer.
     */
    fun fill(x: Int, y: Int, width: Int, height: Int, color: Int) {
        drawContext.fill(x, y, x + width, y + height, color)
    }

    /**
     * Fills a rectangle on the screen with the specified color and alpha value.
     *
     * @param color The color to fill the rectangle with, represented as an RGB integer. The alpha value is applied separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun fill(x: Int, y: Int, width: Int, height: Int, color: Int, alpha: Float) {
        fill(x, y, width, height, color or (alpha * 255).toInt() shl 24)
    }

    /**
     * Fills a rectangle on the screen with the specified RGB color and alpha value.
     *
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun fill(x: Int, y: Int, width: Int, height: Int, r: Int, g: Int, b: Int, alpha: Float = 1.0f) {
        fill(x, y, width, height, ((alpha * 255).toInt() shl 24) or (r shl 16) or (g shl 8) or b)
    }

    /**
     * Fills a rectangle on the screen with the specified RGB color and alpha value.
     *
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun fill(x: Int, y: Int, width: Int, height: Int, r: Float, g: Float, b: Float, alpha: Float = 1.0f) {
        fill(x, y, width, height, (r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt(), alpha)
    }

    /**
     * Draws a border inside a rectangle on the screen with the specified color.
     *
     * @param color The color to fill the rectangle with, represented as an ARGB integer.
     */
    fun drawBorder(x: Int, y: Int, width: Int, height: Int, color: Int) {
        drawContext.drawBorder(x, y, width, height, color)
    }

    /**
     * Draws a border inside a rectangle on the screen with the specified color and alpha value.
     *
     * @param color The color to fill the rectangle with, represented as an RGB integer. The alpha value is applied separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawBorder(x: Int, y: Int, width: Int, height: Int, color: Int, alpha: Float) {
        drawBorder(x, y, width, height, color or (alpha * 255).toInt() shl 24)
    }

    /**
     * Draws a border inside a rectangle on the screen with the specified RGB color and alpha value.
     *
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawBorder(x: Int, y: Int, width: Int, height: Int, r: Int, g: Int, b: Int, alpha: Float = 1.0f) {
        drawBorder(x, y, width, height, ((alpha * 255).toInt() shl 24) or (r shl 16) or (g shl 8) or b)
    }

    /**
     * Draws a border inside a rectangle on the screen with the specified RGB color and alpha value.
     *
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawBorder(x: Int, y: Int, width: Int, height: Int, r: Float, g: Float, b: Float, alpha: Float = 1.0f) {
        drawBorder(x, y, width, height, (r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt(), alpha)
    }

    /**
     * Draws a rectangle and a border with the specified colors on screen.
     *
     * @param color The color to fill the rectangle with, represented as an ARGB integer.
     * @param borderColor The color of the border, represented as an ARGB integer.
     */
    fun fillWithBorder(x: Int, y: Int, width: Int, height: Int, color: Int, borderColor: Int) {
        fill(x, y, width, height, color)
        drawBorder(x, y, width, height, borderColor)
    }

    /**
     * Draws a rectangle and a border with the specified RGB colors on screen.
     *
     * @param color The color to fill the rectangle with, represented as an RGB integer.
     * @param borderColor The color of the border, represented as an RGB integer.
     * @param alpha The alpha value for both the rectangle and the border, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun fillWithBorder(x: Int, y: Int, width: Int, height: Int, color: Int, borderColor: Int, alpha: Float) {
        fill(x, y, width, height, color or (alpha * 255).toInt() shl 24)
        drawBorder(x, y, width, height, borderColor or (alpha * 255).toInt() shl 24)
    }

    /**
     * Draws a rectangle and a border with the specified RGB colors on screen.
     *
     * @param r The red component of the rectangle color, ranging from 0 to 255.
     * @param g The green component of the rectangle color, ranging from 0 to 255.
     * @param b The blue component of the rectangle color, ranging from 0 to 255.
     * @param borderR The red component of the border color, ranging from 0 to 255.
     * @param borderG The green component of the border color, ranging from 0 to 255.
     * @param borderB The blue component of the border color, ranging from 0 to 255.
     * @param alpha The alpha value for both the rectangle and the border, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun fillWithBorder(x: Int, y: Int, width: Int, height: Int, r: Int, g: Int, b: Int, borderR: Int, borderG: Int, borderB: Int, alpha: Float = 1.0f) {
        fill(x, y, width, height, ((alpha * 255).toInt() shl 24) or (r shl 16) or (g shl 8) or b)
        drawBorder(x, y, width, height, ((alpha * 255).toInt() shl 24) or (borderR shl 16) or (borderG shl 8) or borderB)
    }

    /**
     * Draws a rectangle and a border with the specified RGB colors on screen.
     *
     * @param r The red component of the rectangle color, ranging from 0.0 to 1.0.
     * @param g The green component of the rectangle color, ranging from 0.0 to 1.0.
     * @param b The blue component of the rectangle color, ranging from 0.0 to 1.0.
     * @param borderR The red component of the border color, ranging from 0.0 to 1.0.
     * @param borderG The green component of the border color, ranging from 0.0 to 1.0.
     * @param borderB The blue component of the border color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for both the rectangle and the border, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun fillWithBorder(x: Int, y: Int, width: Int, height: Int, r: Float, g: Float, b: Float, borderR: Float, borderG: Float, borderB: Float, alpha: Float = 1.0f) {
        fill(x, y, width, height, (r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt(), alpha)
        drawBorder(x, y, width, height, (borderR * 255).toInt(), (borderG * 255).toInt(), (borderB * 255).toInt(), alpha)
    }

    /**
     * Draws a rectangle and a border with the specified colors on screen.
     *
     * @param color The color to fill the rectangle with, represented as an RGB integer.
     * @param alpha The alpha value for the rectangle, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     * @param borderColor The color of the border, represented as an RGB integer.
     * @param borderAlpha The alpha value for the border, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun fillWithBorder(x: Int, y: Int, width: Int, height: Int, color: Int, alpha: Float, borderColor: Int, borderAlpha: Float) {
        fill(x, y, width, height, color or (alpha * 255).toInt() shl 24)
        drawBorder(x, y, width, height, borderColor or (borderAlpha * 255).toInt() shl 24)
    }

    /**
     * Draws a rectangle and a border with the specified RGB colors on screen.
     *
     * @param r The red component of the rectangle color, ranging from 0 to 255.
     * @param g The green component of the rectangle color, ranging from 0 to 255.
     * @param b The blue component of the rectangle color, ranging from 0 to 255.
     * @param alpha The alpha value for the rectangle, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     * @param borderR The red component of the border color, ranging from 0 to 255.
     * @param borderG The green component of the border color, ranging from 0 to 255.
     * @param borderB The blue component of the border color, ranging from 0 to 255.
     * @param borderAlpha The alpha value for the border, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun fillWithBorder(x: Int, y: Int, width: Int, height: Int, r: Int, g: Int, b: Int, alpha: Float = 1.0f, borderR: Int, borderG: Int, borderB: Int, borderAlpha: Float = 1.0f) {
        fill(x, y, width, height, ((alpha * 255).toInt() shl 24) or (r shl 16) or (g shl 8) or b)
        drawBorder(x, y, width, height, ((borderAlpha * 255).toInt() shl 24) or (borderR shl 16) or (borderG shl 8) or borderB)
    }

    /**
     * Draws a rectangle and a border with the specified RGB colors on screen.
     *
     * @param r The red component of the rectangle color, ranging from 0.0 to 1.0.
     * @param g The green component of the rectangle color, ranging from 0.0 to 1.0.
     * @param b The blue component of the rectangle color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the rectangle, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     * @param borderR The red component of the border color, ranging from 0.0 to 1.0.
     * @param borderG The green component of the border color, ranging from 0.0 to 1.0.
     * @param borderB The blue component of the border color, ranging from 0.0 to 1.0.
     * @param borderAlpha The alpha value for the border, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun fillWithBorder(x: Int, y: Int, width: Int, height: Int, r: Float, g: Float, b: Float, alpha: Float = 1.0f, borderR: Float, borderG: Float, borderB: Float, borderAlpha: Float = 1.0f) {
        fill(x, y, width, height, (r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt(), alpha)
        drawBorder(x, y, width, height, (borderR * 255).toInt(), (borderG * 255).toInt(), (borderB * 255).toInt(), borderAlpha)
    }

    /**
     * Translates the drawing context by the specified x and y offsets.
     *
     * @param x The x offset to translate the context by.
     * @param y The y offset to translate the context by.
     */
    fun translate(x: Float, y: Float) {
        drawContext.matrices.translate(x,y)
    }

    /**
     * Scales the drawing context by the specified x and y factors.
     *
     * @param x The x factor to scale the context by.
     * @param y The y factor to scale the context by.
     */
    fun scale(x: Float, y: Float) {
        drawContext.matrices.scale(x,y)
    }

    /**
     * Rotates the drawing context by the specified angle in degrees.
     *
     * @param angle The angle in degrees to rotate the context by.
     */
    fun rotateDegrees(angle: Float) {
        drawContext.matrices.rotate(Math.toRadians(angle.toDouble()).toFloat())
    }

    /**
     * Rotates the drawing context by the specified angle in radians.
     *
     * @param angle The angle in radians to rotate the context by.
     */
    fun rotate(angle: Float) {
        drawContext.matrices.rotate(angle)
    }

    /**
     * Pushes a new matrix onto the drawing context's matrix stack.
     * This is used to save the current transformation state so that it can be restored later.
     */
    fun push() {
        drawContext.matrices.pushMatrix()
    }

    /**
     * Pops the last matrix from the drawing context's matrix stack.
     * This restores the previous transformation state that was saved by a push operation.
     */
    fun pop() {
        drawContext.matrices.popMatrix()
    }

    /**
     * Moves the drawing context up one layer in the rendering stack.
     * This is used to change the rendering order of elements on the screen.
     */
    fun goUpLayer() {
        drawContext.state.goUpLayer()
    }

    /**
     * Moves the drawing context down one layer in the rendering stack.
     * This is used to change the rendering order of elements on the screen.
     */
    fun goDownLayer() {
        drawContext.state.goDownLayer()
    }

    // Text rendering methods

    /**
     * Draws text at the specified position with the given color.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawText(text: String, x: Int, y: Int, color: Int) {
        drawContext.drawText(textRenderer, text, x, y, color, false)
    }

    /**
     * Draws text at the specified position with the given color and alpha value.
     *
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawText(text: String, x: Int, y: Int, color: Int, alpha: Float) {
        drawContext.drawText(textRenderer, text, x, y, color or (alpha * 255).toInt() shl 24, false)
    }

    /**
     * Draws text at the specified position with the given color.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawText(text: Text, x: Int, y: Int, color: Int) {
        drawContext.drawText(textRenderer, text, x, y, color, false)
    }

    /**
     * Draws text at the specified position with the given color and alpha value.
     *
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawText(text: Text, x: Int, y: Int, color: Int, alpha: Float) {
        drawContext.drawText(textRenderer, text, x, y, color or (alpha * 255).toInt() shl 24, false)
    }

    /**
     * Draws text with a shadow at the specified position.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawTextWithShadow(text: String, x: Int, y: Int, color: Int) {
        drawContext.drawText(textRenderer, text, x, y, color, true)
    }

    /**
     * Draws text with a shadow at the specified position.
     *
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawTextWithShadow(text: String, x: Int, y: Int, color: Int, alpha: Float) {
        drawContext.drawText(textRenderer, text, x, y, color or (alpha * 255).toInt() shl 24, true)
    }

    /**
     * Draws text with a shadow at the specified position.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawTextWithShadow(text: Text, x: Int, y: Int, color: Int) {
        drawContext.drawText(textRenderer, text, x, y, color, true)
    }

    /**
     * Draws text with a shadow at the specified position.
     *
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawTextWithShadow(text: Text, x: Int, y: Int, color: Int, alpha: Float) {
        drawContext.drawText(textRenderer, text, x, y, color or (alpha * 255).toInt() shl 24, true)
    }

    /**
     * Draws text centered at the specified x position.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawCenteredText(text: String, centerX: Int, y: Int, color: Int) {
        val textWidth = textRenderer.getWidth(text)
        drawContext.drawText(textRenderer, text, centerX - textWidth / 2, y, color, false)
    }

    /**
     * Draws text centered at the specified x position.
     *
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawCenteredText(text: String, centerX: Int, y: Int, color: Int, alpha: Float) {
        val textWidth = textRenderer.getWidth(text)
        drawContext.drawText(textRenderer, text, centerX - textWidth / 2, y, color or (alpha * 255).toInt() shl 24, false)
    }

    /**
     * Draws text centered at the specified x position with a shadow.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawCenteredTextWithShadow(text: String, centerX: Int, y: Int, color: Int) {
        val textWidth = textRenderer.getWidth(text)
        drawContext.drawText(textRenderer, text, centerX - textWidth / 2, y, color, true)
    }

    /**
     * Draws text centered at the specified x position with a shadow.
     *
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawCenteredTextWithShadow(text: String, centerX: Int, y: Int, color: Int, alpha: Float) {
        val textWidth = textRenderer.getWidth(text)
        drawContext.drawText(textRenderer, text, centerX - textWidth / 2, y, color or (alpha * 255).toInt() shl 24, true)
    }

    /**
     * Draws text right-aligned at the specified x position.
     *
     * @param rightX The x coordinate where the text should end (right edge).
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawRightAlignedText(text: String, rightX: Int, y: Int, color: Int) {
        val textWidth = textRenderer.getWidth(text)
        drawContext.drawText(textRenderer, text, rightX - textWidth, y, color, false)
    }

    /**
     * Draws text right-aligned at the specified x position.
     *
     * @param rightX The x coordinate where the text should end (right edge).
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawRightAlignedText(text: String, rightX: Int, y: Int, color: Int, alpha: Float) {
        val textWidth = textRenderer.getWidth(text)
        drawContext.drawText(textRenderer, text, rightX - textWidth, y, color or (alpha * 255).toInt() shl 24, false)
    }

    /**
     * Draws text right-aligned at the specified x position with a shadow.
     *
     * @param rightX The x coordinate where the text should end (right edge).
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawRightAlignedTextWithShadow(text: String, rightX: Int, y: Int, color: Int) {
        val textWidth = textRenderer.getWidth(text)
        drawContext.drawText(textRenderer, text, rightX - textWidth, y, color, true)
    }

    /**
     * Draws text right-aligned at the specified x position with a shadow.
     *
     * @param rightX The x coordinate where the text should end (right edge).
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawRightAlignedTextWithShadow(text: String, rightX: Int, y: Int, color: Int, alpha: Float) {
        val textWidth = textRenderer.getWidth(text)
        drawContext.drawText(textRenderer, text, rightX - textWidth, y, color or (alpha * 255).toInt() shl 24, true)
    }

    /**
     * Draws wrapped text within the specified width bounds.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param color The color of the text, represented as an ARGB integer.
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawWrappedText(text: String, x: Int, y: Int, maxWidth: Int, color: Int): List<String> {
        return wrapText(text, maxWidth).let { lines ->
            val lineHeight = textRenderer.fontHeight
            for (i in lines.indices) {
                drawContext.drawText(textRenderer, lines[i], x, y + i * lineHeight, color, false)
            }
            lines
        }
    }

    /**
     * Draws wrapped text within the specified width bounds.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawWrappedText(text: String, x: Int, y: Int, maxWidth: Int, color: Int, alpha: Float): List<String> {
        return wrapText(text, maxWidth).let { lines ->
            val lineHeight = textRenderer.fontHeight
            val colorWithAlpha = color or (alpha * 255).toInt() shl 24
            for (i in lines.indices) {
                drawContext.drawText(textRenderer, lines[i], x, y + i * lineHeight, colorWithAlpha, false)
            }
            lines
        }
    }

    /**
     * Draws wrapped text within the specified width bounds.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param color The color of the text, represented as an ARGB integer.
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawWrappedText(text: Text, x: Int, y: Int, maxWidth: Int, color: Int): List<String> {
        return drawWrappedText(text.string, x, y, maxWidth, color)
    }

    /**
     * Draws wrapped text within the specified width bounds.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawWrappedText(text: Text, x: Int, y: Int, maxWidth: Int, color: Int, alpha: Float): List<String> {
        return drawWrappedText(text.string, x, y, maxWidth, color, alpha)
    }

    /**
     * Wraps text to fit within the specified width.
     *
     * @param maxWidth The maximum width for each line.
     * @return A list of strings, each representing a line of wrapped text.
     */
    private fun wrapText(text: String, maxWidth: Int): List<String> {
        val lines = mutableListOf<String>()

        val paragraphs = text.split("\n")

        for (paragraph in paragraphs) {
            val words = paragraph.split(" ")
            var currentLine = ""

            for (word in words) {
                val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"

                if (textRenderer.getWidth(testLine) <= maxWidth) {
                    currentLine = testLine
                } else {
                    if (currentLine.isNotEmpty()) {
                        lines.add(currentLine)
                        currentLine = word
                    }

                    while (textRenderer.getWidth(currentLine) > maxWidth && currentLine.isNotEmpty()) {
                        var cutIndex = currentLine.length - 1

                        while (cutIndex > 0 && textRenderer.getWidth(currentLine.substring(0, cutIndex)) > maxWidth) {
                            cutIndex--
                        }

                        if (cutIndex == 0) cutIndex = 1

                        lines.add(currentLine.substring(0, cutIndex))
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
        return textRenderer.getWidth(text)
    }

    /**
     * Gets the width of the specified text when rendered.
     *
     * @return The width of the text in pixels.
     */
    fun getTextWidth(text: Text): Int {
        return textRenderer.getWidth(text)
    }

    /**
     * Gets the height of a line of text.
     *
     * @return The height of a line of text in pixels.
     */
    fun getTextHeight(): Int {
        return textRenderer.fontHeight
    }

    /**
     * Draws a texture at the specified position.
     *
     * @param texture The texture identifier to draw.
     */
    fun drawTexture(texture: Identifier, x: Int, y: Int, width: Int, height: Int) {
        // This may need to be adjusted based on available DrawContext methods
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, 0f, 0f, width, height, width, height)
    }

    /**
     * Draws a texture at the specified position with UV coordinates.
     *
     * @param texture The texture identifier to draw.
     * @param u The U coordinate (x) in the texture atlas.
     * @param v The V coordinate (y) in the texture atlas.
     * @param textureWidth The total width of the texture.
     * @param textureHeight The total height of the texture.
     */
    fun drawTexture(texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int) {
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, textureWidth, textureHeight)
    }

    /**
     * Draws a texture at the specified position with UV coordinates and color tint.
     *
     * @param texture The texture identifier to draw.
     * @param u The U coordinate (x) in the texture atlas.
     * @param v The V coordinate (y) in the texture atlas.
     * @param textureWidth The total width of the texture.
     * @param textureHeight The total height of the texture.
     * @param color The color tint to apply, represented as an ARGB integer.
     */
    fun drawTexture(texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int, color: Int) {
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, textureWidth, textureHeight, color)
    }

    /**
     * Draws a texture at the specified position with basic dimensions and color tint.
     *
     * @param texture The texture identifier to draw.
     * @param color The color tint to apply, represented as an RGB integer.
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawTexture(texture: Identifier, x: Int, y: Int, width: Int, height: Int, color: Int, alpha: Float) {
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, 0f, 0f, width, height, width, height, color or (alpha * 255).toInt() shl 24)
    }

    /**
     * Draws a texture at the specified position with basic dimensions and RGB color tint.
     *
     * @param texture The texture identifier to draw.
     * @param r The red component of the tint, ranging from 0 to 255.
     * @param g The green component of the tint, ranging from 0 to 255.
     * @param b The blue component of the tint, ranging from 0 to 255.
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawTexture(texture: Identifier, x: Int, y: Int, width: Int, height: Int, r: Int, g: Int, b: Int, alpha: Float = 1.0f) {
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, 0f, 0f, width, height, width, height, ((alpha * 255).toInt() shl 24) or (r shl 16) or (g shl 8) or b)
    }

    /**
     * Draws a texture at the specified position with basic dimensions and RGB color tint.
     *
     * @param texture The texture identifier to draw.
     * @param r The red component of the tint, ranging from 0.0 to 1.0.
     * @param g The green component of the tint, ranging from 0.0 to 1.0.
     * @param b The blue component of the tint, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawTexture(texture: Identifier, x: Int, y: Int, width: Int, height: Int, r: Float, g: Float, b: Float, alpha: Float = 1.0f) {
        drawTexture(texture, x, y, width, height, (r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt(), alpha)
    }

    /**
     * Draws a texture at the specified position with UV coordinates and RGB color tint.
     *
     * @param texture The texture identifier to draw.
     * @param u The U coordinate (x) in the texture atlas.
     * @param v The V coordinate (y) in the texture atlas.
     * @param textureWidth The total width of the texture.
     * @param textureHeight The total height of the texture.
     * @param color The color tint to apply, represented as an RGB integer.
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawTexture(texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int, color: Int, alpha: Float) {
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, textureWidth, textureHeight, color or (alpha * 255).toInt() shl 24)
    }

    /**
     * Draws a texture at the specified position with UV coordinates and RGB color tint.
     *
     * @param texture The texture identifier to draw.
     * @param u The U coordinate (x) in the texture atlas.
     * @param v The V coordinate (y) in the texture atlas.
     * @param textureWidth The total width of the texture.
     * @param textureHeight The total height of the texture.
     * @param r The red component of the tint, ranging from 0 to 255.
     * @param g The green component of the tint, ranging from 0 to 255.
     * @param b The blue component of the tint, ranging from 0 to 255.
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawTexture(texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int, r: Int, g: Int, b: Int, alpha: Float = 1.0f) {
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, textureWidth, textureHeight, ((alpha * 255).toInt() shl 24) or (r shl 16) or (g shl 8) or b)
    }

    /**
     * Draws a texture at the specified position with UV coordinates and RGB color tint.
     *
     * @param texture The texture identifier to draw.
     * @param u The U coordinate (x) in the texture atlas.
     * @param v The V coordinate (y) in the texture atlas.
     * @param textureWidth The total width of the texture.
     * @param textureHeight The total height of the texture.
     * @param r The red component of the tint, ranging from 0.0 to 1.0.
     * @param g The green component of the tint, ranging from 0.0 to 1.0.
     * @param b The blue component of the tint, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawTexture(texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int, r: Float, g: Float, b: Float, alpha: Float = 1.0f) {
        drawTexture(texture, x, y, u, v, width, height, textureWidth, textureHeight, (r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt(), alpha)
    }

    /**
     * Draws a texture with specific region dimensions.
     *
     * @param texture The texture identifier to draw.
     * @param u The U coordinate (x) in the texture atlas.
     * @param v The V coordinate (y) in the texture atlas.
     * @param regionWidth The width of the texture region to draw.
     * @param regionHeight The height of the texture region to draw.
     * @param textureWidth The total width of the texture.
     * @param textureHeight The total height of the texture.
     */
    fun drawTextureRegion(texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int) {
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight)
    }

    /**
     * Draws a texture with specific region dimensions and color tint.
     *
     * @param texture The texture identifier to draw.
     * @param u The U coordinate (x) in the texture atlas.
     * @param v The V coordinate (y) in the texture atlas.
     * @param regionWidth The width of the texture region to draw.
     * @param regionHeight The height of the texture region to draw.
     * @param textureWidth The total width of the texture.
     * @param textureHeight The total height of the texture.
     * @param color The color tint to apply, represented as an ARGB integer.
     */
    fun drawTextureRegion(texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, color: Int) {
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color)
    }

    /**
     * Draws a texture with specific region dimensions and RGB color tint.
     *
     * @param texture The texture identifier to draw.
     * @param u The U coordinate (x) in the texture atlas.
     * @param v The V coordinate (y) in the texture atlas.
     * @param regionWidth The width of the texture region to draw.
     * @param regionHeight The height of the texture region to draw.
     * @param textureWidth The total width of the texture.
     * @param textureHeight The total height of the texture.
     * @param color The color tint to apply, represented as an RGB integer.
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawTextureRegion(texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, color: Int, alpha: Float) {
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color or (alpha * 255).toInt() shl 24)
    }

    /**
     * Draws a texture with specific region dimensions and RGB color tint.
     *
     * @param texture The texture identifier to draw.
     * @param u The U coordinate (x) in the texture atlas.
     * @param v The V coordinate (y) in the texture atlas.
     * @param regionWidth The width of the texture region to draw.
     * @param regionHeight The height of the texture region to draw.
     * @param textureWidth The total width of the texture.
     * @param textureHeight The total height of the texture.
     * @param r The red component of the tint, ranging from 0 to 255.
     * @param g The green component of the tint, ranging from 0 to 255.
     * @param b The blue component of the tint, ranging from 0 to 255.
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawTextureRegion(texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, r: Int, g: Int, b: Int, alpha: Float = 1.0f) {
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, ((alpha * 255).toInt() shl 24) or (r shl 16) or (g shl 8) or b)
    }

    /**
     * Draws a texture with specific region dimensions and RGB color tint.
     *
     * @param texture The texture identifier to draw.
     * @param u The U coordinate (x) in the texture atlas.
     * @param v The V coordinate (y) in the texture atlas.
     * @param regionWidth The width of the texture region to draw.
     * @param regionHeight The height of the texture region to draw.
     * @param textureWidth The total width of the texture.
     * @param textureHeight The total height of the texture.
     * @param r The red component of the tint, ranging from 0.0 to 1.0.
     * @param g The green component of the tint, ranging from 0.0 to 1.0.
     * @param b The blue component of the tint, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawTextureRegion(texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, r: Float, g: Float, b: Float, alpha: Float = 1.0f) {
        drawTextureRegion(texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, (r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt(), alpha)
    }

    /**
     * Draws a simple sprite from a texture atlas.
     *
     * @param texture The texture identifier to draw.
     * @param spriteU The U coordinate of the sprite in the atlas.
     * @param spriteV The V coordinate of the sprite in the atlas.
     * @param textureWidth The total width of the texture atlas.
     * @param textureHeight The total height of the texture atlas.
     */
    fun drawSprite(texture: Identifier, x: Int, y: Int, width: Int, height: Int, spriteU: Float, spriteV: Float, textureWidth: Int, textureHeight: Int) {
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, spriteU, spriteV, width, height, textureWidth, textureHeight)
    }

    /**
     * Draws a simple sprite from a texture atlas with color tint.
     *
     * @param texture The texture identifier to draw.
     * @param spriteU The U coordinate of the sprite in the atlas.
     * @param spriteV The V coordinate of the sprite in the atlas.
     * @param textureWidth The total width of the texture atlas.
     * @param textureHeight The total height of the texture atlas.
     * @param color The color tint to apply, represented as an ARGB integer.
     */
    fun drawSprite(texture: Identifier, x: Int, y: Int, width: Int, height: Int, spriteU: Float, spriteV: Float, textureWidth: Int, textureHeight: Int, color: Int) {
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, spriteU, spriteV, width, height, textureWidth, textureHeight, color)
    }

    /**
     * Draws a simple sprite from a texture atlas with RGB color tint.
     *
     * @param texture The texture identifier to draw.
     * @param spriteU The U coordinate of the sprite in the atlas.
     * @param spriteV The V coordinate of the sprite in the atlas.
     * @param textureWidth The total width of the texture atlas.
     * @param textureHeight The total height of the texture atlas.
     * @param color The color tint to apply, represented as an RGB integer.
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawSprite(texture: Identifier, x: Int, y: Int, width: Int, height: Int, spriteU: Float, spriteV: Float, textureWidth: Int, textureHeight: Int, color: Int, alpha: Float) {
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, spriteU, spriteV, width, height, textureWidth, textureHeight, color or (alpha * 255).toInt() shl 24)
    }

    /**
     * Draws a simple sprite from a texture atlas with RGB color tint.
     *
     * @param texture The texture identifier to draw.
     * @param spriteU The U coordinate of the sprite in the atlas.
     * @param spriteV The V coordinate of the sprite in the atlas.
     * @param textureWidth The total width of the texture atlas.
     * @param textureHeight The total height of the texture atlas.
     * @param r The red component of the tint, ranging from 0 to 255.
     * @param g The green component of the tint, ranging from 0 to 255.
     * @param b The blue component of the tint, ranging from 0 to 255.
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawSprite(texture: Identifier, x: Int, y: Int, width: Int, height: Int, spriteU: Float, spriteV: Float, textureWidth: Int, textureHeight: Int, r: Int, g: Int, b: Int, alpha: Float = 1.0f) {
        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, spriteU, spriteV, width, height, textureWidth, textureHeight, ((alpha * 255).toInt() shl 24) or (r shl 16) or (g shl 8) or b)
    }

    /**
     * Draws a simple sprite from a texture atlas with RGB color tint.
     *
     * @param texture The texture identifier to draw.
     * @param spriteU The U coordinate of the sprite in the atlas.
     * @param spriteV The V coordinate of the sprite in the atlas.
     * @param textureWidth The total width of the texture atlas.
     * @param textureHeight The total height of the texture atlas.
     * @param r The red component of the tint, ranging from 0.0 to 1.0.
     * @param g The green component of the tint, ranging from 0.0 to 1.0.
     * @param b The blue component of the tint, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    fun drawSprite(texture: Identifier, x: Int, y: Int, width: Int, height: Int, spriteU: Float, spriteV: Float, textureWidth: Int, textureHeight: Int, r: Float, g: Float, b: Float, alpha: Float = 1.0f) {
        drawSprite(texture, x, y, width, height, spriteU, spriteV, textureWidth, textureHeight, (r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt(), alpha)
    }

    /**
     * Gets access to the underlying DrawContext for advanced texture operations.
     * Use this only when the wrapper methods are insufficient.
     *
     * @return The underlying DrawContext instance.
     */
    fun getDrawContext(): DrawContext {
        return drawContext
    }

    /**
     * Draws a horizontal line.
     *
     * @param color The color of the line, represented as an ARGB integer.
     */
    fun drawHorizontalLine(startX: Int, y: Int, width: Int, color: Int) {
        fill(startX, y, width, 1, color)
    }

    /**
     * Draws a horizontal line with alpha.
     *
     * @param color The color of the line, represented as an RGB integer.
     * @param alpha The alpha value for the line, ranging from 0.0 to 1.0.
     */
    fun drawHorizontalLine(startX: Int, y: Int, width: Int, color: Int, alpha: Float) {
        fill(startX, y, width, 1, color, alpha)
    }

    /**
     * Draws a horizontal line with RGB color components.
     *
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the line, ranging from 0.0 to 1.0.
     */
    fun drawHorizontalLine(startX: Int, y: Int, width: Int, r: Int, g: Int, b: Int, alpha: Float = 1.0f) {
        fill(startX, y, width, 1, r, g, b, alpha)
    }

    /**
     * Draws a vertical line.
     *
     * @param color The color of the line, represented as an ARGB integer.
     */
    fun drawVerticalLine(x: Int, startY: Int, height: Int, color: Int) {
        fill(x, startY, 1, height, color)
    }

    /**
     * Draws a vertical line with alpha.
     *
     * @param color The color of the line, represented as an RGB integer.
     * @param alpha The alpha value for the line, ranging from 0.0 to 1.0.
     */
    fun drawVerticalLine(x: Int, startY: Int, height: Int, color: Int, alpha: Float) {
        fill(x, startY, 1, height, color, alpha)
    }

    /**
     * Draws a vertical line with RGB color components.
     *
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the line, ranging from 0.0 to 1.0.
     */
    fun drawVerticalLine(x: Int, startY: Int, height: Int, r: Int, g: Int, b: Int, alpha: Float = 1.0f) {
        fill(x, startY, 1, height, r, g, b, alpha)
    }

    // Gradient drawing methods

    /**
     * Draws a horizontal gradient from left to right.
     *
     * @param startColor The starting color on the left, represented as an ARGB integer.
     * @param endColor The ending color on the right, represented as an ARGB integer.
     */
    fun drawHorizontalGradient(x: Int, y: Int, width: Int, height: Int, startColor: Int, endColor: Int) {
        drawContext.fillGradient(x, y, x + width, y + height, startColor, endColor)
    }

    /**
     * Draws a horizontal gradient from left to right with alpha.
     *
     * @param startColor The starting color on the left, represented as an RGB integer.
     * @param endColor The ending color on the right, represented as an RGB integer.
     * @param alpha The alpha value for both colors, ranging from 0.0 to 1.0.
     */
    fun drawHorizontalGradient(x: Int, y: Int, width: Int, height: Int, startColor: Int, endColor: Int, alpha: Float) {
        val startColorWithAlpha = startColor or (alpha * 255).toInt() shl 24
        val endColorWithAlpha = endColor or (alpha * 255).toInt() shl 24
        drawContext.fillGradient(x, y, x + width, y + height, startColorWithAlpha, endColorWithAlpha)
    }

    /**
     * Draws a vertical gradient from top to bottom.
     *
     * @param startColor The starting color at the top, represented as an ARGB integer.
     * @param endColor The ending color at the bottom, represented as an ARGB integer.
     */
    fun drawVerticalGradient(x: Int, y: Int, width: Int, height: Int, startColor: Int, endColor: Int) {
        drawContext.fillGradient(x, y, x + width, y + height, startColor, endColor)
    }

    /**
     * Draws a vertical gradient from top to bottom with alpha.
     *
     * TODO Has to be tested, might not work as expected
     *
     * @param startColor The starting color at the top, represented as an RGB integer.
     * @param endColor The ending color at the bottom, represented as an RGB integer.
     * @param alpha The alpha value for both colors, ranging from 0.0 to 1.0.
     */
    fun drawVerticalGradient(x: Int, y: Int, width: Int, height: Int, startColor: Int, endColor: Int, alpha: Float) {
        val startColorWithAlpha = startColor or (alpha * 255).toInt() shl 24
        val endColorWithAlpha = endColor or (alpha * 255).toInt() shl 24
        push()
        rotate(-90f)
        translate((x-width).toFloat(), -y.toFloat())
        drawContext.fillGradient(0, 0, height, width, startColorWithAlpha, endColorWithAlpha)
        pop()
    }

    fun drawEntity(context: DrawContext, x1: Int, y1: Int, x2: Int, y2: Int, size: Int, scale: Float, mouseX: Float, mouseY: Float, entity: LivingEntity) {
        val f = (x1 + x2).toFloat() / 2.0f
        val g = (y1 + y2).toFloat() / 2.0f
        context.enableScissor(x1, y1, x2, y2)
        val h = atan(((f - mouseX) / 40.0f).toDouble()).toFloat()
        val i = atan(((g - mouseY) / 40.0f).toDouble()).toFloat()
        val quaternion = Quaternionf().rotateZ(Math.PI.toFloat())
        val quaternion2 = Quaternionf().rotateX(i * 20.0f * (Math.PI.toFloat() / 180))
        quaternion.mul(quaternion2)
        val j = entity.bodyYaw
        val k = entity.yaw
        val l = entity.pitch
        val m = entity.lastHeadYaw
        val n = entity.headYaw
        entity.bodyYaw = 180.0f + h * 20.0f
        entity.yaw = 180.0f + h * 40.0f
        entity.pitch = -i * 20.0f
        entity.headYaw = entity.yaw
        entity.lastHeadYaw = entity.yaw
        val o = entity.scale
        val vector3f = Vector3f(0.0f, entity.height / 2.0f + scale * o, 0.0f)
        val p = size.toFloat() / o
        InventoryScreen.drawEntity(context, x1, y1, x2, y2, p, vector3f, quaternion, quaternion2, entity)
        entity.bodyYaw = j
        entity.yaw = k
        entity.pitch = l
        entity.lastHeadYaw = m
        entity.headYaw = n
        context.disableScissor()
    }

    fun drawEntity(drawer: DrawContext, x1: Int, y1: Int, x2: Int, y2: Int, scale: Float, translation: Vector3f?, rotation: Quaternionf?, overrideCameraAngle: Quaternionf?, entity: LivingEntity?) {
        val entityRenderDispatcher = MinecraftClient.getInstance().entityRenderDispatcher
        val entityRenderer: EntityRenderer<in LivingEntity, *> = entityRenderDispatcher.getRenderer(entity)
        val entityRenderState = entityRenderer.getAndUpdateRenderState(entity, 1.0f)
        entityRenderState.hitbox = null
        drawer.addEntity(entityRenderState as EntityRenderState, scale, translation, rotation, overrideCameraAngle, x1, y1, x2, y2)
    }

    /**
     * Draws an item stack at the specified position.
     *
     * @param itemStack The item stack to render.
     */
    fun drawItemStack(itemStack: ItemStack, x: Int, y: Int) {
        drawContext.drawItem(itemStack, x, y)
    }

    /**
     * Draws an item stack at the specified position with a custom size.
     *
     * @param itemStack The item stack to render.
     * @param size The size to render the item at.
     */
    fun drawItemStack(itemStack: ItemStack, x: Int, y: Int, size: Int) {
        push()
        val scale = size / 16.0f
        scale(scale, scale)
        drawContext.drawItem(itemStack, (x / scale).toInt(), (y / scale).toInt())
        pop()
    }

    /**
     * Draws an item stack with its overlay (durability bar, stack count, etc.).
     *
     * @param itemStack The item stack to render.
     */
    fun drawItemStackWithOverlay(itemStack: ItemStack, x: Int, y: Int) {
        drawContext.drawItem(itemStack, x, y)
        drawContext.drawStackOverlay(textRenderer, itemStack, x, y)
    }

    /**
     * Draws an item stack with its overlay and custom size.
     *
     * @param itemStack The item stack to render.
     * @param size The size to render the item at.
     */
    fun drawItemStackWithOverlay(itemStack: ItemStack, x: Int, y: Int, size: Int) {
        push()
        val scale = size / 16.0f
        scale(scale, scale)
        val scaledX = (x / scale).toInt()
        val scaledY = (y / scale).toInt()
        drawContext.drawItem(itemStack, scaledX, scaledY)
        drawContext.drawStackOverlay(textRenderer, itemStack, scaledX, scaledY)
        pop()
    }

    /**
     * Checks if the mouse is hovering over a specific area and executes the appropriate action.
     */
    fun hoverSeparate(mouseX: Float, mouseY: Float, x: Int, y: Int, width: Int, height: Int, normal: () -> Unit, hovered: () -> Unit) {
        if (isMouseOver(mouseX, mouseY, x, y, width, height)) {
            hovered()
        } else {
            normal()
        }
    }

    /**
     * Checks if the mouse is hovering over a specific area and executes the appropriate action.
     */
    fun hoverSeparate(mouseX: Float, mouseY: Float, x: Int, y: Int, width: Int, height: Int, normal: (x: Int, y: Int, width: Int, height: Int) -> Unit, hovered: (x: Int, y: Int, width: Int, height: Int) -> Unit) {
        if (isMouseOver(mouseX, mouseY, x, y, width, height)) {
            hovered(x, y, width, height)
        } else {
            normal(x, y, width, height)
        }
    }

    fun isMouseOver(mouseX: Float, mouseY: Float, x: Int, y: Int, width: Int, height: Int): Boolean {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height
    }
}
