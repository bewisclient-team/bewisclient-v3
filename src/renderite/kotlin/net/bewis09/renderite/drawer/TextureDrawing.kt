package net.bewis09.renderite.drawer

import net.bewis09.renderite.logic.Color

interface TextureDrawing<I, T, F> : ScreenDrawingInterface<I, T, F> {
    fun drawTexture(texture: I, x: Int, y: Int, width: Int, height: Int) {
        drawTexture(texture, x, y, 0f, 0f, width, height, width, height)
    }

    fun drawTexture(texture: I, x: Int, y: Int, width: Int, height: Int, color: Color) {
        drawTexture(texture, x, y, 0f, 0f, width, height, width, height, color)
    }

    fun drawTexture(texture: I, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int) {
        drawTexture(texture, x, y, u, v, width, height, textureWidth, textureHeight, Color.WHITE)
    }

    fun drawTexture(texture: I, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, textureWidth: Int, textureHeight: Int, color: Color) {
        drawTextureRegion(texture, x, y, u, v, width, height, textureWidth, textureHeight, textureWidth, textureHeight, color)
    }

    fun drawTextureRegion(texture: I, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int) {
        drawTextureRegion(texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, Color.WHITE)
    }

    fun drawTextureRegion(
        texture: I, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, color: Color
    ) {
        drawTextureIntern(
            texture, x, y, u, v, width, height, regionWidth, regionHeight, textureWidth, textureHeight, applyAlpha(color)
        )
    }

    fun drawTextureIntern(
        texture: I, x: Int, y: Int, u: Float, v: Float, width: Int, height: Int, regionWidth: Int, regionHeight: Int, textureWidth: Int, textureHeight: Int, color: Int
    )
}