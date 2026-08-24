package net.bewis09.bewisclient.drawable.renderables.components.button

import net.bewis09.bewisclient.drawable.Animator
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.drawer.transform
import net.bewis09.bewisclient.features.sidebar.General
import net.minecraft.network.chat.Component

class ThemeButton(p: Props<ThemeButton>) : TooltipHoverable<ThemeButton>(p) {
    lateinit var text: Component
    var selected: () -> Boolean = { false }
    var onClick: (ThemeButton) -> Unit = {}

    init { props() }

    val clickAnimation: Animator = Animator({ General.animationDuration }, Animator.EASE_IN_OUT, 1f)
    val colorAnimation: Animator = Animator({ General.animationDuration }, Animator.EASE_IN_OUT, 0f)

    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        colorAnimation.set(if (selected()) 1f else 0f)
        val click = if (General.isMinecrafty) 1f else clickAnimation.get()
        SelectiveScreenDrawer.renderButtonBackground(screenDrawing, hoverFactor, colorAnimation.get(), x, y, width, height, click, mouseX, mouseY)

        usePointer(screenDrawing, mouseX, mouseY)

        screenDrawing.transform(exactCenterX, exactCenterY, 0.95f + 0.05f * click, 0.95f + 0.05f * click) {
            screenDrawing.drawCenteredText(text, 0, screenDrawing.getTextHeight() / -2f, General.getTextThemeColor())
        }
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!General.isMinecrafty)
            colorAnimation.set(1f)
        onClick(this)
        if (!General.isMinecrafty)
            clickAnimation.set(0f) { set(1f) }
        return true
    }
}