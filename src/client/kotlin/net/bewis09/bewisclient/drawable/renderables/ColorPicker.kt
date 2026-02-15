package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.createIdentifier
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper

class ColorPicker(val get: () -> Color, val set: (hue: Float, sat: Float) -> Unit) : Renderable() {
    companion object {
        val colorPickerCache = mutableMapOf<Int, Identifier>()
    }

    fun getColorPickerImage(size: Int): Identifier {
        colorPickerCache[size]?.let { return it }

        val identifier = createIdentifier("bewisclient", "color_picker_${size}")

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
        screenDrawing.drawBorder(x, y, width, height, OptionsMenuSettings.getThemeColor(alpha = 0.3f))
        get().brightness.let { screenDrawing.pushColor(it, it, it, 1f) }
        screenDrawing.drawTexture(getColorPickerImage((width - 2).coerceAtMost((height - 2))), x + 1, y + 1, width - 2, height - 2)
        screenDrawing.popColor()
    }

    override fun onMouseDrag(mouseX: Double, mouseY: Double, startX: Double, startY: Double, button: Int): Boolean {
        set(
            (MathHelper.clamp((mouseX - x - 1f).toFloat(), 0f, width - 2f) / (width - 2f)), (MathHelper.clamp((mouseY - y - 1f).toFloat(), 0f, (height - 2f)) / (height - 2f))
        )

        return true
    }
}