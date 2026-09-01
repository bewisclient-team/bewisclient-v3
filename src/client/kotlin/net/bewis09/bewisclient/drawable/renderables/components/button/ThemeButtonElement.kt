package net.bewis09.bewisclient.drawable.renderables.components.button

import net.bewis09.bewisclient.drawable.Init
import net.bewis09.renderite.logic.Animator
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.renderite.drawer.transform
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.RenderiteElement
import net.minecraft.network.chat.Component

class ThemeButtonElement(p: Props<ThemeButtonElement>) : AbstractButtonElement<ThemeButtonElement>(p + {
    val oldClick = onClick
    onClick = {
        if (!General.isMinecrafty) colorAnimation.set(1f)
        oldClick(this)
        if (!General.isMinecrafty) clickAnimation.set(0f) { set(1f) }
    }
}) {
    lateinit var text: Component
    var selected: () -> Boolean = { false }

    init { props() }

    val clickAnimation: Animator = Animator({ General.animationDuration }, Animator.EASE_IN_OUT, 1f)
    val colorAnimation: Animator = Animator({ General.animationDuration }, Animator.EASE_IN_OUT, 0f)

    override fun renderLogic(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.renderLogic(screenDrawing, mouseX, mouseY)
        colorAnimation.set(if (selected()) 1f else 0f)
    }

    override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val click = if (General.isMinecrafty) 1f else clickAnimation.get()

        screenDrawing.transform(exactCenterX, exactCenterY, 0.95f + 0.05f * click, 0.95f + 0.05f * click) {
            screenDrawing.drawCenteredText(text, 0, screenDrawing.getTextHeight() / -2f, General.getTextThemeColor())
        }
    }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        val click = if (General.isMinecrafty) 1f else clickAnimation.get()
        SelectiveScreenDrawer.renderButtonBackground(screenDrawing, hoverFactor, colorAnimation.get(), x, y, width, height, click)
    }
}

fun Init.ThemeButton(p: RenderiteElement.Props<ThemeButtonElement>): ThemeButtonElement = ThemeButtonElement(p).add()