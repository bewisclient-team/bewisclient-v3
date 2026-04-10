package net.bewis09.bewisclient.drawable.renderables.popup

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.Button
import net.bewis09.bewisclient.drawable.renderables.Input
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class InputTextPopup(val text: Component, val onConfirm: (text: String) -> Unit, val default: String = "", confirmText: Component = CommonComponents.GUI_CONTINUE, cancelText: Component = CommonComponents.GUI_CANCEL) : Renderable(200, 100) {
    val input = Input(text = default)

    val cancelButton = Button(cancelText) {
        OptionScreen.currentInstance?.closePopup()
    }

    val confirmButton = Button(confirmText, selected = { true }, onClick = {
        onConfirm(input.text)
        OptionScreen.currentInstance?.closePopup()
    })

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val lines = screenDrawing.wrapText(text.string, width - 20)
        internalHeight = 60 + lines.size * 9

        input.setWidth(width - 12)
        input.setPosition(x + 6, y + height - 40)

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
        addRenderable(input)
    }
}
