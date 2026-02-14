package net.bewis09.bewisclient.drawable.renderables.options_structure

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.Plane
import net.bewis09.bewisclient.drawable.renderables.TextElement
import net.bewis09.bewisclient.drawable.renderables.ThemeButton
import net.bewis09.bewisclient.drawable.renderables.VerticalScrollGrid
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.minecraft.util.Identifier

open class SidebarCategory(val id: Identifier, val name: Translation, val renderable: Renderable) {
    constructor(id: Identifier, name: String, renderable: Renderable) : this(id, Translation(id.namespace, "menu.category."+id.path, name), renderable)
    constructor(id: Identifier, name: String, settings: List<Renderable>) : this(id, name, VerticalScrollGrid({ settings.map { it.setHeight(90) } }, 5, 80))

    operator fun invoke(screen: OptionScreen): ThemeButton {
        return ThemeButton(id.toString(), name(), screen.category) {
            screen.openPage(
                getHeader(), renderable, null, true
            )
        }.setHeight(14) as ThemeButton
    }

    fun getHeader(): Renderable {
        return Plane { x, y, width, height -> listOf(TextElement(name(), { OptionsMenuSettings.getTextThemeColor() }, centered = true)(x, y, width, 13)) }.setHeight(14)
    }
}