package net.bewis09.bewisclient.drawable.renderables.components.logic

import net.bewis09.renderite.logic.Animator
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.util.Bewisclient
import net.bewis09.renderite.drawer.pushAlpha
import net.bewis09.bewisclient.version.translateToTopOptional
import net.bewis09.renderite.components.Hoverable
import net.bewis09.renderite.logic.Color
import net.minecraft.network.chat.Component

abstract class TooltipHoverable<P: TooltipHoverable<P>>(p: Props<P>) : Hoverable<P>(p) {
    val tooltipAnimation = Animator(200, Animator.EASE_IN_OUT, 0f)
    var wasActuallyDrawn: Boolean? = null
    var isActuallyDrawn: Boolean? = null

    var tooltipProvider: () -> Component? = { tooltip }
    var tooltip: Component? = null

    override fun renderAccessories(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        wasActuallyDrawn = isActuallyDrawn
        isActuallyDrawn = null

        val tooltip = tooltipProvider()

        if (tooltip != null && hoverFactor > 0f) {
            if (hoverFactor == 1f && wasActuallyDrawn != false) tooltipAnimation.set(1f)

            if (wasActuallyDrawn == false) {
                tooltipAnimation.setInstant(0f)
            }

            isActuallyDrawn = false

            screenDrawing.afterDraw("tooltip", {
                isActuallyDrawn = true

                if (hoverFactor != 1f) return@afterDraw

                screenDrawing.setBewisclientFont()

                val textHeight = screenDrawing.getTextHeight()
                val wrappedText = screenDrawing.wrapText(tooltip, 200)
                val tooltipHeight = wrappedText.size * textHeight + 10

                val width = wrappedText.maxOfOrNull { screenDrawing.getTextWidth(it) }?.plus(10) ?: 210

                if (mouseX + width > Bewisclient.screenWidth) {
                    screenDrawing.translate(-width.toFloat(), 0f)
                }

                screenDrawing.push()
                screenDrawing.guiGraphics.translateToTopOptional()
                if (General.isMinecrafty) {
                    screenDrawing.pushAlpha(tooltipAnimation.get() * 0.9f) {
                        SelectiveScreenDrawer.renderButtonBackground(screenDrawing, 1f, 0f, mouseX, mouseY - tooltipHeight, width, tooltipHeight, 1f, true)
                    }
                } else {
                    screenDrawing.fillRounded(mouseX, mouseY - tooltipHeight, width, tooltipHeight, 5, Color.BLACK alpha tooltipAnimation.get() * 0.8f)
                }
                screenDrawing.drawWrappedText(wrappedText, mouseX + 5, mouseY - tooltipHeight + 5, Color.WHITE alpha tooltipAnimation.get())
                screenDrawing.pop()
            })
        } else {
            if (tooltipAnimation.get() != 0f) tooltipAnimation.pauseForOnce()
            tooltipAnimation.set(0f)
        }
    }

    override fun initLogic() {
        tooltipAnimation.setInstant(0f)
        super.initLogic()
    }
}