package net.bewis09.bewisclient.drawable.renderables.components.button

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.util.interfaces.Settable

class ResetButton<T>(p: Props<ResetButton<T>>) : TooltipHoverable<ResetButton<T>>(p + {
    tooltipProvider = { if (isDefault()) null else resetText() }
    width = 14
    height = 14
}) {
    var settable: Settable<T?> = {}
    var isDefault: () -> Boolean = { false }

    init { props() }

    companion object {
        val resetText = Translation("menu.general.reset", "Reset")
    }

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)

        if (!isDefault())
            usePointer(screenDrawing, mouseX, mouseY)

        if (isDefault())
            screenDrawing.pushColor(0.5f, 0.5f, 0.5f, 1f)

        screenDrawing.pushColor(0.8f, 0.8f, 0.8f, 1f)
        SelectiveScreenDrawer.renderButtonBackground(screenDrawing, if (isDefault()) 0f else hoverFactor, 0f, x, y, width, height, 1f, mouseX, mouseY)
        screenDrawing.popColor()

        val imagePadding = if (General.isMinecrafty) 3 else 2
        screenDrawing.drawTexture(createIdentifier("bewisclient", "textures/gui/sprites/reset.png"), x + imagePadding, y + imagePadding, width - imagePadding * 2, height - imagePadding * 2, General.getTextThemeColor())

        if (isDefault())
            screenDrawing.popColor()
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean = settable.set(null).let { true }
}