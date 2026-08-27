package net.bewis09.bewisclient.drawable.renderables.components.button

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.drawable.Init
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.util.interfaces.Settable
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.logic.Color

class ResetButton<T>(p: Props<ResetButton<T>>) : TooltipHoverable<ResetButton<T>>(p + {
    tooltipProvider = { if (isDefault()) null else resetText() }
    pointerProvider = { !isDefault() }
    colorModifier = { if(isDefault()) Color(127, 127, 127, 255) else Color.WHITE }
    width = 14
    height = 14
}) {
    var settable: Settable<T?> = {}
    var isDefault: () -> Boolean = { false }

    init { props() }

    companion object {
        val resetText = Translation("menu.general.reset", "Reset")
    }

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val imagePadding = if (General.isMinecrafty) 3 else 2
        screenDrawing.drawTexture(createIdentifier("bewisclient", "textures/gui/sprites/reset.png"), x + imagePadding, y + imagePadding, width - imagePadding * 2, height - imagePadding * 2, General.getTextThemeColor())
    }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        screenDrawing.pushColor(0.8f, 0.8f, 0.8f, 1f)
        SelectiveScreenDrawer.renderButtonBackground(screenDrawing, if (isDefault()) 0f else hoverFactor, 0f, x, y, width, height, 1f)
        screenDrawing.popColor()
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean = settable.set(null).let { true }
}

fun <T> Init.ResetButton(p: RenderiteElement.Props<ResetButton<T>>) = net.bewis09.bewisclient.drawable.renderables.components.button.ResetButton(p).add()