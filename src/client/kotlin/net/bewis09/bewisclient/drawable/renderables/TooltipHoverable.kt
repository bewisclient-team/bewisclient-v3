package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.util.color.Color
import net.minecraft.text.Text

open class TooltipHoverable(val tooltip: () -> Text?) : Hoverable() {
    constructor(tooltip: Text? = null) : this({ tooltip })

    val tooltipAnimation = Animator(200, Animator.EASE_IN_OUT, 0f)
    var wasActuallyDrawn: Boolean? = null
    var isActuallyDrawn: Boolean? = null

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)

        wasActuallyDrawn = isActuallyDrawn
        isActuallyDrawn = null

        val tooltip = tooltip()

        if (tooltip != null && hoverFactor > 0f) {
            if (hoverFactor == 1f && wasActuallyDrawn != false) tooltipAnimation.set(1f)

            if (wasActuallyDrawn == false) {
                tooltipAnimation.pauseForOnce()
                tooltipAnimation.set(0f)
            }

            isActuallyDrawn = false

            screenDrawing.afterDraw("tooltip", {
                isActuallyDrawn = true

                if (hoverFactor != 1f) return@afterDraw

                screenDrawing.setBewisclientFont()

                val textHeight = screenDrawing.getTextHeight()
                val wrappedText = screenDrawing.wrapText(tooltip.string, 200)
                val tooltipHeight = wrappedText.size * textHeight + 10

                val width = wrappedText.maxOfOrNull { screenDrawing.getTextWidth(it) }?.plus(10) ?: 210

                if (mouseX + width > util.width) {
                    screenDrawing.translate(-width.toFloat(), 0f)
                }

                screenDrawing.fillRounded(mouseX, mouseY - tooltipHeight, width, tooltipHeight, 5, Color.BLACK alpha tooltipAnimation.get() * 0.8f)
                screenDrawing.drawWrappedText(wrappedText, mouseX + 5, mouseY - tooltipHeight + 5, Color.WHITE alpha tooltipAnimation.get())
            })
        } else {
            if (tooltipAnimation.get() != 0f) tooltipAnimation.pauseForOnce()
            tooltipAnimation.set(0f)
        }
    }

    override fun init() {
        tooltipAnimation.pauseForOnce()
        tooltipAnimation.set(0f)
        super.init()
    }
}