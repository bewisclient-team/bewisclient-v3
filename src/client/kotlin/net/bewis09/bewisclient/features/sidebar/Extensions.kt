package net.bewis09.bewisclient.features.sidebar

import net.bewis09.bewisclient.api.APIEntrypointLoader
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.renderite.components.Div
import net.bewis09.bewisclient.drawable.renderables.impl.ExtensionListRenderable
import net.bewis09.bewisclient.settings.structure.SidebarFeature
import net.bewis09.renderite.logic.FitType

object Extensions : SidebarFeature(createIdentifier("bewisclient", "extensions"), "Extensions") {
    override fun getRenderable(): Renderable = Div {
        gap = 1
        fitType = FitType.SCROLL
        onInit = {
            APIEntrypointLoader.mapContainer {
                ExtensionListRenderable {
                    modContainer = it.provider
                    entrypoint = it.entrypoint
                }.add()
            }
        }
    }
}