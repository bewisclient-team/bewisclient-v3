package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.logic.color.Color
import net.bewis09.bewisclient.logic.color.alpha
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper

class ColorPicker(val get: () -> Color, val set: (hue: Float, sat: Float) -> Unit) : Renderable() {
    companion object {
        val colorPickerCache = mutableMapOf<Int, Identifier>()
    }

    fun getColorPickerImage(size: Int): Identifier {
        colorPickerCache[size]?.let { return it }

        val identifier = Identifier.of("bewisclient", "color_picker_${size}")

        createTexture(identifier, size, size) {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    val color = java.awt.Color.HSBtoRGB(x / size.toFloat(), y / size.toFloat(), 1f)
                    it.setRGB(x, y, color)
                }
            }
        }

        colorPickerCache[size] = identifier

        return identifier
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.drawBorder(getX(), getY(), getWidth(), getHeight(), 0xAAAAAA alpha 0.5f)
        get().brightness.let { screenDrawing.pushColor(it, it, it, 1f) }
        screenDrawing.drawTexture(getColorPickerImage((getWidth() - 2).coerceAtMost((getHeight() - 2))), getX() + 1, getY() + 1, getWidth() - 2, getHeight() - 2)
        screenDrawing.popColor()
    }

    override fun onMouseDrag(mouseX: Double, mouseY: Double, startX: Double, startY: Double, button: Int): Boolean {
        set(
            (MathHelper.clamp((mouseX - getX() - 1f).toFloat(), 0f, getWidth() - 2f) / (getWidth() - 2f)), (MathHelper.clamp((mouseY - getY() - 1f).toFloat(), 0f, (getHeight() - 2f)) / (getHeight() - 2f))
        )

        return true
    }
}