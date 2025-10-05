package net.bewis09.bewisclient.impl.renderable

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.impl.widget.TiwylaWidget
import net.bewis09.bewisclient.util.color.color
import net.bewis09.bewisclient.settings.types.ListSetting

class TiwylaLinesSettingsPopup<T>(
    val setting: ListSetting<TiwylaWidget.Information<T>>, val options: List<TiwylaWidget.Information.Line<T>>, val yIndex: Int, val left: Boolean
) : Renderable() {
    companion object {
        val selectText = Translation("popup.tiwyla_lines_settings.title", "Select Information")
    }

    val inner = Inner()

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        addRenderable(inner(width / 2 - inner.width / 2, height / 2 - inner.height / 2, inner.width, inner.height))
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!inner.isMouseOver(mouseX, mouseY)) {
            OptionScreen.currentInstance?.closePopup()
            return true
        }
        return super.onMouseClick(mouseX, mouseY, button)
    }

    inner class Inner : Renderable() {
        val plane = VerticalAlignScrollPlane(
            {
                mutableListOf(Button(TiwylaLinesSettingsRenderable.none()) {
                    if (yIndex < setting.size) {
                        val arr = arrayOf(setting[yIndex].first, setting[yIndex].second).filterNotNull().sortedBy { a -> a.priority }
                        setting[yIndex] = TiwylaWidget.Information(
                            if (left) null else arr.getOrNull(0), if (left) arr.getOrNull(1) else null
                        )
                    }

                    OptionScreen.currentInstance?.closePopup()
                    OptionScreen.currentInstance?.resize()
                }.setHeight(14)).also {
                    it += options.map { option ->
                        Button(option.translation()) {
                            if (yIndex >= setting.size) {
                                setting.add(TiwylaWidget.Information(option, null))
                            } else {
                                val arr = arrayOf(setting[yIndex].first, setting[yIndex].second).filterNotNull().sortedBy { a -> a.priority }
                                setting[yIndex] = TiwylaWidget.Information(
                                    if (left) option else arr.getOrNull(0), if (left) arr.getOrNull(1) else option
                                )
                            }

                            OptionScreen.currentInstance?.closePopup()
                            OptionScreen.currentInstance?.resize()
                        }.setHeight(14)
                    }
                }
            }, 2
        )

        init {
            internalWidth = 200
            internalHeight = 100
        }

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.fillWithBorderRounded(x, y, width, height, 5, OptionsMenuSettings.themeColor.get().getColor() * 0x333333.color alpha 0.9f, OptionsMenuSettings.themeColor.get().getColor() * 0xAAAAAA.color alpha 0.5f)
            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            addRenderable(TextElement(selectText(), centered = true)(x, y + 6, width, 14))
            addRenderable(plane(x + 5, y + 25, width - 10, height - 30))
        }
    }
}