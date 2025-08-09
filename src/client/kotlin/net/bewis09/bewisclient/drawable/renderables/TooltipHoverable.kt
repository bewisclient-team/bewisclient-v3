package net.bewis09.bewisclient.drawable.renderables

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.drawable.animate
import net.bewis09.bewisclient.drawable.combineInt
import net.bewis09.bewisclient.game.Translation
import net.minecraft.client.MinecraftClient

open class TooltipHoverable(val tooltip: Translation?): Hoverable() {
    val tooltipAnimation = animate(200, Animator.EASE_IN_OUT, "tooltip" to 0f)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        if (tooltip != null && hoverAnimation["hovering"] > 0f) {
            if (hoverAnimation["hovering"] == 1f)
                tooltipAnimation["tooltip"] = 1f

            screenDrawing.afterDraw("tooltip") {
                if (hoverAnimation["hovering"] != 1f) return@afterDraw

                screenDrawing.setBewisclientFont()

                val textHeight = screenDrawing.getTextHeight()
                val wrappedText = screenDrawing.wrapText(tooltip.getTranslatedString(), 200)
                val tooltipHeight = wrappedText.size * textHeight + 10

                val width = wrappedText.maxOfOrNull { screenDrawing.getTextWidth(it) }?.plus(10) ?: 210

                if (mouseX + width > MinecraftClient.getInstance().window.scaledWidth) {
                    screenDrawing.translate(-width.toFloat(),0f)
                }

                screenDrawing.fillRounded(mouseX, mouseY - tooltipHeight, width, tooltipHeight, 5, 0x000000, tooltipAnimation["tooltip"] * 0.8f)
                screenDrawing.drawWrappedText(wrappedText, mouseX + 5, mouseY - tooltipHeight + 5, combineInt(0xFFFFFF, tooltipAnimation["tooltip"]))
            }
        } else {
            if (tooltipAnimation["tooltip"] != 0f)
                tooltipAnimation.pauseForOnce()
            tooltipAnimation["tooltip"] = 0f
        }
    }

    override fun init() {
        tooltipAnimation.pauseForOnce()
        tooltipAnimation["tooltip"] = 0f
        super.init()
    }
}