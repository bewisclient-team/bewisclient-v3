package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.animate
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.logic.color.alpha
import java.awt.Color

open class TooltipHoverable(val tooltip: () -> Translation?) : Hoverable() {
    constructor(tooltip: Translation? = null) : this({ tooltip })

    val tooltipAnimation = animate(200, Animator.EASE_IN_OUT, "tooltip" to 0f)
    var wasActuallyDrawn: Boolean? = null
    var isActuallyDrawn: Boolean? = null

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)

        wasActuallyDrawn = isActuallyDrawn
        isActuallyDrawn = null

        val tooltip = tooltip()

        if (tooltip != null && hoverAnimation["hovering"] > 0f) {
            if (hoverAnimation["hovering"] == 1f && wasActuallyDrawn != false) tooltipAnimation["tooltip"] = 1f

            if (wasActuallyDrawn == false) {
                tooltipAnimation.pauseForOnce()
                tooltipAnimation["tooltip"] = 0f
            }

            isActuallyDrawn = false

            screenDrawing.afterDraw("tooltip", {
                isActuallyDrawn = true

                if (hoverAnimation["hovering"] != 1f) return@afterDraw

                screenDrawing.setBewisclientFont()

                val textHeight = screenDrawing.getTextHeight()
                val wrappedText = screenDrawing.wrapText(tooltip.getTranslatedString(), 200)
                val tooltipHeight = wrappedText.size * textHeight + 10

                val width = wrappedText.maxOfOrNull { screenDrawing.getTextWidth(it) }?.plus(10) ?: 210

                if (mouseX + width > client.window.scaledWidth) {
                    screenDrawing.translate(-width.toFloat(), 0f)
                }

                screenDrawing.fillRounded(mouseX, mouseY - tooltipHeight, width, tooltipHeight, 5, Color.BLACK alpha tooltipAnimation["tooltip"] * 0.8f)
                screenDrawing.drawWrappedText(wrappedText, mouseX + 5, mouseY - tooltipHeight + 5, Color.WHITE alpha tooltipAnimation["tooltip"])
            })
        } else {
            if (tooltipAnimation["tooltip"] != 0f) tooltipAnimation.pauseForOnce()
            tooltipAnimation["tooltip"] = 0f
        }
    }

    override fun init() {
        tooltipAnimation.pauseForOnce()
        tooltipAnimation["tooltip"] = 0f
        super.init()
    }
}