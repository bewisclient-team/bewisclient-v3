package net.bewis09.bewisclient.core

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.joml.*

class DrawingCore(internal val drawContext: DrawContext, internal val textRenderer: TextRenderer) {
    fun drawText(text: Text, x: Int, y: Int, color: Int, shadow: Boolean) {
        drawContext.drawText(textRenderer, text, x, y, color, shadow)
    }

    fun getFontHeight(): Int = textRenderer.fontHeight

    fun getTextWidth(text: Text): Int = textRenderer.getWidth(text)

    fun drawTexture(
        texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, color: Int
    ) = drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, color)

    fun drawEntity(x1: Int, y1: Int, x2: Int, y2: Int, scale: Float, translation: Vector3f?, rotation: Quaternionf?, overrideCameraAngle: Quaternionf?, entity: LivingEntity?) = InventoryScreen.drawEntity(
        drawContext, x1, y1, x2, y2, scale, translation, rotation, overrideCameraAngle, entity
    )

    fun drawItemStack(itemStack: ItemStack, x: Int, y: Int) {
        drawContext.drawItem(itemStack, x, y)
    }

    fun drawStackOverlay(itemStack: ItemStack, x: Int, y: Int) {
        drawContext.drawStackOverlay(textRenderer, itemStack, x, y)
    }

    fun fillGradient(x: Int, y: Int, x2: Int, y2: Int, colorStart: Int, colorEnd: Int) {
        drawContext.fillGradient(x, y, x2, y2, colorStart, colorEnd)
    }

    fun fill(x1: Int, y1: Int, x2: Int, y2: Int, color: Int) {
        drawContext.fill(x1, y1, x2, y2, color)
    }

    fun translate(x: Float, y: Float): Matrix3x2f = drawContext.matrices.translate(x, y)

    fun scale(x: Float, y: Float): Matrix3x2f = drawContext.matrices.scale(x, y)

    fun rotate(angle: Float): Matrix3x2f = drawContext.matrices.rotate(angle)

    fun push(): Matrix3x2fStack = drawContext.matrices.pushMatrix()

    fun pop(): Matrix3x2fStack = drawContext.matrices.popMatrix()

    fun enableScissors(x: Int, y: Int, width: Int, height: Int) = drawContext.enableScissor(x, y, width, height)

    fun disableScissors() = drawContext.disableScissor()

    fun scissorContains(x: Int, y: Int): Boolean = drawContext.scissorContains(x, y)
}