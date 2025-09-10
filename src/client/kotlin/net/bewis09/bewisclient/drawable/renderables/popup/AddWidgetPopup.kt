package net.bewis09.bewisclient.drawable.renderables.popup

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.Hoverable
import net.bewis09.bewisclient.drawable.renderables.Text
import net.bewis09.bewisclient.drawable.renderables.VerticalScrollGrid
import net.bewis09.bewisclient.drawable.renderables.screen.HudEditScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.color.within
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.WidgetLoader
import net.bewis09.bewisclient.logic.color.Color

class AddWidgetPopup(val screen: HudEditScreen) : Renderable() {
    companion object {
        val addText = Translation("popup.add_widget.title", "Add Widget")
    }

    val inner = Inner()

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        addRenderable(inner(50, 50, getWidth() - 100, getHeight() - 100))
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!inner.isMouseOver(mouseX, mouseY)) {
            screen.closePopup()
            return true
        }
        return super.onMouseClick(mouseX, mouseY, button)
    }

    inner class Inner : Renderable() {
        val text = Text({ addText.getTranslatedString() }, 0.5f within (OptionsMenuSettings.themeColor.get().getColor() to Color.WHITE), centered = true)
        var grid = VerticalScrollGrid({
            WidgetLoader.widgets.filter { !it.isEnabled() }.map { widget -> WidgetElement(widget, screen, this).setHeight(90) }
        }, 5, 80)

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.fillWithBorderRounded(getX(), getY(), getWidth(), getHeight(), 10, 0.15f within (Color.BLACK to OptionsMenuSettings.themeColor.get().getColor()) alpha 0.5f, OptionsMenuSettings.themeColor.get().getColor() alpha 0.15f)
            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            addRenderable(text(getX(), getY() + 7, getWidth(), 14))
            addRenderable(grid(getX() + 10, getY() + 24, getWidth() - 20, getHeight() - 31))
        }
    }

    class WidgetElement(val widget: Widget, val screen: HudEditScreen, val inner: Inner) : Hoverable() {
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

            val height = (screenDrawing.wrapText(widget.getTranslation().getTranslatedString(), getWidth() - 10).size - 1) * screenDrawing.getTextHeight()
            val descriptionHeight = (screenDrawing.wrapText(widget.getDescription().getTranslatedString(), getWidth() - 10).size - 1) * screenDrawing.getTextHeight()

            screenDrawing.fillRounded(getX(), getY(), getWidth(), getHeight(), 5, OptionsMenuSettings.themeColor.get().getColor() alpha hoverAnimation["hovering"] * 0.15f + 0.15f)
            screenDrawing.drawCenteredWrappedText(widget.getTranslation().getTranslatedString(), getX() + getWidth() / 2, getY() + 14 - height / 2, getWidth() - 10, OptionsMenuSettings.themeColor.get().getColor())
            screenDrawing.drawCenteredWrappedText(widget.getDescription().getTranslatedString(), getX() + getWidth() / 2, getY() + getHeight() - 38 - descriptionHeight / 2, getWidth() - 10, OptionsMenuSettings.themeColor.get().getColor() * 0xAAAAAA alpha 0.65f)

            renderRenderables(screenDrawing, mouseX, mouseY)
        }
    }
}