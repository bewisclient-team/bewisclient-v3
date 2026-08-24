package net.bewis09.bewisclient.drawable.renderables.impl

import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.renderables.components.button.Button
import net.bewis09.bewisclient.drawable.renderables.components.element.Rectangle
import net.bewis09.bewisclient.drawable.renderables.components.element.Text
import net.bewis09.bewisclient.drawable.renderables.components.logic.TextAlign
import net.bewis09.bewisclient.drawable.renderables.popup.TiwylaLinesSettingsPopup
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.types.ListSetting
import net.bewis09.bewisclient.widget.impl.TiwylaWidget
import net.bewis09.renderite.logic.Color

class TiwylaLinesSettingsRenderable : PropedRenderable<TiwylaLinesSettingsRenderable>({
    height = 78
}) {
    companion object {
        val entityText = Translation("widget.tiwyla_widget.entity_lines", "Entity Information")
        val blockText = Translation("widget.tiwyla_widget.block_lines", "Block Information")
        val none = Translation("widget.tiwyla_widget.none", "None")
    }

    override fun init() {
        if (width < 12) return

        addRenderable(Rectangle {
            color = Color.WHITE alpha 0.25f
            x = this@TiwylaLinesSettingsRenderable.centerX
            y = this@TiwylaLinesSettingsRenderable.y + 5
            width = 1
            height = this@TiwylaLinesSettingsRenderable.height - 5
        })
        addRenderable(Text{
            text = entityText()
            textAlign = TextAlign.CENTER
            x = this@TiwylaLinesSettingsRenderable.x
            y = this@TiwylaLinesSettingsRenderable.y + 6
            width = (this@TiwylaLinesSettingsRenderable.width - 11) / 2
            height = 9
        })
        addRenderable(Text{
            text = blockText()
            textAlign = TextAlign.CENTER
            x = this@TiwylaLinesSettingsRenderable.x2 - (this@TiwylaLinesSettingsRenderable.width - 11) / 2
            y = this@TiwylaLinesSettingsRenderable.y + 6
            width = (this@TiwylaLinesSettingsRenderable.width - 11) / 2
            height = 9
        })

        addForSide(TiwylaWidget.entityLines)
        addForSide(TiwylaWidget.blockLines, right = true)
    }

    fun <T> addForSide(list: ListSetting<TiwylaWidget.Information<T>>, right: Boolean = false) {
        fun openPopup(index: Int, left: Boolean) {
            @Suppress("UNCHECKED_CAST") OptionScreen.currentInstance?.openPopup(TiwylaLinesSettingsPopup<T> {
                options = (if (right) TiwylaWidget.blockInformation else TiwylaWidget.entityInformation) as List<TiwylaWidget.Line<T>>
                setting = list
                this.yIndex = index
                this.left = left
            })
        }

        for (i in 0..2.coerceAtMost(list.size + 1)) {
            val arr = arrayOf(list.get().getOrNull(i)?.first, list.get().getOrNull(i)?.second).filterNotNull().sortedBy { it.priority }
            if (arr.isEmpty()) {
                addRenderable(Button{
                    text = (arr.getOrNull(0)?.translation ?: none)()
                    onClick = { openPopup(i, true) }
                    dark = arr.isEmpty()
                }(if (right) x2 - (width - 11) / 2 else x, y + 20 + i * 20, (width - 11) / 2, 18))
            } else {
                addRenderable(Button{
                    text = (arr.getOrNull(0)?.translation ?: none)()
                    onClick = { openPopup(i, true) }
                    dark = arr.isEmpty()
                }(if (right) x2 - (width - 11) / 2 else x, y + 20 + i * 20, (width - 13) / 4, 18))
                addRenderable(Button{
                    text = (arr.getOrNull(1)?.translation ?: none)()
                    onClick = { openPopup(i, false) }
                    dark = arr.size < 2
                }(if (right) x2 - (width - 13) / 4 else x + (width - 11) / 2 - (width - 13) / 4, y + 20 + i * 20, (width - 13) / 4, 18))
            }
        }
    }
}