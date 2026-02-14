package net.bewis09.bewisclient.drawable.renderables.popup

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.renderables.screen.HudEditScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.WidgetLoader

class AddWidgetPopup(val screen: HudEditScreen) : Renderable() {
    companion object {
        val addText = Translation("popup.add_widget.title", "Add Widget")
    }

    val inner = Inner()

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        addRenderable(inner(50, 50, width - 100, height - 100))
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!inner.isMouseOver(mouseX, mouseY)) {
            screen.closePopup()
            return true
        }
        return super.onMouseClick(mouseX, mouseY, button)
    }

    inner class Inner : Renderable() {
        val text = TextElement({ addText() }, OptionsMenuSettings.getTextThemeColor(), centered = true)
        var grid = VerticalScrollGrid({
            WidgetLoader.widgets.filter { !it.isEnabled() }.map { widget -> WidgetElement(widget, screen, this).setHeight(90) }
        }, 5, 80)

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.fillWithBorderRounded(x, y, width, height, 10, OptionsMenuSettings.getBackgroundColor(), OptionsMenuSettings.getThemeColor(alpha = 0.15f))
            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            addRenderable(text(x, y + 7, width, 14))
            addRenderable(grid(x + 10, y + 24, width - 20, height - 31))
        }
    }

    class WidgetElement(val widget: Widget, val screen: HudEditScreen, val inner: Inner) : Hoverable() {
        val title = widget.widgetTitle()
        val description = widget.widgetDescription()

        override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
            widget.enabled.set(true)
            inner.grid = VerticalScrollGrid({
                WidgetLoader.widgets.filter { !it.isEnabled() }.map { widget -> WidgetElement(widget, screen, inner).setHeight(90) }
            }, 5, 80)
            inner.resize()

            return true
        }

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            super.render(screenDrawing, mouseX, mouseY)

            val textHeight = (screenDrawing.wrapText(title.string, width - 10).size - 1) * screenDrawing.getTextHeight()
            val descriptionHeight = (screenDrawing.wrapText(description.string, width - 10).size - 1) * screenDrawing.getTextHeight()

            screenDrawing.fillWithBorderRounded(x, y, width, height, 5, OptionsMenuSettings.getThemeColor(alpha = hoverAnimation["hovering"] * 0.15f + 0.15f), OptionsMenuSettings.getThemeColor(alpha = hoverAnimation["hovering"] * 0.15f + 0.15f))
            screenDrawing.drawCenteredWrappedText(title, centerX, y + 14 - textHeight / 2, width - 10, OptionsMenuSettings.getThemeColor())
            screenDrawing.drawCenteredWrappedText(description, centerX, y2 - 38 - descriptionHeight / 2, width - 10, OptionsMenuSettings.getThemeColor() * 0xAAAAAA alpha 0.65f)

            renderRenderables(screenDrawing, mouseX, mouseY)
        }
    }
}