package net.bewis09.bewisclient.drawable.screen_drawing

import net.bewis09.bewisclient.core.BewisclientID
import net.bewis09.bewisclient.logic.color.Color

interface TextureDrawing : ScreenDrawingInterface {
    fun drawTexture(texture: BewisclientID, x: Int, y: Int, width: Int, height: Int) {
        drawTexture(texture, x, y, 0f, 0f, width, height, width, height)
    }

    fun drawTexture(texture: BewisclientID, x: Int, y: Int, width: Int, height: Int, color: Color) {
        drawTexture(texture, x, y, 0f, 0f, width, height, width, height)
    }

    fun drawTexture(
        texture: BewisclientID, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int
    ) {
        drawTexture(
            texture, x, y, u, v, width, height, textureWidth, textureHeight, Color.WHITE
        )
    }

    fun drawTexture(
        texture: BewisclientID, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int, color: Color
    ) {
        drawTextureRegion(
            texture, x, y, u, v, width, height, textureWidth, textureHeight, textureWidth, textureHeight, color
        )
    }

    fun drawTextureRegion(
        texture: BewisclientID, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int
    ) {
        drawTextureRegion(
            texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, Color.WHITE
        )
    }

    fun drawTextureRegion(
        texture: BewisclientID, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, color: Color
    ) {
        drawingCore.drawTexture(
            texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, applyAlpha(color)
        )
    }
}