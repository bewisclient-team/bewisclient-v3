package net.bewis09.bewisclient.drawable.renderables.components.setting

import net.bewis09.renderite.logic.Color
import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.drawer.pushColor
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.util.Bewisclient

class ColorPicker(p: Props<ColorPicker>) : PropedRenderable<ColorPicker>(p) {
    lateinit var get: () -> Color
    lateinit var set: (hue: Float, sat: Float) -> Unit

    init { props() }

    companion object {
        val colorPickerCache = mutableMapOf<Int, Identifier>()
    }

    fun getColorPickerImage(size: Int): Identifier {
        colorPickerCache[size]?.let { return it }

        val identifier = createIdentifier("bewisclient", "color_picker_${size}")

        Bewisclient.createTexture(identifier, size, size) {
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

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.drawBorder(x, y, width, height, General.getThemeColor(alpha = 0.3f))
    }

    override fun Init.init() {
        Image {
            colorProvider = { Color(get().brightness, get().brightness, get().brightness, 1f) }
            image = getColorPickerImage((width - 2).coerceAtMost((height - 2)))
            padding = 1
        }
    }

    override fun onMouseDrag(mouseX: Double, mouseY: Double, startX: Double, startY: Double, button: Int): Boolean {
        set((mouseX - x - 1f).toFloat().coerceIn(0f, width - 2f) / (width - 2f), (mouseY - y - 1f).toFloat().coerceIn(0f, height - 2f) / (height - 2f))

        return true
    }
}