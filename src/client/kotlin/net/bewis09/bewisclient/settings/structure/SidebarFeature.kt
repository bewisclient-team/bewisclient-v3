package net.bewis09.bewisclient.settings.structure

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.ThemeButton
import net.bewis09.bewisclient.drawable.renderables.components.structure.VerticalScrollGrid
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.game.translations.Translation

abstract class SidebarFeature(id: Identifier, titleText: String): Feature(id) {
    val title = Translation(id.namespace, "category.${id.path}", titleText)

    fun createButton(): ThemeButton {
        return ThemeButton(title(), { OptionScreen.currentInstance?.category == id.toString() }, {
            OptionScreen.currentInstance?.changeCategory(this)
        }).setHeight(SelectiveScreenDrawer.getSideButtonHeight()) as ThemeButton
    }

    abstract fun getRenderable(): Renderable

    fun createGrid(renderables: List<Renderable>): Renderable {
        return VerticalScrollGrid({ renderables.map { it.setHeight(90) } }, 5, 80)
    }
}