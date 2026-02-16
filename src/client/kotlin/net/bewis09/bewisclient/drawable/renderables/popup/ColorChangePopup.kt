package net.bewis09.bewisclient.drawable.renderables.popup

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.Rectangle
import net.bewis09.bewisclient.drawable.renderables.ThemeButton
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.interfaces.Gettable
import net.bewis09.bewisclient.util.color.ColorSaver

class ColorChangePopup(val state: Gettable<ColorSaver>, val onChange: (ColorSaver) -> Unit, val types: Array<String>) : Renderable() {
    val buttons = types.map { type ->
        ColorSaver.getType(type)?.let {
            ThemeButton(it.getTranslation()(), {
                state.get().getType() == type
            }, { _ ->
                if (state.get().getType() != type) {
                    onChange(it.getDefault())
                    renderables.clear()
                    init()
                }
            }, it.getDescription()?.invoke())
        }
    }

    init {
        internalWidth = 200
        internalHeight = 100
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.fillWithBorderRounded(x, y, width, height, 5, OptionsMenuSettings.getBackgroundColor(), OptionsMenuSettings.getThemeColor(alpha = 0.3f))
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        super.init()
        buttons.forEachIndexed { index, button ->
            button?.let {
                it.setSize((width - ((buttons.size - 1) * 5) - 10) / buttons.size, 14)
                it.setPosition(x + 5 + index * (it.width + 5), y + height - 20)
                addRenderable(it)
            }
        }
        addRenderable(Rectangle { OptionsMenuSettings.getThemeColor(alpha = 0.3f) }(x + 5, y + height - 26, width - 11, 1))
        ColorSaver.getFactory(state.get())?.getSettingsRenderable({ state.get() }, onChange)(x + 5, y + 6, width - 11, height - 37)?.let { addRenderable(it); it.resize() }
    }
}