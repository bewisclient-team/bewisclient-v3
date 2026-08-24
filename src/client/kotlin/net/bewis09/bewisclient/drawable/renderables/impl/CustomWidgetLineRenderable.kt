package net.bewis09.bewisclient.drawable.renderables.impl

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.renderables.components.button.Button
import net.bewis09.bewisclient.drawable.renderables.components.button.ImageButton
import net.bewis09.bewisclient.drawable.renderables.components.element.Rectangle
import net.bewis09.bewisclient.drawable.renderables.components.element.Text
import net.bewis09.bewisclient.drawable.renderables.components.logic.Direction
import net.bewis09.bewisclient.drawable.renderables.components.logic.TextAlign
import net.bewis09.bewisclient.drawable.renderables.components.setting.Input
import net.bewis09.bewisclient.drawable.renderables.components.structure.Grid
import net.bewis09.bewisclient.drawable.renderables.popup.CustomWidgetHelpPopup
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.widget.impl.CustomWidget
import net.bewis09.renderite.logic.Color

class CustomWidgetLineRenderable : PropedRenderable<CustomWidgetLineRenderable>() {
    val addLine = Translation("widget.tiwyla_widget.add_line", "Add Line")

    var lines = computeLines()
    var centered = CustomWidget.centered.get()

    // TODO aktuell macht eine lange zeile das nur in der zeile länger, sollte aber in allen
    val textDisplay = Grid {
        init = {
            this@CustomWidgetLineRenderable.lines.map { input ->
                Text {
                    textProvider = { CustomWidget.computeLine(input.text).toText() }
                    color = Color.WHITE
                    textAlign = if(CustomWidget.centered.get()) TextAlign.CENTER else TextAlign.START
                    font = ScreenDrawing.DEFAULT_FONT
                    minWidth = (this@CustomWidgetLineRenderable.width / 2 - 3)
                }.updateHeight(10)
            }
        }
        lines = this@CustomWidgetLineRenderable.lines.size
        fitType = Grid.FitType.SCROLL
        direction = Direction.HORIZONTAL
    }

    fun computeLines(): MutableList<Input> = MutableList(CustomWidget.lines.size) { i -> Input {
        onChange = { CustomWidget.lines[i] = it }
        maxTextLength = 1000
    } }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        textDisplay(x + width / 2 + 3, y + 7, (width / 2 - 3), lines.size * 10)
        textDisplay.renderables.forEach { renderable ->
            renderable.updateWidth((CustomWidget.lines.maxOfOrNull {
                screenDrawing.getTextWidth(CustomWidget.computeLine(it), ScreenDrawing.DEFAULT_FONT)
            }?.toFloat() ?: 0f).coerceAtLeast(((width / 2 - 3).toFloat())).toInt())
        }
        if (centered != CustomWidget.centered.get()) {
            centered = CustomWidget.centered.get()
            resize()
        }
        height = if (CustomWidget.lines.isEmpty()) 30 else CustomWidget.lines.size * 10 + 31
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        if (width < 15) return
        addRenderable(Rectangle { colorProvider = { General.getThemeColor(alpha = 0.5f) } }(x, y + 3, width, 1))
        addRenderable(Rectangle { colorProvider = { General.getThemeColor(alpha = 0.5f) } }(x, y + 27 + lines.size * 10 - if (CustomWidget.lines.isEmpty()) 1 else 0, width, 1))
        lines.forEachIndexed { index, input ->
            addRenderable(ImageButton {
                image = createIdentifier("bewisclient", "textures/gui/sprites/remove.png")
                small = true
                imagePadding = 1
                onClick = {
                    CustomWidget.lines.removeAt(index)
                    lines = computeLines()
                    resize()
                }
            }(x, index * 10 + y + 7, 9, 9))
            addRenderable(ImageButton {
                image = createIdentifier("bewisclient", "textures/gui/sprites/up.png")
                small = true
                imagePadding = 0
                onClick = {
                    if (index > 0) {
                        val temp = CustomWidget.lines[index - 1]
                        CustomWidget.lines[index - 1] = CustomWidget.lines[index]
                        CustomWidget.lines[index] = temp
                        lines = computeLines()
                        resize()
                    }
                }
            }(x + 10, index * 10 + y + 7, 9, 9))
            addRenderable(ImageButton {
                image = createIdentifier("bewisclient", "textures/gui/sprites/down.png")
                small = true
                imagePadding = 0
                onClick = {
                    if (index < lines.size - 1) {
                        val temp = CustomWidget.lines[index + 1]
                        CustomWidget.lines[index + 1] = CustomWidget.lines[index]
                        CustomWidget.lines[index] = temp
                        lines = computeLines()
                        resize()
                    }
                }
            }(x + 20, index * 10 + y + 7, 9, 9))
            addRenderable(input.updatePosition(x + 31, index * 10 + y + 7).updateWidth(width / 2 - 33).updateHeight(10))
            input.setText(CustomWidget.lines[index])
        }
        addRenderable(textDisplay)
        addRenderable(Button {
            text = addLine()
            onClick = {
                CustomWidget.lines.add("")
                lines = computeLines()
                resize()
            }
        }(x, y + 9 + lines.size * 10 - if (CustomWidget.lines.isEmpty()) 1 else 0, width - 16, 14))
        addRenderable(ImageButton {
            image = createIdentifier("bewisclient", "textures/gui/sprites/help.png")
            onClick = {
                OptionScreen.currentInstance?.openPopup(CustomWidgetHelpPopup(), Color.BLACK alpha 0.9f)
            }
            imagePadding = 2
        }(x + width - 14, y + 9 + lines.size * 10 - if (CustomWidget.lines.isEmpty()) 1 else 0, 14, 14))
    }
}
