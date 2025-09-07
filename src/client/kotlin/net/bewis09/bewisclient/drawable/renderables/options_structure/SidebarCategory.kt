package net.bewis09.bewisclient.drawable.renderables.options_structure

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.Plane
import net.bewis09.bewisclient.drawable.renderables.Text
import net.bewis09.bewisclient.drawable.renderables.ThemeButton
import net.bewis09.bewisclient.drawable.renderables.VerticalScrollGrid
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.logic.within

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
        return Plane { x, y, width, height -> listOf(Text(name.getTranslatedString(), { 0.5f within (0xFFFFFF to OptionsMenuSettings.themeColor.get().getColor()) or 0xFF000000.toInt() }, centered = true)(x, y, width, 13)) }.setHeight(14)
    }
}