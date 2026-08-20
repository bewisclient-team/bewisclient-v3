package net.bewis09.bewisclient.drawable.renderables.popup

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.logic.Hoverable
import net.bewis09.bewisclient.drawable.renderables.components.element.TextElement
import net.bewis09.bewisclient.drawable.renderables.components.structure.VerticalScrollGrid
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.util.Bewisclient
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.WidgetLoader

class AddWidgetPopup : Renderable(
    widthProvider = { Bewisclient.screenWidth - 100 },
    heightProvider = { Bewisclient.screenHeight - 100 }
) {
    companion object {
        val addText = Translation("popup.add_widget.title", "Add Widget")
    }

    val text = TextElement({ addText() }, General.getTextThemeColor(), centered = true)
    var grid = VerticalScrollGrid({
        WidgetLoader.widgets.filter { !it.enabled }.map { widget -> WidgetElement(widget).setHeight(90) }
    }, 5, 80)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderPopupBackground(screenDrawing, x, y, width, height, 10, 0.15f)
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        addRenderable(text(x, y + 7, width, 14))
        addRenderable(grid(x + 10, y + 24, width - 20, height - 31))
    }

    inner class WidgetElement(val widget: Widget) : Hoverable() {
        val title = widget.title()
        val description = widget.description()

        override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
            widget.enabled = true
            this@AddWidgetPopup.grid = VerticalScrollGrid({
                WidgetLoader.widgets.filter { !it.enabled }.map { widget -> WidgetElement(widget).setHeight(90) }
            }, 5, 80)
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