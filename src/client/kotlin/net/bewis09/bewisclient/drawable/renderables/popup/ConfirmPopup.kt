package net.bewis09.bewisclient.drawable.renderables.popup

import net.bewis09.bewisclient.drawable.PropedRenderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.Button
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.logic.TextAlign
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class ConfirmPopup(p: Props<ConfirmPopup>) : PropedRenderable<ConfirmPopup>(p + {
    width = 200
    height = 20
}) {
    lateinit var text: Component
    var onConfirm: () -> Unit = {}
    var confirmText: Component = CommonComponents.GUI_CONTINUE
    var cancelText: Component = CommonComponents.GUI_CANCEL

    init { props() }

    override fun renderLogic(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        updateHeight(40 + screenDrawing.wrapText(text, width - 20).size * 9)
    }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderPopupBackground(screenDrawing, x, y, width, height, 5, 0.3f)
    }

    override fun init() {
        Button {
            text = cancelText
            onClick = { OptionScreen.currentInstance?.closePopup() }
        }(x + 6, y + height - SelectiveScreenDrawer.getSideButtonHeight() - 6, (width - 18) / 2, SelectiveScreenDrawer.getSideButtonHeight())
        Button {
            text = confirmText
            selected = { true }
            onClick = {
                onConfirm()
                OptionScreen.currentInstance?.closePopup()
            }
        }(x + width / 2 + 3, y + height - SelectiveScreenDrawer.getSideButtonHeight() - 6, (width - 18) / 2, SelectiveScreenDrawer.getSideButtonHeight())
        Text {
            text = this@ConfirmPopup.text
            wrap = true
            textAlign = TextAlign.CENTER
            verticalAlign = TextAlign.START
        }(x + 10, y + 10, width - 20, height)
    }
}