package net.bewis09.bewisclient.drawable.renderables.popup

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.Button
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text

class ConfirmPopup(val text: Text, val onConfirm: () -> Unit, val confirmText: Text = ScreenTexts.CONTINUE, val cancelText: Text = ScreenTexts.CANCEL): Renderable() {
    val inner = Inner()

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        addRenderable(inner.setPosition(width / 2 - inner.width / 2, height / 2 - inner.height / 2))
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!inner.isMouseOver(mouseX, mouseY)) {
            OptionScreen.currentInstance?.closePopup()
            return true
        }
        return super.onMouseClick(mouseX, mouseY, button)
    }

    inner class Inner : Renderable() {
        val cancelButton = Button(cancelText, {
            OptionScreen.currentInstance?.closePopup()
        })
        val confirmButton = Button(confirmText, selected = { true }, onClick = {
            onConfirm()
            OptionScreen.currentInstance?.closePopup()
        })

        init {
            internalWidth = 200
            internalHeight = 100
        }

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            setPosition(this@ConfirmPopup.width / 2 - inner.width / 2, this@ConfirmPopup.height / 2 - inner.height / 2)

            val lines = screenDrawing.wrapText(text.string, width - 20)
            internalHeight = 40 + lines.size * 9

            cancelButton.setSize((width - 18) / 2, 14)
            confirmButton.setSize((width - 18) / 2, 14)

            cancelButton.setPosition(x + 6, y + height - 20)
            confirmButton.setPosition(x + width - confirmButton.width - 6, y + height - 20)

            screenDrawing.fillWithBorderRounded(x, y, width, height, 5, OptionsMenuSettings.getBackgroundColor(), OptionsMenuSettings.getThemeColor(alpha = 0.3f))

            lines.forEachIndexed { index, line ->
                screenDrawing.drawCenteredText(line, x + width / 2, y + 10 + index * 9, OptionsMenuSettings.getTextThemeColor())
            }

            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            addRenderable(cancelButton)
            addRenderable(confirmButton)
        }
    }
}