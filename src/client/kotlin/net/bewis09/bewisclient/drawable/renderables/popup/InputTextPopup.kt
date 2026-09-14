package net.bewis09.bewisclient.drawable.renderables.popup

import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.Button
import net.bewis09.bewisclient.drawable.renderables.components.setting.InputElement
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.logic.TextAlign
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

    val input = InputElement { text = default }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderPopupBackground(screenDrawing, x, y, width, height, 5, 0.3f)
    }

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        updateHeight(60 + screenDrawing.drawWrappedText(text, x + width / 2, y + 10, width - 20) {
            textAlign = TextAlign.CENTER
        }.size * 9)
    }

    override fun init() {
        Button {
            text = cancelText
            onClick = { OptionScreen.currentInstance?.closePopup() }
        }(x + 6, y2 - 20, (width - 18) / 2, 14)
        Button {
            text = confirmText
            selected = { true }
            onClick = { onConfirm(input.text); OptionScreen.currentInstance?.closePopup() }
        }(x2 - (width - 18) / 2 - 6, y2 - 20, (width - 18) / 2, 14)
        input.add(x + 6, y2 - 40, width - 12, 10)
    }
}
