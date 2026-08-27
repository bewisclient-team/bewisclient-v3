package net.bewis09.bewisclient.drawable.renderables.popup

import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.util.Bewisclient
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.WidgetLoader
import net.bewis09.renderite.components.Hoverable
import net.bewis09.renderite.logic.FitType
import net.bewis09.renderite.logic.LineType
import net.bewis09.renderite.logic.TextAlign

class AddWidgetPopup : PropedRenderable<AddWidgetPopup>({
    widthProvider = { Bewisclient.screenWidth - 100 }
    heightProvider = { Bewisclient.screenHeight - 100 }
}) {
    init {
        props()
    }

    companion object {
        val addText = Translation("popup.add_widget.title", "Add Widget")
    }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderPopupBackground(screenDrawing, x, y, width, height, 10, 0.15f)
    }

    override fun Init.init() {
        Text {
            text = addText()
            textAlign = TextAlign.CENTER
            verticalAlign = TextAlign.START
            paddingTop = 9
        }
        Div(0) {
            initForEach(WidgetLoader.widgets.filter { !it.enabled }) { widget ->
                addRenderable(WidgetElement(widget))
            }
            gap = 5
            minElementSize = 80
            lineType = LineType.SIZED
            fitType = FitType.SCROLL
            cacheChildren = true
        }(x + 10, y + 24, width - 20, height - 31)
    }

    inner class WidgetElement(val widget: Widget) : Hoverable<WidgetElement>({
        height = 90
    }) {
        init { props() }

        val title = widget.title()
        val description = widget.description()

        override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
            widget.enabled = true
            this@AddWidgetPopup.removeFromCache(0)
            this@AddWidgetPopup.resize()

            return true
        }

        override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            SelectiveScreenDrawer.renderSettingsCategoryBackground(screenDrawing, x, y, width, height, 1f, hoverFactor, mouseX, mouseY)
        }

        override fun Init.init() {
            Text {
                text = title
                wrap = true
                overflowVisible = true
                color = General.getThemeColor()
                textAlign = TextAlign.CENTER
            }(x + 5, y + 16, width - 10, 0)
            Text {
                text = description
                wrap = true
                overflowVisible = true
                color = General.getThemeColor() * 0xAAAAAA alpha 0.65f
                textAlign = TextAlign.CENTER
            }(x + 5, y2 - 38, width - 10, 0)
        }
    }
}