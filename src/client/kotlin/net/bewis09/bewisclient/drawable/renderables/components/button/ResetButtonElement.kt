package net.bewis09.bewisclient.drawable.renderables.components.button

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.drawable.Init
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.util.interfaces.Settable
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.logic.Color

class ResetButtonElement<T>(p: Props<ResetButtonElement<T>>) : AbstractButtonElement<ResetButtonElement<T>>(p + {
    tooltipProvider = { if (isDefault()) null else resetText() }
    pointerProvider = { !isDefault() }
    colorModifier = { if(isDefault()) Color(127, 127, 127, 255) else Color.WHITE }
    width = 14
    height = 14
    onClick = { settable.set(null) }
}) {
    var settable: Settable<T?> = {}
    var isDefault: () -> Boolean = { false }

    init { props() }

    companion object {
        val resetText = Translation("menu.general.reset", "Reset")
    }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.pushColor(0.8f, 0.8f, 0.8f, 1f)
        SelectiveScreenDrawer.renderButtonBackground(screenDrawing, if (isDefault()) 0f else hoverFactor, 0f, x, y, width, height, 1f)
        screenDrawing.popColor()
    }

    override fun Init.init() {
        Image {
            image = createIdentifier("bewisclient", "textures/gui/sprites/reset.png")
            padding = if (General.isMinecrafty) 3 else 2
            colorModifier = { General.getTextThemeColor() }
        }
    }
}

fun <T> Init.ResetButton(p: RenderiteElement.Props<ResetButtonElement<T>>) = ResetButtonElement(p).add()