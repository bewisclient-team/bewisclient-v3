package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.logic.color.Color
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.util.Identifier

interface TextureDrawing : ScreenDrawingInterface {
    fun drawTexture(texture: Identifier, x: Int, y: Int, width: Int, height: Int) {
        drawTexture(texture, x, y, 0f, 0f, width, height, width, height)
    }

    fun drawTexture(texture: Identifier, x: Int, y: Int, width: Int, height: Int, color: Color) {
        drawTexture(texture, x, y, 0f, 0f, width, height, width, height)
    }

    fun drawTexture(
        texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int
    ) {
        drawTexture(
            texture, x, y, u, v, width, height, textureWidth, textureHeight, Color.WHITE
        )
    }

    fun drawTexture(
        texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int, color: Color
    ) {
        drawContext.drawTexture(
            RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, textureWidth, textureHeight, applyAlpha(color)
        )
    }

    fun drawTextureRegion(
        texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int
    ) {
        drawTextureRegion(
            texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, Color.WHITE
        )
    }

    fun drawTextureRegion(
        texture: Identifier, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, color: Color
    ) {
        drawContext.drawTexture(
            RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, applyAlpha(color)
        )
    }
}