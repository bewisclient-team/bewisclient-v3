package net.bewis09.bewisclient.drawable.renderables.components.button

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.RenderiteElement
import net.bewis09.renderite.logic.Animator
import net.bewis09.renderite.logic.TextAlign
import net.bewis09.renderite.style.RenderiteChild
import net.minecraft.network.chat.Component

class ThemeButtonElement(p: Props<ThemeButtonElement>) : AbstractButtonElement<ThemeButtonElement>(p + {
    val oldClick = onClick
    onClick = {
        oldClick(this)
        if (!General.isMinecrafty) clickAnimation.set(0f) { set(1f) }
    }
    height = SelectiveScreenDrawer.getSideButtonHeight()
}) {
    lateinit var text: Component
    var selected: () -> Boolean = { false }

    init { props() }

    val clickAnimation: Animator = Animator({ General.animationDuration }, Animator.EASE_IN_OUT, 1f)
    val colorAnimation: Animator = Animator({ General.animationDuration }, Animator.EASE_IN_OUT, { if (selected()) 1f else 0f })

    override fun init() {
        Text {
            text = this@ThemeButtonElement.text
            color = General.getTextThemeColor()
            textAlign = TextAlign.CENTER
            animated = {
                fontSize = 8 + if (General.isMinecrafty) 1f else clickAnimation.get()
            }
        }
    }

    override fun renderBackground(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        SelectiveScreenDrawer.renderButtonBackground(screenDrawing, hoverFactor, colorAnimation.get(), x, y, width, height, clickAnimation.get())
    }
}

@RenderiteChild
fun Renderable.ThemeButton(p: RenderiteElement.Props<ThemeButtonElement>): ThemeButtonElement = ThemeButtonElement(p).add()