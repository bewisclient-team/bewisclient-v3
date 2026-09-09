package net.bewis09.bewisclient.drawable.renderables.components.button

import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.screen.OptionScreen
import net.bewis09.bewisclient.settings.structure.SidebarFeature
import net.bewis09.renderite.RenderiteElement

fun Renderable.SidebarButton(sidebarFeature: SidebarFeature, p: RenderiteElement.Props<ThemeButtonElement> = {}) {
    ThemeButton {
        text = sidebarFeature.title()
        selected = { OptionScreen.currentInstance?.category == sidebarFeature.id.toString() }
        onClick = { OptionScreen.currentInstance?.changeCategory(sidebarFeature) }
        p()
    }
}