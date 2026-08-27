package net.bewis09.bewisclient.drawable.renderables.components.button

import net.bewis09.bewisclient.drawable.Init
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.logic.TextAlign
import net.minecraft.network.chat.Component

class Button(p: Props<Button>) : TooltipHoverable<Button>(p + {
    shouldUsePointer = true
}) {
    lateinit var text: Component
    var onClick: (Button) -> Unit = {}
    var selected: (() -> Boolean)? = null
    var dark: Boolean = false
    var small: Boolean = false

    init { props() }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderButtonBackground(screenDrawing, hoverAnimation.get(), if (selected?.invoke() == true) 1f else 0f, x, y, width, height, 1f, dark, small)
    }

    override fun Init.init() {
        Text {
            textProvider = { this@Button.text }
            textAlign = TextAlign.CENTER
        }
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean = onClick(this).let { true }
}

fun Init.Button(p: RenderiteElement.Props<Button>): Button = net.bewis09.bewisclient.drawable.renderables.components.button.Button(p).also(::addRenderable)