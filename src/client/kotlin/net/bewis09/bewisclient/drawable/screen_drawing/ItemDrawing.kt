package net.bewis09.bewisclient.drawable.screen_drawing

import net.minecraft.item.ItemStack

interface ItemDrawing : ScreenDrawingInterface {
    fun drawItemStack(itemStack: ItemStack, x: Int, y: Int) {
        drawingCore.drawItemStack(itemStack, x, y)
    }

    fun drawItemStack(itemStack: ItemStack, x: Int, y: Int, size: Int) {
        val scale = size / 16.0f
        scale(scale, scale) {
            drawItemStack(itemStack, (x / scale).toInt(), (y / scale).toInt())
        }
    }

    fun drawItemStackWithOverlay(itemStack: ItemStack, x: Int, y: Int) {
        drawItemStack(itemStack, x, y)
        drawingCore.drawStackOverlay(itemStack, x, y)
    }

    fun drawItemStackWithOverlay(itemStack: ItemStack, x: Int, y: Int, size: Int) {
        val scale = size / 16.0f
        scale(scale, scale) {
            val scaledX = (x / scale).toInt()
            val scaledY = (y / scale).toInt()
            drawItemStackWithOverlay(itemStack, scaledX, scaledY)
        }
    }
}