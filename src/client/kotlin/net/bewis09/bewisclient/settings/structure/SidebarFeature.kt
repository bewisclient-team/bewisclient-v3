package net.bewis09.bewisclient.settings.structure

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.ThemeButtonElement
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.renderite.components.DivElement
import net.bewis09.renderite.logic.FitType
import net.bewis09.renderite.logic.LineType

abstract class SidebarFeature(id: Identifier, titleText: String) : Feature(id) {
    val title = Translation(id.namespace, "category.${id.path}", titleText)

    fun createButton() = ThemeButtonElement {
        text = title()
        selected = { OptionScreen.currentInstance?.category == id.toString() }
        onClick = { OptionScreen.currentInstance?.changeCategory(this@SidebarFeature) }
    }.updateHeight(SelectiveScreenDrawer.getSideButtonHeight()) as ThemeButtonElement

    abstract fun getRenderable(): Renderable

    fun createGrid(renderables: List<Renderable>): Renderable {
        return DivElement {
            initForEach(renderables) { it.add() }
            gap = 5
            minElementSize = 80
            lineType = LineType.SIZED
            fitType = FitType.SCROLL
        }
    }
}