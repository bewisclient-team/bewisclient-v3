package net.bewis09.bewisclient.drawable.renderables.popup

import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.Button
import net.bewis09.bewisclient.drawable.renderables.components.setting.Input
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class InputTextPopup(p: Props<InputTextPopup>) : PropedRenderable<InputTextPopup>(p + {
    width = 200
    height = 20
}) {
    lateinit var text: Component
    var onConfirm: (text: String) -> Unit = {}
    var default: String = ""
    var confirmText: Component = CommonComponents.GUI_CONTINUE
    var cancelText: Component = CommonComponents.GUI_CANCEL

    init { props() }

    val input = Input { text = default }

    val cancelButton = Button {
        text = cancelText
        onClick = { OptionScreen.currentInstance?.closePopup() }
    }

    val confirmButton = Button{
        text = confirmText
        selected = { true }
        onClick = { onConfirm(input.text); OptionScreen.currentInstance?.closePopup() }
    }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderPopupBackground(screenDrawing, x, y, width, height, 5, 0.3f)
    }

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val lines = screenDrawing.wrapText(text.string, width - 20)
        height = 60 + lines.size * 9

        lines.forEachIndexed { index, line ->
            screenDrawing.drawCenteredText(line, x + width / 2, y + 10 + index * 9, General.getTextThemeColor())
        }
    }

    override fun init() {
        addRenderable(cancelButton(x + 6, y + height - 20, (width - 18) / 2, 14))
        addRenderable(confirmButton(x + width - (width - 18) / 2 - 6, y + height - 20, (width - 18) / 2, 14))
        addRenderable(input(x + 6, y + height - 40, width - 12, 0))
    }
}
