package net.bewis09.bewisclient.drawable.renderables.impl

import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.components.button.Button
import net.bewis09.renderite.logic.TextAlign
import net.bewis09.bewisclient.drawable.renderables.popup.TiwylaLinesSettingsPopup
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.types.ListSetting
import net.bewis09.bewisclient.widget.impl.TiwylaWidget
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.LineType
import net.minecraft.network.chat.Component

class TiwylaLinesSettingsRenderable : PropedRenderable<TiwylaLinesSettingsRenderable>({
    height = 78
    minWidth = 12
}) {
    init { props() }

    companion object {
        val entityText = Translation("widget.tiwyla_widget.entity_lines", "Entity Information")
        val blockText = Translation("widget.tiwyla_widget.block_lines", "Block Information")
        val none = Translation("widget.tiwyla_widget.none", "None")
    }

    override fun init() {
        Div {
            gap = 11
            lines = 2
            lineType = LineType.DEFINITE
            onInit = {
                addForSide(entityText(), TiwylaWidget.entityLines)
                addForSide(blockText(), TiwylaWidget.blockLines, right = true)
            }
        }(x, y, width, height)
        Rectangle {
            backgroundColor = { Color.WHITE alpha 0.25f }
        }(centerX, y + 5, 1, height - 5)
    }

    fun <T> Renderable.addForSide(title: Component, list: ListSetting<TiwylaWidget.Information<T>>, right: Boolean = false) {
        fun openPopup(index: Int, left: Boolean) {
            @Suppress("UNCHECKED_CAST") OptionScreen.currentInstance?.openPopup(TiwylaLinesSettingsPopup {
                options = (if (right) TiwylaWidget.blockInformation else TiwylaWidget.entityInformation) as List<TiwylaWidget.Line<T>>
                setting = list
                this.yIndex = index
                this.left = left
            })
        }

        Div {
            gap = 2
            onInit = {
                Text {
                    text = title
                    textAlign = TextAlign.CENTER
                    verticalAlign = TextAlign.START
                    height = 18
                }
                for (i in 0..2.coerceAtMost(list.size + 1)) {
                    val arr = arrayOf(list.get().getOrNull(i)?.first, list.get().getOrNull(i)?.second).filterNotNull().sortedBy { it.priority }
                    if (arr.isEmpty()) {
                        Button {
                            text = (arr.getOrNull(0)?.translation ?: none)()
                            onClick = { openPopup(i, true) }
                            dark = arr.isEmpty()
                            height = 18
                        }
                    } else {
                        Button {
                            text = (arr.getOrNull(0)?.translation ?: none)()
                            onClick = { openPopup(i, true) }
                            dark = arr.isEmpty()
                            height = 18
                        }
                        Button {
                            text = (arr.getOrNull(1)?.translation ?: none)()
                            onClick = { openPopup(i, false) }
                            dark = arr.size < 2
                            height = 18
                        }
                    }
                }
            }
        }
    }
}