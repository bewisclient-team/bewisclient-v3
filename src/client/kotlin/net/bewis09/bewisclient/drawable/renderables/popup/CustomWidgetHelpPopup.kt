package net.bewis09.bewisclient.drawable.renderables.popup

import net.bewis09.bewisclient.common.setColor
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.SimpleRenderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.settings.InfoTextRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.util.Bewisclient
import net.bewis09.bewisclient.widget.impl.CustomWidget
import net.bewis09.renderite.logic.FitType

class CustomWidgetHelpPopup : SimpleRenderable({
    width = 200
    heightProvider = { Bewisclient.screenHeight - 100 }
    minWidth = 20
    minHeight = 20
}) {
    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderPopupBackground(screenDrawing, x, y, width, height, 10, 0.15f)
    }

    override fun Init.init() {
        Div(0) {
            gap = 6
            fitType = FitType.SCROLL
            onInit = {
                InfoTextRenderable {
                    text = CustomWidget.customWidgetParamInfo()
                    centered = true
                    padding = 0
                }
                addRenderables(CustomWidget.widgetStringDataPoints.map(::DataPointRenderable))
            }
        }(x + 10, y + 10, width - 20, height - 20)
    }

    // TODO Integrate with Text and InfoTextRenderable
    class DataPointRenderable(val dataPoint: CustomWidget.WidgetStringData) : SimpleRenderable({
        width = 180
        height = 24
    }) {
        override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.drawText(dataPoint.name().append(" ".toText()).append(("{${dataPoint.id}}").toText().setColor((General.getThemeColor(black = 0.5f)).argb)), x, y, General.getThemeColor())
            val texts = screenDrawing.drawWrappedText(dataPoint.description().string, x, y + 10, width, General.getThemeColor(alpha = 0.7f))
            val paramTexts = dataPoint.param?.let { screenDrawing.drawWrappedText("Param: " + it().string, x, y + 10 + texts.size * 10, width, General.getThemeColor(alpha = 0.4f)) } ?: emptyList()
            height = 9 + texts.size * 10 + paramTexts.size * 10
        }
    }
}
