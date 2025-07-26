package net.bewis09.bewisclient.drawable

import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.joml.Quaternionf
import org.joml.Vector3f
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt


/**
 * A class representing a screen drawing context in Bewisclient. This class is used to encapsulate
 * the drawing context
 *
 * @param drawContext The DrawContext used for drawing operations. Using it directly is extremely
 * discouraged.
 */
@Suppress("Unused")
class ScreenDrawing(val drawContext: DrawContext, val textRenderer: TextRenderer) {
    private var style: Style = Style.EMPTY

    companion object {
        val roundFillCache = mutableMapOf<Pair<Int, Int>, Identifier>()
        val roundBorderCache = mutableMapOf<Pair<Int, Int>, Identifier>()
    }

    fun getRoundedImage(radius: Int): Identifier {
        val scale = MinecraftClient.getInstance().window.scaleFactor

        val id = roundFillCache[radius to scale]

        if (id != null) {
            return id
        }

        val identifier = Identifier.of("bewisclient", "rounded_${radius}_$scale")

        val image = BufferedImage(radius * scale, radius * scale, BufferedImage.TYPE_INT_ARGB)

        val r = radius * scale

        for (i in 0 until r) {
            val height = sqrt((r * r - i * i).toDouble()).roundToInt()
            for (j in 0 until height) {
                image.setRGB(i, j, 0xFFFFFFFF.toInt())
            }
        }

        for (i in 0 until r) {
            val height = sqrt((r * r - i * i).toDouble()).roundToInt()
            for (j in 0 until height) {
                image.setRGB(j, i, 0xFFFFFFFF.toInt())
            }
        }

        val os = ByteArrayOutputStream()
        ImageIO.write(image, "png", os)
        val fis: InputStream = ByteArrayInputStream(os.toByteArray())

        MinecraftClient.getInstance().textureManager.registerTexture(
            identifier,
            NativeImageBackedTexture({ identifier.toString() }, NativeImage.read(fis))
        )

        roundFillCache[radius to scale] = identifier

        return identifier
    }

    fun getRoundedBorderImage(radius: Int): Identifier {
        val scale = MinecraftClient.getInstance().window.scaleFactor

        val id = roundBorderCache[radius to scale]

        if (id != null) {
            return id
        }

        val identifier = Identifier.of("bewisclient", "rounded_border_${radius}_$scale")

        val image = BufferedImage(radius * scale, radius * scale, BufferedImage.TYPE_INT_ARGB)

        val r = radius * scale

        for (i in 0 until r) {
            val height = sqrt((r * r - i * i).toDouble()).roundToInt()
            val inner = sqrt(0.0.coerceAtLeast(((r - scale).toDouble()).pow(2) - i * i)).roundToInt()
            for (j in inner until height) {
                image.setRGB(i, j, 0xFFFFFFFF.toInt())
            }
        }

        for (i in 0 until r) {
            val height = sqrt((r * r - i * i).toDouble()).roundToInt()
            val inner = sqrt(0.0.coerceAtLeast(((r - scale).toDouble()).pow(2) - i * i)).roundToInt()
            for (j in inner until height) {
                image.setRGB(j, i, 0xFFFFFFFF.toInt())
            }
        }

        val os = ByteArrayOutputStream()
        ImageIO.write(image, "png", os)
        val fis: InputStream = ByteArrayInputStream(os.toByteArray())

        MinecraftClient.getInstance().textureManager.registerTexture(
            identifier,
            NativeImageBackedTexture({ identifier.toString() }, NativeImage.read(fis))
        )

        roundBorderCache[radius to scale] = identifier

        return identifier
    }

    data class Color(val r: Float, val g: Float, val b: Float, val a: Float) {
        operator fun times(other: Color): Color {
            return Color(
                r * other.r,
                g * other.g,
                b * other.b,
                a * other.a
            )
        }

        fun toInt(): Int {
            return ((r * 255).roundToInt() shl 16) or
                   ((g * 255).roundToInt() shl 8) or
                   (b * 255).roundToInt() or
                   ((a * 255).roundToInt() shl 24)
        }
    }

    val colorStack = mutableListOf<Color>()

    fun pushAlpha(alpha: Float) {
        colorStack.add(Color(1f,1f,1f, alpha))
    }

    fun pushColor(r: Float, g: Float, b: Float, a: Float) {
        colorStack.add(Color(r, g, b, a))
    }

    fun popColor(): Color {
        return if (colorStack.isNotEmpty()) {
            colorStack.removeLast()
        } else {
            Color(1f, 1f, 1f, 1f)
        }
    }

    fun getCurrentColorModifier(): Color {
        return colorStack.reduceOrNull { acc, alpha ->
            acc * alpha
        } ?: Color(1f,1f,1f,1f)
    }

    fun applyAlpha(color: Int): Int {
        return (getCurrentColorModifier() * Color(
            (color shr 16 and 0xFF) / 255f,
            (color shr 8 and 0xFF) / 255f,
            (color and 0xFF) / 255f,
            (color shr 24 and 0xFF) / 255f
        )).toInt()
    }

    fun setFont(font: Identifier) {
        style = Style.EMPTY.withFont(font)
    }

    fun defaultFont() {
        style = Style.EMPTY
    }

    /**
     * Fills a rectangle on the screen with the specified color.
     *
     * @param color The color to fill the rectangle with, represented as an ARGB integer.
     */
    fun fill(x: Int, y: Int, width: Int, height: Int, color: Long) {
        fill(x, y, x + width, y + height, color.toInt())
    }

    /**
     * Fills a rectangle on the screen with the specified color.
     *
     * @param color The color to fill the rectangle with, represented as an ARGB integer.
     */
    fun fill(x: Int, y: Int, width: Int, height: Int, color: Int) {
        drawContext.fill(x, y, x + width, y + height, applyAlpha(color))
    }

    /**
     * Fills a rectangle on the screen with the specified color and alpha value.
     *
     * @param color The color to fill the rectangle with, represented as an RGB integer. The alpha
     * value is applied separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun fill(x: Int, y: Int, width: Int, height: Int, color: Int, alpha: Float) {
        fill(x, y, width, height, combineLong(color, alpha))
    }

    /**
     * Fills a rectangle on the screen with the specified RGB color and alpha value.
     *
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun fill(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ) {
        fill(x, y, width, height, combineLong(r, g, b, alpha))
    }

    /**
     * Fills a rectangle on the screen with the specified RGB color and alpha value.
     *
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun fill(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ) {
        fill(
            x,
            y,
            width,
            height,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
    }

    /**
     * Draws a border inside a rectangle on the screen with the specified color.
     *
     * @param color The color to fill the rectangle with, represented as an ARGB integer.
     */
    fun drawBorder(x: Int, y: Int, width: Int, height: Int, color: Long) {
        drawBorder(x, y, width, height, color.toInt())
    }

    /**
     * Draws a border inside a rectangle on the screen with the specified color.
     *
     * @param color The color to fill the rectangle with, represented as an ARGB integer.
     */
    fun drawBorder(x: Int, y: Int, width: Int, height: Int, color: Int) {
        drawContext.drawBorder(x, y, width, height, applyAlpha(color))
    }

    /**
     * Draws a border inside a rectangle on the screen with the specified color and alpha value.
     *
     * @param color The color to fill the rectangle with, represented as an RGB integer. The alpha
     * value is applied separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawBorder(x: Int, y: Int, width: Int, height: Int, color: Int, alpha: Float) {
        drawBorder(x, y, width, height, combineLong(color, alpha))
    }

    /**
     * Draws a border inside a rectangle on the screen with the specified RGB color and alpha value.
     *
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawBorder(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ) {
        drawBorder(x, y, width, height, combineLong(r, g, b, alpha))
    }

    /**
     * Draws a border inside a rectangle on the screen with the specified RGB color and alpha value.
     *
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawBorder(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ) {
        drawBorder(
            x,
            y,
            width,
            height,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
    }

    /**
     * Draws a rectangle and a border with the specified colors on screen.
     *
     * @param color The color to fill the rectangle with, represented as an ARGB integer.
     * @param borderColor The color of the border, represented as an ARGB integer.
     */
    fun fillWithBorder(x: Int, y: Int, width: Int, height: Int, color: Long, borderColor: Long) {
        fill(x, y, width, height, color)
        drawBorder(x, y, width, height, borderColor)
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
     * @param alpha The alpha value for both the rectangle and the border, ranging from 0.0 (fully
     * transparent) to 1.0 (fully opaque).
     */
    fun fillWithBorder(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        color: Int,
        borderColor: Int,
        alpha: Float
    ) {
        fill(x, y, width, height, combineLong(color, alpha))
        drawBorder(x, y, width, height, combineLong(borderColor, alpha))
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
     * @param alpha The alpha value for both the rectangle and the border, ranging from 0.0 (fully
     * transparent) to 1.0 (fully opaque).
     */
    fun fillWithBorder(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        borderR: UByte,
        borderG: UByte,
        borderB: UByte,
        alpha: Float = 1.0f
    ) {
        fill(x, y, width, height, combineLong(r, g, b, alpha))
        drawBorder(x, y, width, height, combineLong(borderR, borderG, borderB, alpha))
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
     * @param alpha The alpha value for both the rectangle and the border, ranging from 0.0 (fully
     * transparent) to 1.0 (fully opaque).
     */
    fun fillWithBorder(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        r: Float,
        g: Float,
        b: Float,
        borderR: Float,
        borderG: Float,
        borderB: Float,
        alpha: Float = 1.0f
    ) {
        fill(
            x,
            y,
            width,
            height,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
        drawBorder(
            x,
            y,
            width,
            height,
            (borderR * 255).toInt().toUByte(),
            (borderG * 255).toInt().toUByte(),
            (borderB * 255).toInt().toUByte(),
            alpha
        )
    }

    /**
     * Draws a rectangle and a border with the specified colors on screen.
     *
     * @param color The color to fill the rectangle with, represented as an RGB integer.
     * @param alpha The alpha value for the rectangle, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     * @param borderColor The color of the border, represented as an RGB integer.
     * @param borderAlpha The alpha value for the border, ranging from 0.0 (fully transparent) to
     * 1.0 (fully opaque).
     */
    fun fillWithBorder(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        color: Int,
        alpha: Float,
        borderColor: Int,
        borderAlpha: Float
    ) {
        fill(x, y, width, height, combineLong(color, alpha))
        drawBorder(x, y, width, height, combineLong(borderColor, borderAlpha))
    }

    /**
     * Draws a rectangle and a border with the specified RGB colors on screen.
     *
     * @param r The red component of the rectangle color, ranging from 0 to 255.
     * @param g The green component of the rectangle color, ranging from 0 to 255.
     * @param b The blue component of the rectangle color, ranging from 0 to 255.
     * @param alpha The alpha value for the rectangle, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     * @param borderR The red component of the border color, ranging from 0 to 255.
     * @param borderG The green component of the border color, ranging from 0 to 255.
     * @param borderB The blue component of the border color, ranging from 0 to 255.
     * @param borderAlpha The alpha value for the border, ranging from 0.0 (fully transparent) to
     * 1.0 (fully opaque).
     */
    fun fillWithBorder(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f,
        borderR: UByte,
        borderG: UByte,
        borderB: UByte,
        borderAlpha: Float = 1.0f
    ) {
        fill(x, y, width, height, combineLong(r, g, b, alpha))
        drawBorder(x, y, width, height, combineLong(borderR, borderG, borderB, borderAlpha))
    }

    /**
     * Draws a rectangle and a border with the specified RGB colors on screen.
     *
     * @param r The red component of the rectangle color, ranging from 0.0 to 1.0.
     * @param g The green component of the rectangle color, ranging from 0.0 to 1.0.
     * @param b The blue component of the rectangle color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the rectangle, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     * @param borderR The red component of the border color, ranging from 0.0 to 1.0.
     * @param borderG The green component of the border color, ranging from 0.0 to 1.0.
     * @param borderB The blue component of the border color, ranging from 0.0 to 1.0.
     * @param borderAlpha The alpha value for the border, ranging from 0.0 (fully transparent) to
     * 1.0 (fully opaque).
     */
    fun fillWithBorder(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f,
        borderR: Float,
        borderG: Float,
        borderB: Float,
        borderAlpha: Float = 1.0f
    ) {
        fill(
            x,
            y,
            width,
            height,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
        drawBorder(
            x,
            y,
            width,
            height,
            (borderR * 255).toInt().toUByte(),
            (borderG * 255).toInt().toUByte(),
            (borderB * 255).toInt().toUByte(),
            borderAlpha
        )
    }

    /**
     * Translates the drawing context by the specified x and y offsets.
     *
     * @param x The x offset to translate the context by.
     * @param y The y offset to translate the context by.
     */
    fun translate(x: Float, y: Float) {
        drawContext.matrices.translate(x, y)
    }

    /**
     * Scales the drawing context by the specified x and y factors.
     *
     * @param x The x factor to scale the context by.
     * @param y The y factor to scale the context by.
     */
    fun scale(x: Float, y: Float) {
        drawContext.matrices.scale(x, y)
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
     * Pushes a new matrix onto the drawing context's matrix stack. This is used to save the current
     * transformation state so that it can be restored later.
     */
    fun push() {
        drawContext.matrices.pushMatrix()
    }

    /**
     * Pops the last matrix from the drawing context's matrix stack. This restores the previous
     * transformation state that was saved by a push operation.
     */
    fun pop() {
        drawContext.matrices.popMatrix()
    }

    /**
     * Moves the drawing context up one layer in the rendering stack. This is used to change the
     * rendering order of elements on the screen.
     */
    fun goUpLayer() {
        drawContext.state.goUpLayer()
    }

    /**
     * Moves the drawing context down one layer in the rendering stack. This is used to change the
     * rendering order of elements on the screen.
     */
    fun goDownLayer() {
        drawContext.state.goDownLayer()
    }

    /**
     * Draws text at the specified position with the given color.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawText(text: String, x: Int, y: Int, color: Long) {
        drawText(text, x, y, color.toInt())
    }

    /**
     * Draws text at the specified position with the given color.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawText(text: String, x: Int, y: Int, color: Int) {
        drawContext.drawText(textRenderer, Text.literal(text).fillStyle(style), x, y, applyAlpha(color), false)
    }

    /**
     * Draws text at the specified position with the given color and alpha value.
     *
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied
     * separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawText(text: String, x: Int, y: Int, color: Int, alpha: Float) {
        drawText(text, x, y, combineInt(color, alpha))
    }

    /**
     * Draws text at the specified position with the given color.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawText(text: Text, x: Int, y: Int, color: Long) {
        drawText(text, x, y, (color).toInt())
    }

    /**
     * Draws text at the specified position with the given color.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawText(text: Text, x: Int, y: Int, color: Int) {
        drawContext.drawText(textRenderer, text.copy().fillStyle(style), x, y, applyAlpha(color), false)
    }

    /**
     * Draws text at the specified position with the given color and alpha value.
     *
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied
     * separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawText(text: Text, x: Int, y: Int, color: Int, alpha: Float) {
        drawText(text, x, y, combineInt(color, alpha))
    }

    /**
     * Draws text at the specified position with the given RGBA color.
     *
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawText(text: String, x: Int, y: Int, r: UByte, g: UByte, b: UByte, alpha: Float = 1.0f) {
        drawText(text, x, y, combineInt(r, g, b, alpha))
    }

    /**
     * Draws text at the specified position with the given RGBA color.
     *
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawText(text: String, x: Int, y: Int, r: Float, g: Float, b: Float, alpha: Float = 1.0f) {
        drawText(
            text,
            x,
            y,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
    }

    /**
     * Draws text at the specified position with the given RGBA color.
     *
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawText(text: Text, x: Int, y: Int, r: UByte, g: UByte, b: UByte, alpha: Float = 1.0f) {
        drawText(text, x, y, combineInt(r, g, b, alpha))
    }

    /**
     * Draws text at the specified position with the given RGBA color.
     *
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawText(text: Text, x: Int, y: Int, r: Float, g: Float, b: Float, alpha: Float = 1.0f) {
        drawText(
            text,
            x,
            y,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
    }

    /**
     * Draws text with a shadow at the specified position.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawTextWithShadow(text: String, x: Int, y: Int, color: Long) {
        drawTextWithShadow(text, x, y, color.toInt())
    }

    /**
     * Draws text with a shadow at the specified position.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawTextWithShadow(text: String, x: Int, y: Int, color: Int) {
        drawTextWithShadow(Text.literal(text), x, y, color)
    }

    /**
     * Draws text with a shadow at the specified position.
     *
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied
     * separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawTextWithShadow(text: String, x: Int, y: Int, color: Int, alpha: Float) {
        drawTextWithShadow(Text.literal(text), x, y, combineInt(color, alpha))
    }

    /**
     * Draws text with a shadow at the specified position.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawTextWithShadow(text: Text, x: Int, y: Int, color: Long) {
        drawTextWithShadow(text, x, y, color.toInt())
    }

    /**
     * Draws text with a shadow at the specified position.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawTextWithShadow(text: Text, x: Int, y: Int, color: Int) {
        drawContext.drawText(textRenderer, text.copy().fillStyle(style), x, y, applyAlpha(color), true)
    }

    /**
     * Draws text with a shadow at the specified position.
     *
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied
     * separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawTextWithShadow(text: Text, x: Int, y: Int, color: Int, alpha: Float) {
        drawTextWithShadow(text, x, y, combineInt(color, alpha))
    }

    /**
     * Draws text with a shadow at the specified position with the given RGBA color.
     *
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawTextWithShadow(
        text: String,
        x: Int,
        y: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ) {
        drawTextWithShadow(Text.literal(text), x, y, combineInt(r, g, b, alpha))
    }

    /**
     * Draws text with a shadow at the specified position with the given RGBA color.
     *
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawTextWithShadow(
        text: String,
        x: Int,
        y: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ) {
        drawTextWithShadow(
            text,
            x,
            y,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
    }

    /**
     * Draws text with a shadow at the specified position with the given RGBA color.
     *
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawTextWithShadow(
        text: Text,
        x: Int,
        y: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ) {
        drawTextWithShadow(text, x, y, combineInt(r, g, b, alpha))
    }

    /**
     * Draws text with a shadow at the specified position with the given RGBA color.
     *
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawTextWithShadow(
        text: Text,
        x: Int,
        y: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ) {
        drawTextWithShadow(
            text,
            x,
            y,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
    }

    /**
     * Draws text centered at the specified x position.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawCenteredText(text: String, centerX: Int, y: Int, color: Long) {
        drawCenteredText(text, centerX, y, color.toInt())
    }

    /**
     * Draws text centered at the specified x position.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawCenteredText(text: String, centerX: Int, y: Int, color: Int) {
        val textWidth = textRenderer.getWidth(Text.literal(text).fillStyle(style))
        drawContext.drawText(textRenderer, Text.literal(text).fillStyle(style), centerX - textWidth / 2, y, applyAlpha(color), false)
    }

    /**
     * Draws text centered at the specified x position.
     *
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied
     * separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawCenteredText(text: String, centerX: Int, y: Int, color: Int, alpha: Float) {
        drawCenteredText(text, centerX, y, combineInt(color, alpha))
    }

    /**
     * Draws text centered at the specified x position with the given RGBA color.
     *
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawCenteredText(
        text: String,
        centerX: Int,
        y: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ) {
        drawCenteredText(text, centerX, y, combineInt(r, g, b, alpha))
    }

    /**
     * Draws text centered at the specified x position with the given RGBA color.
     *
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawCenteredText(
        text: String,
        centerX: Int,
        y: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ) {
        drawCenteredText(
            text,
            centerX,
            y,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
    }

    /**
     * Draws text centered at the specified x position with a shadow.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawCenteredTextWithShadow(text: String, centerX: Int, y: Int, color: Long) {
        drawCenteredTextWithShadow(text, centerX, y, color.toInt())
    }

    /**
     * Draws text centered at the specified x position with a shadow.
     *
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawCenteredTextWithShadow(text: String, centerX: Int, y: Int, color: Int) {
        val textWidth = textRenderer.getWidth(Text.literal(text).fillStyle(style))
        drawContext.drawText(textRenderer, Text.literal(text).fillStyle(style), centerX - textWidth / 2, y, applyAlpha(color), true)
    }

    /**
     * Draws text centered at the specified x position with a shadow.
     *
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied
     * separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawCenteredTextWithShadow(text: String, centerX: Int, y: Int, color: Int, alpha: Float) {
        drawCenteredTextWithShadow(text, centerX, y, combineInt(color, alpha))
    }

    /**
     * Draws text centered at the specified x position with a shadow and the given RGBA color.
     *
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawCenteredTextWithShadow(
        text: String,
        centerX: Int,
        y: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ) {
        drawCenteredTextWithShadow(
            text,
            centerX,
            y,
            combineInt(r, g, b, alpha)
        )
    }

    /**
     * Draws text centered at the specified x position with a shadow and the given RGBA color.
     *
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawCenteredTextWithShadow(
        text: String,
        centerX: Int,
        y: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ) {
        drawCenteredTextWithShadow(
            text,
            centerX,
            y,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
    }

    /**
     * Draws text right-aligned at the specified x position.
     *
     * @param rightX The x coordinate where the text should end (right edge).
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawRightAlignedText(text: String, rightX: Int, y: Int, color: Long) {
        drawRightAlignedText(text, rightX, y, color.toInt())
    }

    /**
     * Draws text right-aligned at the specified x position.
     *
     * @param rightX The x coordinate where the text should end (right edge).
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawRightAlignedText(text: String, rightX: Int, y: Int, color: Int) {
        val textWidth = textRenderer.getWidth(Text.literal(text).fillStyle(style))
        drawContext.drawText(textRenderer, Text.literal(text).fillStyle(style), rightX - textWidth, y, applyAlpha(color), false)
    }

    /**
     * Draws text right-aligned at the specified x position.
     *
     * @param rightX The x coordinate where the text should end (right edge).
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied
     * separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawRightAlignedText(text: String, rightX: Int, y: Int, color: Int, alpha: Float) {
        drawRightAlignedText(text, rightX, y, combineInt(color, alpha))
    }

    /**
     * Draws text right-aligned at the specified x position with the given RGBA color.
     *
     * @param rightX The x coordinate where the text should end (right edge).
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawRightAlignedText(
        text: String,
        rightX: Int,
        y: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ) {
        drawRightAlignedText(text, rightX, y, combineInt(r, g, b, alpha))
    }

    /**
     * Draws text right-aligned at the specified x position with the given RGBA color.
     *
     * @param rightX The x coordinate where the text should end (right edge).
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawRightAlignedText(
        text: String,
        rightX: Int,
        y: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ) {
        drawRightAlignedText(
            text,
            rightX,
            y,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
    }

    /**
     * Draws text right-aligned at the specified x position with a shadow.
     *
     * @param rightX The x coordinate where the text should end (right edge).
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawRightAlignedTextWithShadow(text: String, rightX: Int, y: Int, color: Long) {
        drawRightAlignedTextWithShadow(text, rightX, y, color.toInt())
    }

    /**
     * Draws text right-aligned at the specified x position with a shadow.
     *
     * @param rightX The x coordinate where the text should end (right edge).
     * @param color The color of the text, represented as an ARGB integer.
     */
    fun drawRightAlignedTextWithShadow(text: String, rightX: Int, y: Int, color: Int) {
        val textWidth = textRenderer.getWidth(Text.literal(text).fillStyle(style))
        drawContext.drawText(textRenderer, Text.literal(text).fillStyle(style), rightX - textWidth, y, applyAlpha(color), true)
    }

    /**
     * Draws text right-aligned at the specified x position with a shadow.
     *
     * @param rightX The x coordinate where the text should end (right edge).
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied
     * separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawRightAlignedTextWithShadow(
        text: String,
        rightX: Int,
        y: Int,
        color: Int,
        alpha: Float
    ) {
        drawRightAlignedTextWithShadow(text, rightX, y, combineInt(color, alpha))
    }

    /**
     * Draws text right-aligned at the specified x position with a shadow and the given RGBA color.
     *
     * @param rightX The x coordinate where the text should end (right edge).
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawRightAlignedTextWithShadow(
        text: String,
        rightX: Int,
        y: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ) {
        drawRightAlignedTextWithShadow(
            text,
            rightX,
            y,
            combineInt(r, g, b, alpha)
        )
    }

    /**
     * Draws text right-aligned at the specified x position with a shadow and the given RGBA color.
     *
     * @param rightX The x coordinate where the text should end (right edge).
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     */
    fun drawRightAlignedTextWithShadow(
        text: String,
        rightX: Int,
        y: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ) {
        drawRightAlignedTextWithShadow(
            text,
            rightX,
            y,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
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
                drawContext.drawText(
                    textRenderer,
                    Text.literal(lines[i]).fillStyle(style),
                    x,
                    y + i * lineHeight,
                    applyAlpha(color),
                    false
                )
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
    fun drawWrappedText(text: String, x: Int, y: Int, maxWidth: Int, color: Long): List<String> {
        return drawWrappedText(text, x, y, maxWidth, color.toInt())
    }

    /**
     * Draws wrapped text within the specified width bounds.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied
     * separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawWrappedText(
        text: String,
        x: Int,
        y: Int,
        maxWidth: Int,
        color: Int,
        alpha: Float
    ): List<String> {
        return drawWrappedText(
            text,
            x,
            y,
            maxWidth,
            combineInt(color, alpha)
        )
    }

    /**
     * Draws wrapped text within the specified width bounds with the given RGBA color.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawWrappedText(
        text: String,
        x: Int,
        y: Int,
        maxWidth: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ): List<String> {
        return drawWrappedText(
            text,
            x,
            y,
            maxWidth,
            combineInt(r, g, b, alpha)
        )
    }

    /**
     * Draws wrapped text within the specified width bounds with the given RGBA color.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawWrappedText(
        text: String,
        x: Int,
        y: Int,
        maxWidth: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ): List<String> {
        return drawWrappedText(
            text,
            x,
            y,
            maxWidth,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
    }

    /**
     * Draws wrapped text within the specified width bounds.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param color The color of the text, represented as an ARGB integer.
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawWrappedText(text: Text, x: Int, y: Int, maxWidth: Int, color: Long): List<String> {
        return drawWrappedText(text.string, x, y, maxWidth, color)
    }

    /**
     * Draws wrapped text within the specified width bounds.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied
     * separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawWrappedText(
        text: Text,
        x: Int,
        y: Int,
        maxWidth: Int,
        color: Int,
        alpha: Float
    ): List<String> {
        return drawWrappedText(text.string, x, y, maxWidth, color, alpha)
    }

    /**
     * Draws wrapped text within the specified width bounds with the given RGBA color.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawWrappedText(
        text: Text,
        x: Int,
        y: Int,
        maxWidth: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ): List<String> {
        return drawWrappedText(text.string, x, y, maxWidth, r, g, b, alpha)
    }

    /**
     * Draws wrapped text within the specified width bounds with the given RGBA color.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawWrappedText(
        text: Text,
        x: Int,
        y: Int,
        maxWidth: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ): List<String> {
        return drawWrappedText(text.string, x, y, maxWidth, r, g, b, alpha)
    }

    /**
     * Draws wrapped text within the specified width bounds.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param color The color of the text, represented as an ARGB integer.
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawCenteredWrappedText(text: String, centerX: Int, y: Int, maxWidth: Int, color: Int): List<String> {
        return wrapText(text, maxWidth).let { lines ->
            val lineHeight = textRenderer.fontHeight
            for (i in lines.indices) {
                drawCenteredText(
                    lines[i],
                    centerX,
                    y + i * lineHeight,
                    applyAlpha(color)
                )
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
    fun drawCenteredWrappedText(text: String, centerX: Int, y: Int, maxWidth: Int, color: Long): List<String> {
        return drawCenteredWrappedText(text, centerX, y, maxWidth, color.toInt())
    }

    /**
     * Draws wrapped text within the specified width bounds.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied
     * separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawCenteredWrappedText(
        text: String,
        centerX: Int,
        y: Int,
        maxWidth: Int,
        color: Int,
        alpha: Float
    ): List<String> {
        return drawCenteredWrappedText(
            text,
            centerX,
            y,
            maxWidth,
            combineInt(color, alpha)
        )
    }

    /**
     * Draws wrapped text within the specified width bounds with the given RGBA color.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawCenteredWrappedText(
        text: String,
        centerX: Int,
        y: Int,
        maxWidth: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ): List<String> {
        return drawCenteredWrappedText(
            text,
            centerX,
            y,
            maxWidth,
            combineInt(r, g, b, alpha)
        )
    }

    /**
     * Draws wrapped text within the specified width bounds with the given RGBA color.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawCenteredWrappedText(
        text: String,
        centerX: Int,
        y: Int,
        maxWidth: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ): List<String> {
        return drawCenteredWrappedText(
            text,
            centerX,
            y,
            maxWidth,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
    }

    /**
     * Draws wrapped text within the specified width bounds.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param color The color of the text, represented as an ARGB integer.
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawCenteredWrappedText(text: Text, centerX: Int, y: Int, maxWidth: Int, color: Long): List<String> {
        return drawCenteredWrappedText(text.string, centerX, y, maxWidth, color)
    }

    /**
     * Draws wrapped text within the specified width bounds.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param color The color of the text, represented as an RGB integer. The alpha value is applied
     * separately.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawCenteredWrappedText(
        text: Text,
        centerX: Int,
        y: Int,
        maxWidth: Int,
        color: Int,
        alpha: Float
    ): List<String> {
        return drawCenteredWrappedText(text.string, centerX, y, maxWidth, color, alpha)
    }

    /**
     * Draws wrapped text within the specified width bounds with the given RGBA color.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawCenteredWrappedText(
        text: Text,
        centerX: Int,
        y: Int,
        maxWidth: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ): List<String> {
        return drawCenteredWrappedText(text.string, centerX, y, maxWidth, r, g, b, alpha)
    }

    /**
     * Draws centered wrapped text within the specified width bounds with the given RGBA color.
     *
     * @param maxWidth The maximum width for text wrapping.
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 (fully transparent) to 1.0
     * (fully opaque).
     * @return A list of strings representing the drawn lines of wrapped text.
     */
    fun drawCenteredWrappedText(
        text: Text,
        centerX: Int,
        y: Int,
        maxWidth: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ): List<String> {
        return drawCenteredWrappedText(text.string, centerX, y, maxWidth, r, g, b, alpha)
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

                    while (textRenderer.getWidth(Text.literal(currentLine).fillStyle(style)) > maxWidth &&
                        currentLine.isNotEmpty()
                    ) {
                        var cutIndex = currentLine.length - 1

                        while (cutIndex > 0 &&
                            textRenderer.getWidth(Text.literal(currentLine.substring(0, cutIndex)).fillStyle(style)) >
                            maxWidth
                        ) {
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
        drawTexture(texture, x, y, 0f, 0f, width, height, width, height)
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
    fun drawTexture(
        texture: Identifier,
        x: Int,
        y: Int,
        u: Float,
        v: Float,
        width: Int,
        height: Int,
        textureWidth: Int,
        textureHeight: Int
    ) {
        drawTexture(
            texture,
            x,
            y,
            u,
            v,
            width,
            height,
            textureWidth,
            textureHeight,
            0xFFFFFFFF.toInt()
        )
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
    fun drawTexture(
        texture: Identifier,
        x: Int,
        y: Int,
        u: Float,
        v: Float,
        width: Int,
        height: Int,
        textureWidth: Int,
        textureHeight: Int,
        color: Long
    ) {
        drawTexture(
            texture,
            x,
            y,
            u,
            v,
            width,
            height,
            textureWidth,
            textureHeight,
            color.toInt()
        )
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
    fun drawTexture(
        texture: Identifier,
        x: Int,
        y: Int,
        u: Float,
        v: Float,
        width: Int,
        height: Int,
        textureWidth: Int,
        textureHeight: Int,
        color: Int
    ) {
        drawContext.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            texture,
            x,
            y,
            u,
            v,
            width,
            height,
            textureWidth,
            textureHeight,
            applyAlpha(color)
        )
    }

    /**
     * Draws a texture at the specified position with basic dimensions and color tint.
     *
     * @param texture The texture identifier to draw.
     * @param color The color tint to apply, represented as an RGB integer.
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully
     * opaque).
     */
    fun drawTexture(
        texture: Identifier,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        color: Int,
        alpha: Float
    ) {
        drawTexture(
            texture,
            x,
            y,
            0f,
            0f,
            width,
            height,
            width,
            height,
            combineInt(color, alpha)
        )
    }

    /**
     * Draws a texture at the specified position with basic dimensions and RGB color tint.
     *
     * @param texture The texture identifier to draw.
     * @param r The red component of the tint, ranging from 0 to 255.
     * @param g The green component of the tint, ranging from 0 to 255.
     * @param b The blue component of the tint, ranging from 0 to 255.
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully
     * opaque).
     */
    fun drawTexture(
        texture: Identifier,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ) {
        drawTexture(
            texture,
            x,
            y,
            0.0f,
            0.0f,
            width,
            height,
            width,
            height,
            combineInt(r, g, b, alpha)
        )
    }

    /**
     * Draws a texture at the specified position with basic dimensions and RGB color tint.
     *
     * @param texture The texture identifier to draw.
     * @param r The red component of the tint, ranging from 0.0 to 1.0.
     * @param g The green component of the tint, ranging from 0.0 to 1.0.
     * @param b The blue component of the tint, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully
     * opaque).
     */
    fun drawTexture(
        texture: Identifier,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ) {
        drawTexture(
            texture,
            x,
            y,
            width,
            height,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
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
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully
     * opaque).
     */
    fun drawTexture(
        texture: Identifier,
        x: Int,
        y: Int,
        u: Float,
        v: Float,
        width: Int,
        height: Int,
        textureWidth: Int,
        textureHeight: Int,
        color: Int,
        alpha: Float
    ) {
        drawTexture(
            texture,
            x,
            y,
            u,
            v,
            width,
            height,
            textureWidth,
            textureHeight,
            combineInt(color, alpha)
        )
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
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully
     * opaque).
     */
    fun drawTexture(
        texture: Identifier,
        x: Int,
        y: Int,
        u: Float,
        v: Float,
        width: Int,
        height: Int,
        textureWidth: Int,
        textureHeight: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ) {
        drawTexture(
            texture,
            x,
            y,
            u,
            v,
            width,
            height,
            textureWidth,
            textureHeight,
            combineInt(r, g, b, alpha)
        )
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
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully
     * opaque).
     */
    fun drawTexture(
        texture: Identifier,
        x: Int,
        y: Int,
        u: Float,
        v: Float,
        width: Int,
        height: Int,
        textureWidth: Int,
        textureHeight: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ) {
        drawTexture(
            texture,
            x,
            y,
            u,
            v,
            width,
            height,
            textureWidth,
            textureHeight,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
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
    fun drawTextureRegion(
        texture: Identifier,
        x: Int,
        y: Int,
        u: Float,
        v: Float,
        width: Int,
        height: Int,
        regionWidth: Int,
        regionHeight: Int,
        textureWidth: Int,
        textureHeight: Int
    ) {
        drawTextureRegion(
            texture,
            x,
            y,
            u,
            v,
            width,
            height,
            regionWidth,
            regionHeight,
            textureWidth,
            textureHeight,
            0xFFFFFFFF.toInt()
        )
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
    fun drawTextureRegion(
        texture: Identifier,
        x: Int,
        y: Int,
        u: Float,
        v: Float,
        width: Int,
        height: Int,
        regionWidth: Int,
        regionHeight: Int,
        textureWidth: Int,
        textureHeight: Int,
        color: Long
    ) {
        drawTextureRegion(
            texture,
            x,
            y,
            u,
            v,
            width,
            height,
            regionWidth,
            regionHeight,
            textureWidth,
            textureHeight,
            color.toInt()
        )
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
    fun drawTextureRegion(
        texture: Identifier,
        x: Int,
        y: Int,
        u: Float,
        v: Float,
        width: Int,
        height: Int,
        regionWidth: Int,
        regionHeight: Int,
        textureWidth: Int,
        textureHeight: Int,
        color: Int
    ) {
        drawContext.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            texture,
            x,
            y,
            u,
            v,
            width,
            height,
            regionWidth,
            regionHeight,
            textureWidth,
            textureHeight,
            applyAlpha(color)
        )
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
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully
     * opaque).
     */
    fun drawTextureRegion(
        texture: Identifier,
        x: Int,
        y: Int,
        u: Float,
        v: Float,
        width: Int,
        height: Int,
        regionWidth: Int,
        regionHeight: Int,
        textureWidth: Int,
        textureHeight: Int,
        color: Int,
        alpha: Float
    ) {
        drawTextureRegion(
            texture,
            x,
            y,
            u,
            v,
            width,
            height,
            regionWidth,
            regionHeight,
            textureWidth,
            textureHeight,
            combineInt(color, alpha)
        )
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
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully
     * opaque).
     */
    fun drawTextureRegion(
        texture: Identifier,
        x: Int,
        y: Int,
        u: Float,
        v: Float,
        width: Int,
        height: Int,
        regionWidth: Int,
        regionHeight: Int,
        textureWidth: Int,
        textureHeight: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ) {
        drawTextureRegion(
            texture,
            x,
            y,
            u,
            v,
            width,
            height,
            regionWidth,
            regionHeight,
            textureWidth,
            textureHeight,
            combineInt(r, g, b, alpha)
        )
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
     * @param alpha The alpha value for the tint, ranging from 0.0 (fully transparent) to 1.0 (fully
     * opaque).
     */
    fun drawTextureRegion(
        texture: Identifier,
        x: Int,
        y: Int,
        u: Float,
        v: Float,
        width: Int,
        height: Int,
        regionWidth: Int,
        regionHeight: Int,
        textureWidth: Int,
        textureHeight: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ) {
        drawTextureRegion(
            texture,
            x,
            y,
            u,
            v,
            width,
            height,
            regionWidth,
            regionHeight,
            textureWidth,
            textureHeight,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
    }

    /**
     * Draws a horizontal line.
     *
     * @param color The color of the line, represented as an ARGB integer.
     */
    fun drawHorizontalLine(startX: Int, y: Int, width: Int, color: Long) {
        fill(startX, y, width, 1, color)
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
    fun drawHorizontalLine(
        startX: Int,
        y: Int,
        width: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ) {
        fill(startX, y, width, 1, r, g, b, alpha)
    }

    /**
     * Draws a vertical line.
     *
     * @param color The color of the line, represented as an ARGB integer.
     */
    fun drawVerticalLine(x: Int, startY: Int, height: Int, color: Long) {
        fill(x, startY, 1, height, color)
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
    fun drawVerticalLine(
        x: Int,
        startY: Int,
        height: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ) {
        fill(x, startY, 1, height, r, g, b, alpha)
    }

    // Gradient drawing methods

    /**
     * Draws a horizontal gradient from left to right.
     *
     * @param startColor The starting color on the left, represented as an ARGB integer.
     * @param endColor The ending color on the right, represented as an ARGB integer.
     */
    fun drawHorizontalGradient(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        startColor: Long,
        endColor: Long
    ) {
        drawHorizontalGradient(
            x,
            y,
            width,
            height,
            startColor.toInt(),
            endColor.toInt()
        )
    }

    /**
     * Draws a horizontal gradient from left to right.
     *
     * @param startColor The starting color on the left, represented as an ARGB integer.
     * @param endColor The ending color on the right, represented as an ARGB integer.
     */
    fun drawHorizontalGradient(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        startColor: Int,
        endColor: Int
    ) {
        drawContext.fillGradient(x, y, x + width, y + height, applyAlpha(startColor), applyAlpha(endColor))
    }

    /**
     * Draws a horizontal gradient from left to right with alpha.
     *
     * @param startColor The starting color on the left, represented as an RGB integer.
     * @param endColor The ending color on the right, represented as an RGB integer.
     * @param alpha The alpha value for both colors, ranging from 0.0 to 1.0.
     */
    fun drawHorizontalGradient(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        startColor: Int,
        endColor: Int,
        alpha: Float
    ) {
        drawHorizontalGradient(
            x,
            y,
            width,
            height,
            combineInt(startColor, alpha),
            combineInt(endColor, alpha)
        )
    }

    /**
     * Draws a vertical gradient from top to bottom.
     *
     * @param startColor The starting color at the top, represented as an ARGB integer.
     * @param endColor The ending color at the bottom, represented as an ARGB integer.
     */
    fun drawVerticalGradient(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        startColor: Long,
        endColor: Long
    ) {
        drawVerticalGradient(
            x,
            y,
            width,
            height,
            startColor.toInt(),
            endColor.toInt()
        )
    }

    /**
     * Draws a vertical gradient from top to bottom.
     *
     * @param startColor The starting color at the top, represented as an ARGB integer.
     * @param endColor The ending color at the bottom, represented as an ARGB integer.
     */
    fun drawVerticalGradient(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        startColor: Int,
        endColor: Int
    ) {
        push()
        translate((x - width).toFloat(), -y.toFloat())
        rotate(-90f)
        drawHorizontalGradient(0,0, height, width, startColor, endColor)
        pop()
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
    fun drawVerticalGradient(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        startColor: Int,
        endColor: Int,
        alpha: Float
    ) {
        drawVerticalGradient(
            x,
            y,
            width,
            height,
            combineInt(startColor, alpha),
            combineInt(endColor, alpha)
        )
    }

    /**
     * Fills a rectangle with rounded corners.
     *
     * @param radius The radius of the rounded corners in pixels.
     * @param color The color to fill the rectangle with, represented as an ARGB integer.
     */
    fun fillRounded(x: Int, y: Int, width: Int, height: Int, radius: Int, color: Long) {
        fillRounded(x, y, width, height, radius, color.toInt())
    }

    /**
     * Fills a rectangle with rounded corners.
     *
     * @param radius The radius of the rounded corners in pixels.
     * @param color The color to fill the rectangle with, represented as an ARGB integer.
     */
    fun fillRounded(x: Int, y: Int, width: Int, height: Int, radius: Int, color: Int) {
        val adjustedRadius = kotlin.math.min(radius, kotlin.math.min(width / 2, height / 2))

        // Fill the main rectangle (without corners)
        fill(x + adjustedRadius, y, width - 2 * adjustedRadius, radius, color)
        fill(x, y + adjustedRadius, width, height - 2 * adjustedRadius, color)
        fill(x + adjustedRadius, y + height - radius, width - 2 * adjustedRadius, radius, color)

        // Draw rounded corners using circles
        drawRoundedCorner(x + adjustedRadius, y + adjustedRadius, adjustedRadius, color, 180f) // Top-left
        drawRoundedCorner(x + width - adjustedRadius, y + adjustedRadius, adjustedRadius, color, 270f) // Top-right
        drawRoundedCorner(x + adjustedRadius, y + height - adjustedRadius, adjustedRadius, color, 90f) // Bottom-left
        drawRoundedCorner(x + width - adjustedRadius, y + height - adjustedRadius, adjustedRadius, color, 0f) // Bottom-right
    }

    /**
     * Fills a rectangle with rounded corners and RGB color with alpha.
     *
     * @param radius The radius of the rounded corners in pixels.
     * @param color The color to fill the rectangle with, represented as an RGB integer.
     * @param alpha The alpha value for the color, ranging from 0.0 to 1.0.
     */
    fun fillRounded(x: Int, y: Int, width: Int, height: Int, radius: Int, color: Int, alpha: Float) {
        fillRounded(x, y, width, height, radius, combineInt(color, alpha))
    }

    /**
     * Fills a rectangle with rounded corners and RGB color components.
     *
     * @param radius The radius of the rounded corners in pixels.
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 to 1.0.
     */
    fun fillRounded(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        radius: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ) {
        fillRounded(x, y, width, height, radius, combineInt(r, g, b, alpha))
    }

    /**
     * Fills a rectangle with rounded corners and RGB color components (float values).
     *
     * @param radius The radius of the rounded corners in pixels.
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 to 1.0.
     */
    fun fillRounded(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        radius: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ) {
        fillRounded(
            x,
            y,
            width,
            height,
            radius,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
    }

    /**
     * Draws a border with rounded corners.
     *
     * @param radius The radius of the rounded corners in pixels.
     * @param color The color of the border, represented as an ARGB integer.
     */
    fun drawBorderRounded(x: Int, y: Int, width: Int, height: Int, radius: Int, color: Long) {
        drawBorderRounded(x, y, width, height, radius, color.toInt())
    }

    /**
     * Draws a border with rounded corners.
     *
     * @param radius The radius of the rounded corners in pixels.
     * @param color The color of the border, represented as an ARGB integer.
     */
    fun drawBorderRounded(x: Int, y: Int, width: Int, height: Int, radius: Int, color: Int) {
        val adjustedRadius = kotlin.math.min(radius, kotlin.math.min(width / 2, height / 2))

        // Draw the border lines (without corners)
        drawHorizontalLine(x + adjustedRadius, y, width - 2 * adjustedRadius, color) // Top
        drawHorizontalLine(x + adjustedRadius, y + height - 1, width - 2 * adjustedRadius, color) // Bottom
        drawVerticalLine(x, y + adjustedRadius, height - 2 * adjustedRadius, color) // Left
        drawVerticalLine(x + width - 1, y + adjustedRadius, height - 2 * adjustedRadius, color) // Right

        // Draw rounded corner borders
        drawRoundedCornerBorder(x + adjustedRadius, y + adjustedRadius, adjustedRadius, color, 180f) // Top-left
        drawRoundedCornerBorder(x + width - adjustedRadius, y + adjustedRadius, adjustedRadius, color, 270f) // Top-right
        drawRoundedCornerBorder(x + adjustedRadius, y + height - adjustedRadius, adjustedRadius, color, 90f) // Bottom-left
        drawRoundedCornerBorder(x + width - adjustedRadius, y + height - adjustedRadius, adjustedRadius, color, 0f) // Bottom-right
    }

    /**
     * Draws a border with rounded corners and RGB color with alpha.
     *
     * @param radius The radius of the rounded corners in pixels.
     * @param color The color of the border, represented as an RGB integer.
     * @param alpha The alpha value for the color, ranging from 0.0 to 1.0.
     */
    fun drawBorderRounded(x: Int, y: Int, width: Int, height: Int, radius: Int, color: Int, alpha: Float) {
        drawBorderRounded(x, y, width, height, radius, combineInt(color, alpha))
    }

    /**
     * Draws a border with rounded corners and RGB color components.
     *
     * @param radius The radius of the rounded corners in pixels.
     * @param r The red component of the color, ranging from 0 to 255.
     * @param g The green component of the color, ranging from 0 to 255.
     * @param b The blue component of the color, ranging from 0 to 255.
     * @param alpha The alpha value for the color, ranging from 0.0 to 1.0.
     */
    fun drawBorderRounded(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        radius: Int,
        r: UByte,
        g: UByte,
        b: UByte,
        alpha: Float = 1.0f
    ) {
        drawBorderRounded(x, y, width, height, radius, combineInt(r, g, b, alpha))
    }

    /**
     * Draws a border with rounded corners and RGB color components (float values).
     *
     * @param radius The radius of the rounded corners in pixels.
     * @param r The red component of the color, ranging from 0.0 to 1.0.
     * @param g The green component of the color, ranging from 0.0 to 1.0.
     * @param b The blue component of the color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for the color, ranging from 0.0 to 1.0.
     */
    fun drawBorderRounded(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        radius: Int,
        r: Float,
        g: Float,
        b: Float,
        alpha: Float = 1.0f
    ) {
        drawBorderRounded(
            x,
            y,
            width,
            height,
            radius,
            (r * 255).toInt().toUByte(),
            (g * 255).toInt().toUByte(),
            (b * 255).toInt().toUByte(),
            alpha
        )
    }

    /**
     * Draws a rectangle with rounded corners and a border.
     *
     * @param radius The radius of the rounded corners in pixels.
     * @param fillColor The color to fill the rectangle with, represented as an ARGB integer.
     * @param borderColor The color of the border, represented as an ARGB integer.
     */
    fun fillWithBorderRounded(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        radius: Int,
        fillColor: Long,
        borderColor: Long
    ) {
        fillRounded(x, y, width, height, radius, fillColor)
        drawBorderRounded(x, y, width, height, radius, borderColor)
    }

    /**
     * Draws a rectangle with rounded corners and a border.
     *
     * @param radius The radius of the rounded corners in pixels.
     * @param fillColor The color to fill the rectangle with, represented as an ARGB integer.
     * @param borderColor The color of the border, represented as an ARGB integer.
     */
    fun fillWithBorderRounded(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        radius: Int,
        fillColor: Int,
        borderColor: Int
    ) {
        fillRounded(x, y, width, height, radius, fillColor)
        drawBorderRounded(x, y, width, height, radius, borderColor)
    }

    /**
     * Draws a rectangle with rounded corners and a border with RGB colors and alpha.
     *
     * @param radius The radius of the rounded corners in pixels.
     * @param fillColor The color to fill the rectangle with, represented as an RGB integer.
     * @param borderColor The color of the border, represented as an RGB integer.
     * @param alpha The alpha value for both colors, ranging from 0.0 to 1.0.
     */
    fun fillWithBorderRounded(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        radius: Int,
        fillColor: Int,
        borderColor: Int,
        alpha: Float
    ) {
        fillRounded(x, y, width, height, radius, fillColor, alpha)
        drawBorderRounded(x, y, width, height, radius, borderColor, alpha)
    }

    /**
     * Draws a rectangle with rounded corners and a border with separate alpha values.
     *
     * @param radius The radius of the rounded corners in pixels.
     * @param fillColor The color to fill the rectangle with, represented as an RGB integer.
     * @param fillAlpha The alpha value for the fill color, ranging from 0.0 to 1.0.
     * @param borderColor The color of the border, represented as an RGB integer.
     * @param borderAlpha The alpha value for the border color, ranging from 0.0 to 1.0.
     */
    fun fillWithBorderRounded(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        radius: Int,
        fillColor: Int,
        fillAlpha: Float,
        borderColor: Int,
        borderAlpha: Float
    ) {
        fillRounded(x, y, width, height, radius, fillColor, fillAlpha)
        drawBorderRounded(x, y, width, height, radius, borderColor, borderAlpha)
    }

    /**
     * Draws a rectangle with rounded corners and a border with RGB color components.
     *
     * @param radius The radius of the rounded corners in pixels.
     * @param fillR The red component of the fill color, ranging from 0 to 255.
     * @param fillG The green component of the fill color, ranging from 0 to 255.
     * @param fillB The blue component of the fill color, ranging from 0 to 255.
     * @param borderR The red component of the border color, ranging from 0 to 255.
     * @param borderG The green component of the border color, ranging from 0 to 255.
     * @param borderB The blue component of the border color, ranging from 0 to 255.
     * @param alpha The alpha value for both colors, ranging from 0.0 to 1.0.
     */
    fun fillWithBorderRounded(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        radius: Int,
        fillR: UByte,
        fillG: UByte,
        fillB: UByte,
        borderR: UByte,
        borderG: UByte,
        borderB: UByte,
        alpha: Float = 1.0f
    ) {
        fillRounded(x, y, width, height, radius, fillR, fillG, fillB, alpha)
        drawBorderRounded(x, y, width, height, radius, borderR, borderG, borderB, alpha)
    }

    /**
     * Draws a rectangle with rounded corners and a border with RGB color components (float values).
     *
     * @param radius The radius of the rounded corners in pixels.
     * @param fillR The red component of the fill color, ranging from 0.0 to 1.0.
     * @param fillG The green component of the fill color, ranging from 0.0 to 1.0.
     * @param fillB The blue component of the fill color, ranging from 0.0 to 1.0.
     * @param borderR The red component of the border color, ranging from 0.0 to 1.0.
     * @param borderG The green component of the border color, ranging from 0.0 to 1.0.
     * @param borderB The blue component of the border color, ranging from 0.0 to 1.0.
     * @param alpha The alpha value for both colors, ranging from 0.0 to 1.0.
     */
    fun fillWithBorderRounded(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        radius: Int,
        fillR: Float,
        fillG: Float,
        fillB: Float,
        borderR: Float,
        borderG: Float,
        borderB: Float,
        alpha: Float = 1.0f
    ) {
        fillRounded(x, y, width, height, radius, fillR, fillG, fillB, alpha)
        drawBorderRounded(x, y, width, height, radius, borderR, borderG, borderB, alpha)
    }

    /**
     * Helper function to draw a rounded corner (filled quarter circle).
     *
     * @param centerX The x coordinate of the circle center.
     * @param centerY The y coordinate of the circle center.
     * @param radius The radius of the circle.
     * @param color The color to fill with.
     * @param startAngle The starting angle in degrees.
     */
    private fun drawRoundedCorner(
        centerX: Int,
        centerY: Int,
        radius: Int,
        color: Int,
        startAngle: Float
    ) {
        push()
        translate(centerX.toFloat(), centerY.toFloat())
        rotateDegrees(startAngle)
        scale(1/MinecraftClient.getInstance().window.scaleFactor.toFloat(),
              1/MinecraftClient.getInstance().window.scaleFactor.toFloat())

        val r = radius * (MinecraftClient.getInstance().window.scaleFactor)

        drawTexture(getRoundedImage(radius), 0, 0, 0f, 0f, r, r, r, r, color)
        pop()
    }

    /**
     * Helper function to draw a rounded corner border (quarter circle outline).
     *
     * @param centerX The x coordinate of the circle center.
     * @param centerY The y coordinate of the circle center.
     * @param radius The radius of the circle.
     * @param color The color of the border.
     * @param startAngle The starting angle in degrees.
     */
    private fun drawRoundedCornerBorder(
        centerX: Int,
        centerY: Int,
        radius: Int,
        color: Int,
        startAngle: Float
    ) {
        push()
        translate(centerX.toFloat(), centerY.toFloat())
        rotateDegrees(startAngle)
        scale(1/MinecraftClient.getInstance().window.scaleFactor.toFloat(),
            1/MinecraftClient.getInstance().window.scaleFactor.toFloat())

        val r = radius * (MinecraftClient.getInstance().window.scaleFactor)

        drawTexture(getRoundedBorderImage(radius), 0, 0, 0f, 0f, r, r, r, r, color)
        pop()
    }

    fun drawEntity(
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        size: Int,
        scale: Float,
        mouseX: Float,
        mouseY: Float,
        entity: LivingEntity
    ) {
        val f = (x1 + x2).toFloat() / 2.0f
        val g = (y1 + y2).toFloat() / 2.0f
        drawContext.enableScissor(x1, y1, x2, y2)
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
        InventoryScreen.drawEntity(
            drawContext,
            x1,
            y1,
            x2,
            y2,
            p,
            vector3f,
            quaternion,
            quaternion2,
            entity
        )
        entity.bodyYaw = j
        entity.yaw = k
        entity.pitch = l
        entity.lastHeadYaw = m
        entity.headYaw = n
        drawContext.disableScissor()
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
        drawItemStack(itemStack, (x / scale).toInt(), (y / scale).toInt())
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
        drawItemStackWithOverlay(itemStack, scaledX, scaledY)
        pop()
    }

    /** Checks if the mouse is hovering over a specific area and executes the appropriate action. */
    fun hoverSeparate(
        mouseX: Float,
        mouseY: Float,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        normal: () -> Unit,
        hovered: () -> Unit
    ) {
        if (isMouseOver(mouseX, mouseY, x, y, width, height)) {
            hovered()
        } else {
            normal()
        }
    }

    /** Checks if the mouse is hovering over a specific area and executes the appropriate action. */
    fun hoverSeparate(
        mouseX: Float,
        mouseY: Float,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        normal: (x: Int, y: Int, width: Int, height: Int) -> Unit,
        hovered: (x: Int, y: Int, width: Int, height: Int) -> Unit
    ) {
        if (isMouseOver(mouseX, mouseY, x, y, width, height)) {
            hovered(x, y, width, height)
        } else {
            normal(x, y, width, height)
        }
    }

    fun enableScissors(x: Int, y: Int, width: Int, height: Int) {
        drawContext.enableScissor(x, y, x + width, y + height)
    }

    fun disableScissors() {
        drawContext.disableScissor()
    }
}

fun isMouseOver(
    mouseX: Float,
    mouseY: Float,
    x: Int,
    y: Int,
    width: Int,
    height: Int
): Boolean {
    return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height
}

fun combineInt(rgb: Int, alpha: Float): Int {
    return (rgb.toLong() or ((alpha * 255).toLong() shl 24)).toInt()
}

fun combineLong(rgb: Int, alpha: Float): Long {
    return (rgb.toLong() or ((alpha * 255).toLong() shl 24))
}

fun combineInt(r: UByte, g: UByte, b: UByte, alpha: Float): Int {
    return combineLong(r, g, b, alpha).toInt()
}

fun combineLong(r: UByte, g: UByte, b: UByte, alpha: Float): Long {
    return ((alpha * 255).toLong() shl 24) or
            (r.toLong() shl 16) or
            (g.toLong() shl 8) or
            b.toLong()
}

fun combineInt(r: Float, g: Float, b: Float, alpha: Float): Int {
    return combineLong(r, g, b, alpha).toInt()
}

fun combineLong(r: Float, g: Float, b: Float, alpha: Float): Long {
    return combineLong(
        (r * 255).toInt().toUByte(),
        (g * 255).toInt().toUByte(),
        (b * 255).toInt().toUByte(),
        alpha
    )
}
