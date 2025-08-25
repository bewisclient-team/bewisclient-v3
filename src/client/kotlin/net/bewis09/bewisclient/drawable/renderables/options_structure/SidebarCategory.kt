package net.bewis09.bewisclient.drawable.renderables.options_structure

import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.Button
import net.bewis09.bewisclient.drawable.renderables.Plane
import net.bewis09.bewisclient.drawable.renderables.VerticalScrollGrid
import net.bewis09.bewisclient.drawable.renderables.Text
import net.bewis09.bewisclient.game.Translation

open class SidebarCategory(val name: Translation, val renderable: Renderable) {
    constructor(name: Translation, settings: List<Renderable>) : this(name, VerticalScrollGrid({ settings.map { it.setHeight(90) } }, 5, 80))

    operator fun invoke(screen: OptionScreen): Button {
        return Button(name.getTranslatedString()) {
            screen.transformInside(
                getHeader(),
                renderable
            )
        }.setHeight(14) as Button
    }

    fun getHeader(): Renderable {
        return Plane { x, y, width, height -> listOf(Text(name.getTranslatedString(), centered = true)(x, y, width, 13)) }.setHeight(14)
    }
}