package net.bewis09.bewisclient.drawable.renderables.popup

import net.bewis09.bewisclient.common.setColor
import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.SimpleRenderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.structure.Grid
import net.bewis09.bewisclient.drawable.renderables.settings.InfoTextRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.util.Bewisclient
import net.bewis09.bewisclient.widget.impl.CustomWidget

class CustomWidgetHelpPopup : SimpleRenderable({
    width = 200
    heightProvider = { Bewisclient.screenHeight - 100 }
}) {
    val plane = Grid {
        gap = 6
        fitType = Grid.FitType.SCROLL
        children = mutableListOf<Renderable>(
            InfoTextRenderable {
                text = CustomWidget.customWidgetParamInfo()
                centered = true
                padding = 0
            }
        ).also { it.addAll(CustomWidget.widgetStringDataPoints.map(::DataPointRenderable)) }
    }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderPopupBackground(screenDrawing, x, y, width, height, 10, 0.15f)
    }

    override fun init() {
        if (width < 20 || height < 20) return
        addRenderable(plane(x + 10, y + 10, width - 20, height - 20))
    }

    class DataPointRenderable(val dataPoint: CustomWidget.WidgetStringData) : SimpleRenderable({
        width = 180
        height = 24
    }) {
        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.drawText(dataPoint.name().append(" ".toText()).append(("{${dataPoint.id}}").toText().setColor((General.getThemeColor(black = 0.5f)).argb)), x, y, General.getThemeColor())
            val texts = screenDrawing.drawWrappedText(dataPoint.description().string, x, y + 10, width, General.getThemeColor(alpha = 0.7f))
            val paramTexts = dataPoint.param?.let { screenDrawing.drawWrappedText("Param: " + it().string, x, y + 10 + texts.size * 10, width, General.getThemeColor(alpha = 0.4f)) } ?: emptyList()
            height = 9 + texts.size * 10 + paramTexts.size * 10
        }
    }
}
