package net.bewis09.bewisclient.drawable.renderables.components.button

import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipElement
import net.bewis09.bewisclient.util.Bewisclient

abstract class AbstractButtonElement<T: AbstractButtonElement<T>>(p: Props<T>): TooltipElement<T>(p + {
    shouldUsePointer = true
}) {
    var onClick: (T) -> Unit = {}

    @Suppress("UNCHECKED_CAST")
    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        Bewisclient.playClickSound()
        return (this as T?)?.let { onClick(it) }.let { true }
    }
}