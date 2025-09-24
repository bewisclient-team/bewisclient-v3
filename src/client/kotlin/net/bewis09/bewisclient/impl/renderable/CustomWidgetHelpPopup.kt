package net.bewis09.bewisclient.impl.renderable

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.VerticalAlignScrollPlane
import net.bewis09.bewisclient.drawable.renderables.screen.PopupScreen
import net.bewis09.bewisclient.drawable.renderables.settings.InfoTextRenderable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.impl.widget.CustomWidget
import net.bewis09.bewisclient.logic.color.Color
import net.bewis09.bewisclient.logic.color.within
import net.minecraft.text.Text

class CustomWidgetHelpPopup(val screen: PopupScreen) : Renderable() {
    val inner = Inner()

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        addRenderable(inner(width / 2 - 100, 50, 200, height - 100))
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!inner.isMouseOver(mouseX, mouseY)) {
            screen.closePopup()
            return true
        }
        return super.onMouseClick(mouseX, mouseY, button)
    }

    class Inner : Renderable() {
        val plane = VerticalAlignScrollPlane(
            mutableListOf<Renderable>(
            InfoTextRenderable(CustomWidget.customWidgetParamInfo().string, centered = true, padding = 0)
        ).also {
            it.addAll(CustomWidget.widgetStringDataPoints.map { dataPoint ->
                DataPointRenderable(dataPoint)
            })
        }, 6
        )

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.fillWithBorderRounded(x, y, width, height, 10, OptionsMenuSettings.getBackgroundColor() alpha 0.9f, OptionsMenuSettings.themeColor.get().getColor() alpha 0.15f)
            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            addRenderable(plane(x + 10, y + 10, width - 20, height - 20))
        }

        class DataPointRenderable(val dataPoint: CustomWidget.WidgetStringData) : Renderable() {
            init {
                internalWidth = 180
                internalHeight = 24
            }

            override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
                screenDrawing.drawText(dataPoint.name().append(" ").append(Text.literal("{${dataPoint.id}}").withColor((0.5f within (Color.BLACK to OptionsMenuSettings.themeColor.get().getColor())).argb)), x, y, OptionsMenuSettings.themeColor.get().getColor())
                val texts = screenDrawing.drawWrappedText(dataPoint.description().string, x, y + 10, width, OptionsMenuSettings.themeColor.get().getColor() alpha 0.7f)
                val paramTexts = dataPoint.param?.let { screenDrawing.drawWrappedText("Param: " + it().string, x, y + 10 + texts.size * 10, width, OptionsMenuSettings.themeColor.get().getColor() alpha 0.4f) } ?: emptyList()
                internalHeight = 9 + texts.size * 10 + paramTexts.size * 10
            }
        }
    }
}