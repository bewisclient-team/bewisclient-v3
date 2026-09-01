package net.bewis09.bewisclient.drawable.renderables.popup

import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.ButtonElement
import net.bewis09.renderite.logic.TextAlign
import net.bewis09.bewisclient.drawable.renderables.impl.TiwylaLinesSettingsRenderable
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.types.ListSetting
import net.bewis09.bewisclient.widget.impl.TiwylaWidget
import net.bewis09.renderite.logic.FitType
import net.minecraft.network.chat.Component

class TiwylaLinesSettingsPopup<T>(p: Props<TiwylaLinesSettingsPopup<T>>) : PropedRenderable<TiwylaLinesSettingsPopup<T>>(p + {
    width = 200
    height = 100
}) {
    lateinit var setting: ListSetting<TiwylaWidget.Information<T>>
    lateinit var options: List<TiwylaWidget.Line<T>>
    var yIndex: Int = -1
    var left: Boolean = true

    init { props() }

    companion object {
        val selectText = Translation("popup.tiwyla_lines_settings.title", "Select Information")
    }

    fun updateButton(text: Component, option: TiwylaWidget.Line<T>?): Renderable {
        return ButtonElement {
            this.text = text
            onClick = {
                if (yIndex >= setting.size && option != null) setting.add(TiwylaWidget.Information(option, null))

                if (yIndex < setting.size) {
                    val arr = arrayOf(setting[yIndex].first, setting[yIndex].second).filterNotNull().sortedBy { a -> a.priority }
                    setting[yIndex] = TiwylaWidget.Information(
                        if (left) option else arr.getOrNull(0), if (left) arr.getOrNull(1) else option
                    )
                }

                OptionScreen.currentInstance?.closePopup()
                OptionScreen.currentInstance?.resize()
            }
        }.updateHeight(SelectiveScreenDrawer.getSideButtonHeight())
    }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderPopupBackground(screenDrawing, x, y, width, height, 5, 0.3f)
    }

    override fun Init.init() {
        Text {
            text = selectText()
            textAlign = TextAlign.CENTER
        }(x, y + 6, width, 14)
        Div(0) {
            gap = 2
            fitType = FitType.SCROLL
            onInit = {
                updateButton(TiwylaLinesSettingsRenderable.none(), null).add()
                addRenderables(options.map { (updateButton(it.translation(), it)) })
            }
        }(x + 5, y + 25, width - 10, height - 30)
    }
}