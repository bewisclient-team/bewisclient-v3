package net.bewis09.bewisclient.drawable.renderables.option_screen

import net.bewis09.bewisclient.drawable.OptionScreen
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.Button
import net.bewis09.bewisclient.drawable.renderables.VerticalScrollGrid
import net.bewis09.bewisclient.drawable.renderables.Text
import net.bewis09.bewisclient.game.Translation

open class SidebarCategory(val name: Translation, val renderable: Renderable) {
    constructor(name: Translation, settings: List<Renderable>): this(name, VerticalScrollGrid({ settings.map { it.setHeight(90) } }, 5, 80))

    operator fun invoke(screen: OptionScreen): Button {
        return Button(name.getTranslatedString()) {
            screen.transformInside(
                Text(name.getTranslatedString(), centered = true).setHeight(12),
                renderable
            )
        }.setHeight(14) as Button
    }
}