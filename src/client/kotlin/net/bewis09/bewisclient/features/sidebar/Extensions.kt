package net.bewis09.bewisclient.features.sidebar

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.components.structure.Grid
import net.bewis09.bewisclient.drawable.renderables.impl.ExtensionListRenderable
import net.bewis09.bewisclient.settings.structure.SidebarFeature

object Extensions : SidebarFeature(createIdentifier("bewisclient", "extensions"), "Extensions") {
    val extensions = Grid {
        gap = 1
        fitType = Grid.FitType.SCROLL
        children = APIEntrypointLoader.mapContainer {
            ExtensionListRenderable {
                modContainer = it.provider
                entrypoint = it.entrypoint
            }
        }
    }

    override fun getRenderable(): Renderable = extensions
}