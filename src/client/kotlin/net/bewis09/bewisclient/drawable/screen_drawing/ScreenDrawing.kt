package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.mixin.client.accessor.ScissorStackAccessor
import net.bewis09.bewisclient.util.logic.ClientInterface
import net.bewis09.bewisclient.version.GuiGraphics
import net.bewis09.bewisclient.version.drawItem
import net.bewis09.bewisclient.version.drawItemOverlay
import net.bewis09.bewisclient.version.drawTexture
import net.bewis09.bewisclient.version.pop
import net.bewis09.bewisclient.version.push
import net.bewis09.bewisclient.version.rotate
import net.bewis09.bewisclient.version.scale
import net.bewis09.bewisclient.version.setCursorPointer
import net.bewis09.bewisclient.version.setFont
import net.bewis09.bewisclient.version.string
import net.bewis09.bewisclient.version.translate
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.color
import net.bewis09.renderite.drawer.RenderiteDrawer
import net.bewis09.renderite.drawer.transform
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.ItemStack
import java.awt.image.BufferedImage

class ScreenDrawing(val guiGraphics: GuiGraphics, val font: Font): RenderiteDrawer<Identifier, Component, Identifier>(DEFAULT_FONT), ClientInterface {
    fun setBewisclientFont() = setFont(BEWISCLIENT_FONT)
    fun setDefaultFont() = setFont(DEFAULT_FONT)

    fun copy() = ScreenDrawing(guiGraphics, font)

    override val imageScaleFactor: Int
        get() = scaleFactor * 3

    companion object {
        val DEFAULT_FONT = createIdentifier("minecraft", "default")
        val BEWISCLIENT_FONT = createIdentifier("bewisclient", "screen")

        val roundFillCache = mutableMapOf<Pair<Int, Int>, Identifier>()
        val roundBorderCache = mutableMapOf<Pair<Int, Int>, Identifier>()
    }

    fun drawItemStack(itemStack: ItemStack, x: Int, y: Int) {
        guiGraphics.drawItem(itemStack, x, y)
    }

    fun drawItemStackWithOverlay(itemStack: ItemStack, x: Int, y: Int) {
        drawItemStack(itemStack, x, y)
        guiGraphics.drawItemOverlay(font, itemStack, x, y)
    }

    override fun String.convert(): MutableComponent = this.toText()

    override fun Component.convert(): String = this.string

    override fun drawTextIntern(text: Component, color: Color, font: Identifier?, shadow: Boolean) {
        if ((font == BEWISCLIENT_FONT || (font == null && this.overwrittenFont == BEWISCLIENT_FONT)) && General.isMinecrafty) {
            val color = applyAlpha(color)
            if (color.toLong().color.alpha < 4) return
            transform(0f, getTextHeight() / 2f + 0.7f, 0.85f, 0.85f) {
                translate(0f, -getTextHeight() / 2f)
                guiGraphics.string(this.font, text.copy().setFont(DEFAULT_FONT), 0, 0, color, shadow)
            }
        } else {
            val color = applyAlpha(color)
            if (color.toLong().color.alpha < 4) return
            guiGraphics.string(this.font, text.copy().setFont(font), 0, 0, color, shadow)
        }
    }

    override fun getTextWidth(text: Component, font: Identifier?): Int {
        if ((font == BEWISCLIENT_FONT || (font == null && this.overwrittenFont == BEWISCLIENT_FONT)) && General.isMinecrafty) {
            return this.font.width(text.setFont(DEFAULT_FONT)) * 85 / 100
        }
        return this.font.width(text.setFont(font))
    }

    override fun getTextHeight(): Int = this.font.lineHeight

    override val fillCache: MutableMap<Pair<Int, Int>, Identifier>
        get() = roundFillCache
    override val borderCache: MutableMap<Pair<Int, Int>, Identifier>
        get() = roundBorderCache

    override fun createBorderImage(name: String): Identifier = createIdentifier("bewisclient", name)

    override fun createTexture(i: Identifier, w: Int, h: Int, func: ((Int, Int, Int) -> Unit) -> Unit) {
        createTexture(i, w, h) { image: BufferedImage -> func(image::setRGB) }
    }

    override fun fillIntern(x: Int, y: Int, width: Int, height: Int, color: Int) {
        guiGraphics.fill(x, y, x + width, y + height, color)
    }

    override fun drawHorizontalGradientIntern(x: Int, y: Int, width: Int, height: Int, startColor: Int, endColor: Int) {
        guiGraphics.fillGradient(x, y, x + width, y + height, startColor, endColor)
    }

    override fun translate(x: Float, y: Float) = guiGraphics.translate(x, y)

    override fun scale(x: Float, y: Float) = guiGraphics.scale(x, y)

    override fun rotate(angle: Float) = guiGraphics.rotate(angle)

    override fun push() = guiGraphics.push()

    override fun pop() = guiGraphics.pop()

    override fun enableScissors(x: Int, y: Int, width: Int, height: Int) = guiGraphics.enableScissor(x, y, x + width, y + height)

    override fun disableScissors() = guiGraphics.disableScissor()

    override fun scissorContains(x: Int, y: Int) = (guiGraphics.scissorStack as? ScissorStackAccessor)?.getStack()?.peekLast()?.containsPoint(x, y) != false

    override fun setCursorPointer() = guiGraphics.setCursorPointer()

    override fun drawTextureIntern(texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, color: Int) {
        guiGraphics.drawTexture(texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color)
    }

    override fun wrapText(text: Component, maxWidth: Int, font: Identifier?): List<Component> {
        val texts = text.copy().setFont(font).toFlatList().flatMap {
            it.convert().replace("\r", "").replace("\n", "\uFDD0\n\uFDD0").replace(" ", "\uFDD0 \uFDD0").split("\uFDD0").map { str ->
                str.convert().setStyle(it.style)
            }
        }

        val lines = arrayListOf(Component.empty())

        for (i in texts.indices) {
            val l = texts[i]

            if (l.string == "\n") {
                lines.add(Component.empty())
            } else if (l.string == " ") {
                if (i == texts.indices.last) {
                    lines.last().append(l)
                } else {
                    val added = lines.last().copy().append(l).append(texts[i + 1])

                    if (getTextWidth(added, font) > maxWidth) {
                        lines.add(Component.empty())
                    } else {
                        lines.last().append(l)
                    }
                }
            } else {
                lines.last().append(l)
            }
        }

        return lines
    }
}