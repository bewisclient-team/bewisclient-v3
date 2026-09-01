package net.bewis09.bewisclient.drawable.renderables.popup

import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.ThemeButtonElement
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.util.color.ColorSaver
import net.bewis09.bewisclient.util.interfaces.Gettable

class ColorChangePopup(val state: Gettable<ColorSaver>, val onChange: (ColorSaver) -> Unit, val types: Array<String>) : PropedRenderable<ColorChangePopup>({
    width = 200
    height = 100
}) {
    init {
        props()
    }

    val buttons = types.map { type ->
        ColorSaver.getType(type)?.let {
            ThemeButtonElement {
                text = it.getTranslation()()
                selected = { state.get().getType() == type }
                tooltip = it.getDescription()?.invoke()
                onClick = { _ ->
                    if (state.get().getType() != type) {
                        onChange(it.getDefault())
                        this@ColorChangePopup.resize()
                    }
                }
            }
        }
    }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderPopupBackground(screenDrawing, x, y, width, height, 5, 0.3f)
    }

    override fun Init.init() {
        buttons.forEachIndexed { index, button ->
            button?.updateSize((width - ((buttons.size - 1) * 5) - 10) / buttons.size, 14)
            button?.addPositioned(x + 5 + index * (button.width + 5), y + height - 20)
        }
        Rectangle { colorProvider = { General.getThemeColor(alpha = 0.3f) } }(x + 5, y + height - 26, width - 11, 1)
        ColorSaver.getFactory(state.get())?.getSettingsRenderable({ state.get() }, onChange)(x + 5, y + 6, width - 11, height - 37)?.let {
            addRenderable(it); it.resize()
        }
    }
}