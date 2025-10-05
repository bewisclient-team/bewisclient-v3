package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.transform
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.interfaces.Gettable
import net.bewis09.bewisclient.util.color.*
import net.bewis09.bewisclient.util.number.Precision

class Fader(val value: Gettable<Float>, val precision: Precision, val onChange: (new: Float) -> Unit) : Hoverable() {
    init {
        internalWidth = 100
        internalHeight = 14
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        val normalizedValue = precision.normalize(value.get())
        screenDrawing.fillRounded(
            x, y + 5, width, 4, 2, 0xAAAAAA alpha hoverAnimation["hovering"] * 0.15f + 0.15f
        )

        screenDrawing.transform(x + normalizedValue * (width - 8) + 4, y + 2f, 0.1f) {
            screenDrawing.fillRounded(
                -20, 0, 40, 100, 20, (hoverAnimation["hovering"] within (0xCCCCCC.color to 0xFFFFFF.color)) * OptionsMenuSettings.themeColor.get().getColor()
            )
        }
    }

    override fun onMouseDrag(mouseX: Double, mouseY: Double, startX: Double, startY: Double, button: Int): Boolean {
        return onMouseClick(mouseX, mouseY, button)
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val relativeX = mouseX - x - 4
        var newValue = precision.denormalize(
            (relativeX / (width - 8)).coerceIn(0.0, 1.0).toFloat()
        )
        newValue = precision.getNearestStep(newValue)
        newValue = precision.round(newValue)
        if (newValue == value.get()) return true
        onChange(newValue)
        return true
    }
}