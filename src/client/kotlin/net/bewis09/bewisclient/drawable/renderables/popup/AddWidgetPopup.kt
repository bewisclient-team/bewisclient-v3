package net.bewis09.bewisclient.drawable.renderables.popup

import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.element.Text
import net.bewis09.bewisclient.drawable.renderables.components.logic.TextAlign
import net.bewis09.bewisclient.drawable.renderables.components.structure.Grid
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.util.Bewisclient
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.WidgetLoader
import net.bewis09.renderite.components.Hoverable

class AddWidgetPopup : PropedRenderable<AddWidgetPopup> ({
    widthProvider = { Bewisclient.screenWidth - 100 }
    heightProvider = { Bewisclient.screenHeight - 100 }
}) {
    companion object {
        val addText = Translation("popup.add_widget.title", "Add Widget")
    }

    val text = Text { text = addText(); textAlign = TextAlign.CENTER }

    var grid = Grid {
        init = { WidgetLoader.widgets.filter { !it.enabled }.map { widget -> WidgetElement(widget).updateHeight(90) } }
        gap = 5
        minElementSize = 80
        lineType = Grid.LineType.SIZED
        fitType = Grid.FitType.SCROLL
    }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderPopupBackground(screenDrawing, x, y, width, height, 10, 0.15f)
    }

    override fun init() {
        addRenderable(text(x, y + 7, width, 14))
        addRenderable(grid(x + 10, y + 24, width - 20, height - 31))
    }

    inner class WidgetElement(val widget: Widget) : Hoverable<WidgetElement>({}) {
        val title = widget.title()
        val description = widget.description()

        override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
            widget.enabled = true
            this@AddWidgetPopup.grid = Grid {
                init = { WidgetLoader.widgets.filter { !it.enabled }.map { widget -> WidgetElement(widget).updateHeight(90) } }
                gap = 5
                minElementSize = 80
                lineType = Grid.LineType.SIZED
                fitType = Grid.FitType.SCROLL
            }
            this@AddWidgetPopup.resize()

            return true
        }

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            super.render(screenDrawing, mouseX, mouseY)

            val textHeight = (screenDrawing.wrapText(title.string, width - 10).size - 1) * screenDrawing.getTextHeight()
            val descriptionHeight = (screenDrawing.wrapText(description.string, width - 10).size - 1) * screenDrawing.getTextHeight()

            SelectiveScreenDrawer.renderSettingsCategoryBackground(screenDrawing, x, y, width, height, 1f, hoverFactor, mouseX, mouseY)

            screenDrawing.drawCenteredWrappedText(title, centerX, y + 14 - textHeight / 2, width - 10, General.getThemeColor())
            screenDrawing.drawCenteredWrappedText(description, centerX, y2 - 38 - descriptionHeight / 2, width - 10, General.getThemeColor() * 0xAAAAAA alpha 0.65f)

            renderRenderables(screenDrawing, mouseX, mouseY)
        }
    }
}