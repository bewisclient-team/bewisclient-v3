package net.bewis09.bewisclient.drawable.renderables.options_structure

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.Plane
import net.bewis09.bewisclient.drawable.renderables.TextElement
import net.bewis09.bewisclient.drawable.renderables.ThemeButton
import net.bewis09.bewisclient.drawable.renderables.VerticalScrollGrid
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.color.Color
import net.bewis09.bewisclient.logic.color.within

open class SidebarCategory(val name: Translation, val renderable: Renderable) {
    constructor(name: Translation, settings: List<Renderable>) : this(name, VerticalScrollGrid({ settings.map { it.setHeight(90) } }, 5, 80))

    operator fun invoke(screen: OptionScreen): ThemeButton {
        return ThemeButton(name.getTranslatedString(), screen.clickedButton) {
            screen.transformInside(
                getHeader(), renderable
            )
        }.setHeight(14) as ThemeButton
    }

    fun getHeader(): Renderable {
        return Plane { x, y, width, height -> listOf(TextElement(name.getTranslatedString(), { 0.5f within (Color.WHITE to OptionsMenuSettings.themeColor.get().getColor()) }, centered = true)(x, y, width, 13)) }.setHeight(14)
    }
}