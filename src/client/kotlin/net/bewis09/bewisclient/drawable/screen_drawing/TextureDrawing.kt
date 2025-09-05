package net.bewis09.bewisclient.drawable.screen_drawing

import net.minecraft.client.gl.RenderPipelines
import net.minecraft.util.Identifier

interface TextureDrawing : ScreenDrawingInterface {
    fun drawTexture(texture: Identifier, x: Int, y: Int, width: Int, height: Int) {
        drawTexture(texture, x, y, 0f, 0f, width, height, width, height)
    }

    fun drawTexture(
        texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int
    ) {
        drawTexture(
            texture, x, y, u, v, width, height, textureWidth, textureHeight, 0xFFFFFFFF.toInt()
        )
    }

    fun drawTexture(
        texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int, @ArgbColor color: Number
    ) {
        drawContext.drawTexture(
            RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, textureWidth, textureHeight, applyAlpha(color)
        )
    }

    fun drawTexture(
        texture: Identifier, x: Int, y: Int, width: Int, height: Int, @RgbColor color: Number, alpha: Float
    ) {
        drawTexture(
            texture, x, y, 0f, 0f, width, height, width, height, withAlpha(color, alpha)
        )
    }

    fun drawTexture(
        texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int, @RgbColor color: Number, alpha: Float
    ) {
        drawTexture(
            texture, x, y, u, v, width, height, textureWidth, textureHeight, withAlpha(color, alpha)
        )
    }

    fun drawTextureRegion(
        texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int
    ) {
        drawTextureRegion(
            texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, 0xFFFFFFFF.toInt()
        )
    }

    fun drawTextureRegion(
        texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, @ArgbColor color: Number
    ) {
        drawContext.drawTexture(
            RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, applyAlpha(color)
        )
    }

    fun drawTextureRegion(
        texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, @RgbColor color: Number, alpha: Float
    ) {
        drawTextureRegion(
            texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, withAlpha(color, alpha)
        )
    }
}