package net.bewis09.bewisclient.drawable.renderables.popup

import kotlinx.atomicfu.AtomicRef
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.Button
import net.bewis09.bewisclient.drawable.renderables.Rectangle
import net.bewis09.bewisclient.drawable.renderables.ThemeButton
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.interfaces.Gettable
import net.bewis09.bewisclient.logic.color.ColorSaver

class ColorChangePopup(val state: Gettable<ColorSaver>, val onChange: (ColorSaver) -> Unit, val types: Array<String>) : Renderable() {
    val inner = Inner()

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        addRenderable(inner.setPosition(getWidth() / 2 - inner.getWidth() / 2, getHeight() / 2 - inner.getHeight() / 2))
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!inner.isMouseOver(mouseX, mouseY)) {
            OptionScreen.currentInstance?.closePopup()
            return true
        }
        return super.onMouseClick(mouseX, mouseY, button)
    }

    inner class Inner : Renderable() {
        val buttons = types.map { type ->
            ColorSaver.getType(type)?.let {
                ThemeButton(it.getTranslation().getTranslatedString(), {
                    state.get().getType() == type
                }, { _ ->
                    if (state.get().getType() != type) {
                        onChange(it.getDefault())
                        renderables.clear()
                        init()
                    }
                }, it.getDescription())
            }
        }

        init {
            width = 200u
            height = 100u
        }

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.fillWithBorderRounded(getX(), getY(), getWidth(), getHeight(), 5, 0x333333, 0.9f, 0xAAAAAA, 0.5f)
            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            super.init()
            buttons.forEachIndexed { index, button ->
                button?.let {
                    it.setWidth((getWidth() - ((buttons.size - 1) * 5) - 10) / buttons.size)
                    it.setPosition(getX() + 5 + index * (it.getWidth() + 5), getY() + getHeight() - 20)
                    it.setHeight(14)
                    addRenderable(it)
                }
            }
            addRenderable(Rectangle(0x7FAAAAAA)(getX() + 5, getY() + getHeight() - 26, getWidth() - 11, 1))
            ColorSaver.getFactory(state.get())?.getSettingsRenderable({ state.get() }, onChange)(getX() + 5, getY() + 6, getWidth() - 11, getHeight() - 37)?.let { addRenderable(it); it.resize() }
        }
    }
}