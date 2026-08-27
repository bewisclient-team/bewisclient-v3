package net.bewis09.bewisclient.drawable.renderables.components.button

import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.Init
import net.bewis09.renderite.logic.TextAlign
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.renderables.popup.ColorChangePopup
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.util.color.ColorSaver
import net.bewis09.bewisclient.util.interfaces.Gettable
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.logic.Color

class ColorInfoButton(p: Props<ColorInfoButton>) : TooltipHoverable<ColorInfoButton>(p + {
    tooltip = changeColorTranslation()
    height = 14
    width = 160
    shouldUsePointer = true
}) {
    companion object {
        val changeColorTranslation = Translation("menu.color.change_color", "Change Color")
    }

    lateinit var state: Gettable<ColorSaver>
    lateinit var types: Array<String>
    var onChange: (ColorSaver) -> Unit = {}

    init { props() }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val colorSaver = state.get()
        screenDrawing.fillWithBorderRounded(x, y, width, height, if (General.isMinecrafty) 0 else 5, colorSaver.getColor() alpha hoverFactor * 0.3f + 0.3f, colorSaver.getColor() alpha hoverFactor * 0.5f + 0.5f)
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        OptionScreen.currentInstance?.openPopup(ColorChangePopup(state, onChange, types))
        return true
    }

    override fun Init.init() {
        Text {
            textProvider = { state.get().toInfoString().toText() }
            color = Color.WHITE
            textAlign = TextAlign.CENTER
        }
    }
}

fun Init.ColorInfoButton(p: RenderiteElement.Props<ColorInfoButton>) = net.bewis09.bewisclient.drawable.renderables.components.button.ColorInfoButton(p).add()