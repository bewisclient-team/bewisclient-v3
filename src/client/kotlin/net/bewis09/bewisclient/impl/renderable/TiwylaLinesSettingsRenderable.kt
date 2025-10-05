package net.bewis09.bewisclient.impl.renderable

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.impl.widget.TiwylaWidget
import net.bewis09.bewisclient.util.color.Color
import net.bewis09.bewisclient.util.color.within
import net.bewis09.bewisclient.settings.types.ListSetting

class TiwylaLinesSettingsRenderable : Renderable() {
    companion object {
        val entityText = Translation("widget.tiwyla_widget.entity_lines", "Entity Information")
        val blockText = Translation("widget.tiwyla_widget.block_lines", "Block Information")
        val none = Translation("widget.tiwyla_widget.none", "None")
    }

    init {
        internalHeight = 78
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        if (width < 12) return

        addRenderable(Rectangle(Color.WHITE alpha 0.25f)(centerX, y + 5, 1, height - 5))
        addRenderable(TextElement(entityText(), 0.5f within (Color.WHITE to OptionsMenuSettings.themeColor.get().getColor()), true)(x, y + 6, (width - 11) / 2, 9))
        addRenderable(TextElement(blockText(), 0.5f within (Color.WHITE to OptionsMenuSettings.themeColor.get().getColor()), true)(x2 - (width - 11) / 2, y + 6, (width - 11) / 2, 9))

        addForSide(TiwylaWidget.entityLines)
        addForSide(TiwylaWidget.blockLines, right = true)
    }

    fun <T> addForSide(list: ListSetting<TiwylaWidget.Information<T>>, right: Boolean = false) {
        fun openPopup(index: Int, left: Boolean) {
            @Suppress("UNCHECKED_CAST") OptionScreen.currentInstance?.openPopup(TiwylaLinesSettingsPopup(list, ((if (right) TiwylaWidget.blockInformation else TiwylaWidget.entityInformation) as List<TiwylaWidget.Information.Line<T>>), index, left))
        }

        for (i in 0..2.coerceAtMost(list.size + 1)) {
            val arr = arrayOf(list.get().getOrNull(i)?.first, list.get().getOrNull(i)?.second).filterNotNull().sortedBy { it.priority }
            if (arr.isEmpty()) {
                addRenderable(Button((arr.getOrNull(0)?.translation ?: none)(), {
                    openPopup(i, true)
                }, dark = arr.isEmpty())(if (right) x2 - (width - 11) / 2 else x, y + 20 + i * 20, (width - 11) / 2, 18))
            } else {
                addRenderable(Button((arr.getOrNull(0)?.translation ?: none)(), {
                    openPopup(i, true)
                }, dark = arr.isEmpty())(if (right) x2 - (width - 11) / 2 else x, y + 20 + i * 20, (width - 13) / 4, 18))
                addRenderable(Button((arr.getOrNull(1)?.translation ?: none)(), {
                    openPopup(i, false)
                }, dark = arr.size < 2)(if (right) x2 - (width - 13) / 4 else x + (width - 11) / 2 - (width - 13) / 4, y + 20 + i * 20, (width - 13) / 4, 18))
            }
        }
    }
}