package net.bewis09.bewisclient.impl.renderable

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.drawable.renderables.Button
import net.bewis09.bewisclient.drawable.renderables.Text
import net.bewis09.bewisclient.drawable.renderables.VerticalAlignScrollPlane
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.widget.TiwylaWidget
import net.bewis09.bewisclient.settings.types.ListSetting

class TiwylaLinesSettingsPopup<T>(
    val setting: ListSetting<TiwylaWidget.Information<T>>,
    val options: List<TiwylaWidget.Information.Line<T>>,
    val yIndex: Int,
    val left: Boolean
) : Renderable() {
    companion object {
        val selectText = Translation("popup.tiwyla_lines_settings.title", "Select Information")
    }

    val inner = Inner()

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        addRenderable(inner(getWidth() / 2 - inner.getWidth() / 2, getHeight() / 2 - inner.getHeight() / 2, inner.getWidth(), inner.getHeight()))
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
                mutableListOf(Button(TiwylaLinesSettingsRenderable.none.getTranslatedString()) {
                    if (yIndex < setting.size) {
                        val arr = arrayOf(setting[yIndex].first, setting[yIndex].second).filterNotNull().sortedBy { a -> a.priority }
                        setting[yIndex] = TiwylaWidget.Information(
                            if (left) null else arr.getOrNull(0),
                            if (left) arr.getOrNull(1) else null
                        )
                    }

                    OptionScreen.currentInstance?.closePopup()
                    OptionScreen.currentInstance?.resize()
                }.setHeight(14)).also {
                    it += options.map { option ->
                        Button(option.translation.getTranslatedString()) {
                            if (yIndex >= setting.size) {
                                setting.add(TiwylaWidget.Information(option, null))
                            } else {
                                val arr = arrayOf(setting[yIndex].first, setting[yIndex].second).filterNotNull().sortedBy { a -> a.priority }
                                setting[yIndex] = TiwylaWidget.Information(
                                    if (left) option else arr.getOrNull(0),
                                    if (left) arr.getOrNull(1) else option
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
            width = 200u
            height = 100u
        }

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.fillWithBorderRounded(getX(), getY(), getWidth(), getHeight(), 5, 0x333333, 0.9f, 0xAAAAAA, 0.5f)
            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            addRenderable(Text(selectText.getTranslatedString(), centered = true)(getX(), getY() + 6, getWidth(), 14))
            addRenderable(plane(getX() + 5, getY() + 25, getWidth() - 10, getHeight() - 30))
        }
    }
}