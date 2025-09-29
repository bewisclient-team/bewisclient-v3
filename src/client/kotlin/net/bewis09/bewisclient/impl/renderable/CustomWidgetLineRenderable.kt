package net.bewis09.bewisclient.impl.renderable

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.impl.widget.CustomWidget
import net.bewis09.bewisclient.logic.color.Color
import net.minecraft.text.Style
import net.minecraft.util.Identifier

class CustomWidgetLineRenderable : Renderable() {
    val addLine = Translation("widget.tiwyla_widget.add_line", "Add Line")

    var lines = computeLines()
    var centered = CustomWidget.centered.get()

    val textDisplay = ScrollPlane(Scrollable.Direction.HORIZONTAL) { x, y, width, _, scroll ->
        lines.mapIndexed { index, input ->
            TextElement({ CustomWidget.computeLine(input.text) }, Color.WHITE, centered = CustomWidget.centered.get(), style = Style.EMPTY)(x + scroll.toInt(), index * 10 + y, width, 10)
        }
    }

    fun computeLines(): MutableList<Input> = CustomWidget.lines.mapIndexed { i, _ ->
        Input(1000) {
            CustomWidget.lines[i] = it
        }
    }.toMutableList()

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        textDisplay(x + width / 2 + 3, y + 7, (width / 2 - 3), lines.size * 10)
        textDisplay.innerSize = (CustomWidget.lines.maxOfOrNull { screenDrawing.getTextWidth(CustomWidget.computeLine(it), Style.EMPTY) }?.toFloat() ?: 0f).coerceAtLeast(((width / 2 - 3).toFloat()))
        if (centered != CustomWidget.centered.get()) {
            centered = CustomWidget.centered.get()
            resize()
        }
        internalHeight = if (CustomWidget.lines.isEmpty()) 30 else CustomWidget.lines.size * 10 + 31
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        if (width < 15) return
        addRenderable(Rectangle { OptionsMenuSettings.themeColor.get().getColor() alpha 0.5f }(x, y + 3, width, 1))
        addRenderable(Rectangle { OptionsMenuSettings.themeColor.get().getColor() alpha 0.5f }(x, y + 27 + lines.size * 10 - if (CustomWidget.lines.isEmpty()) 1 else 0, width, 1))
        lines.forEachIndexed { index, input ->
            addRenderable(ImageButton(Identifier.of("bewisclient", "textures/gui/sprites/remove.png")) {
                CustomWidget.lines.removeAt(index)
                lines = computeLines()
                resize()
            }.setImagePadding(1)(x, index * 10 + y + 7, 9, 9))
            addRenderable(ImageButton(Identifier.of("bewisclient", "textures/gui/sprites/up.png")) {
                if (index > 0) {
                    val temp = CustomWidget.lines[index - 1]
                    CustomWidget.lines[index - 1] = CustomWidget.lines[index]
                    CustomWidget.lines[index] = temp
                    lines = computeLines()
                    resize()
                }
            }.setImagePadding(0)(x + 10, index * 10 + y + 7, 9, 9))
            addRenderable(ImageButton(Identifier.of("bewisclient", "textures/gui/sprites/down.png")) {
                if (index < lines.size - 1) {
                    val temp = CustomWidget.lines[index + 1]
                    CustomWidget.lines[index + 1] = CustomWidget.lines[index]
                    CustomWidget.lines[index] = temp
                    lines = computeLines()
                    resize()
                }
            }.setImagePadding(0)(x + 20, index * 10 + y + 7, 9, 9))
            addRenderable(input.setPosition(x + 31, index * 10 + y + 7).setWidth(width / 2 - 33).setHeight(10))
            input.setText(CustomWidget.lines[index])
        }
        addRenderable(textDisplay)
        addRenderable(Button(addLine()) {
            CustomWidget.lines.add("")
            lines = computeLines()
            resize()
        }(x, y + 9 + lines.size * 10 - if (CustomWidget.lines.isEmpty()) 1 else 0, width - 16, 14))
        addRenderable(ImageButton(Identifier.of("bewisclient", "textures/gui/sprites/help.png")) {
            OptionScreen.currentInstance?.let { it.openPopup(CustomWidgetHelpPopup(it), Color.BLACK alpha 0.9f) }
        }.setImagePadding(2)(x + width - 14, y + 9 + lines.size * 10 - if (CustomWidget.lines.isEmpty()) 1 else 0, 14, 14))
    }
}
