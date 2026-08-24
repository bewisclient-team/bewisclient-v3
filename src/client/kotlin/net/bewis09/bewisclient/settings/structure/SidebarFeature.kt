package net.bewis09.bewisclient.settings.structure

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.ThemeButton
import net.bewis09.bewisclient.drawable.renderables.components.structure.Grid
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.game.translations.Translation

abstract class SidebarFeature(id: Identifier, titleText: String) : Feature(id) {
    val title = Translation(id.namespace, "category.${id.path}", titleText)

    fun createButton(): ThemeButton {
        return ThemeButton {
            text = title()
            selected = { OptionScreen.currentInstance?.category == id.toString() }
            onClick = { OptionScreen.currentInstance?.changeCategory(this@SidebarFeature) }
        }.updateHeight(SelectiveScreenDrawer.getSideButtonHeight()) as ThemeButton
    }

    abstract fun getRenderable(): Renderable

    fun createGrid(renderables: List<Renderable>): Renderable {
        return Grid {
            init = { renderables.map { it.updateHeight(90) } }
            gap = 5
            minElementSize = 80
            lineType = Grid.LineType.SIZED
            fitType = Grid.FitType.SCROLL
        }
    }
}