package net.bewis09.bewisclient.drawable.renderables.impl

import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.renderables.components.button.Button
import net.bewis09.renderite.logic.TextAlign
import net.bewis09.bewisclient.drawable.renderables.popup.TiwylaLinesSettingsPopup
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.types.ListSetting
import net.bewis09.bewisclient.widget.impl.TiwylaWidget
import net.bewis09.renderite.logic.Color

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

    override fun Init.init() {
        Rectangle {
            color = Color.WHITE alpha 0.25f
        }(centerX, y + 5, 1, height - 5)
        Text {
            text = entityText()
            textAlign = TextAlign.CENTER
        }(x, y + 6, (width - 11) / 2, 15)
        Text {
            text = blockText()
            textAlign = TextAlign.CENTER
        }(x2 - (width - 11) / 2, y + 6, (width - 11) / 2, 9)

        addForSide(TiwylaWidget.entityLines)
        addForSide(TiwylaWidget.blockLines, right = true)
    }

    fun <T> Init.addForSide(list: ListSetting<TiwylaWidget.Information<T>>, right: Boolean = false) {
        fun openPopup(index: Int, left: Boolean) {
            @Suppress("UNCHECKED_CAST") OptionScreen.currentInstance?.openPopup(TiwylaLinesSettingsPopup {
                options = (if (right) TiwylaWidget.blockInformation else TiwylaWidget.entityInformation) as List<TiwylaWidget.Line<T>>
                setting = list
                this.yIndex = index
                this.left = left
            })
        }

        for (i in 0..2.coerceAtMost(list.size + 1)) {
            val arr = arrayOf(list.get().getOrNull(i)?.first, list.get().getOrNull(i)?.second).filterNotNull().sortedBy { it.priority }
            if (arr.isEmpty()) {
                Button{
                    text = (arr.getOrNull(0)?.translation ?: none)()
                    onClick = { openPopup(i, true) }
                    dark = arr.isEmpty()
                }(if (right) x2 - (width - 11) / 2 else x, y + 20 + i * 20, (width - 11) / 2, 18)
            } else {
                Button{
                    text = (arr.getOrNull(0)?.translation ?: none)()
                    onClick = { openPopup(i, true) }
                    dark = arr.isEmpty()
                }(if (right) x2 - (width - 11) / 2 else x, y + 20 + i * 20, (width - 13) / 4, 18)
                Button{
                    text = (arr.getOrNull(1)?.translation ?: none)()
                    onClick = { openPopup(i, false) }
                    dark = arr.size < 2
                }(if (right) x2 - (width - 13) / 4 else x + (width - 11) / 2 - (width - 13) / 4, y + 20 + i * 20, (width - 13) / 4, 18)
            }
        }
    }
}