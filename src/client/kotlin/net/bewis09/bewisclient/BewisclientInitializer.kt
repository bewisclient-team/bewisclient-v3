package net.bewis09.bewisclient

import net.bewis09.bewisclient.features.sidebar.Debug
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.util.logic.ClientInterface
import net.bewis09.renderite.Renderite
import net.fabricmc.api.ClientModInitializer

object BewisclientInitializer : ClientInterface, ClientModInitializer {
    override fun onInitializeClient() {
        EventEntrypoint.registerEntrypoints()
        EventEntrypoint.onAllEventEntrypoints(EventEntrypoint::onInitializeClient)

        Renderite.debugHighlightEnabled = { Debug.elementHighlight() }
        Renderite.debugBorderEnabled = { Debug.elementBorders() }
        Renderite.debugHighlightColor = { Debug.highlightColor.get().getColor() alpha Debug.highlightAlpha.get() }
        Renderite.debugBorderColor = { Debug.highlightBorderColor.get().getColor() alpha Debug.highlightBorderAlpha.get() }
        Renderite.debugUpdateHighlightColor = { Debug.updateColor.get().getColor() alpha Debug.updateAlpha.get() }
        Renderite.debugUpdateBorderColor = { Debug.updateColor.get().getColor() alpha Debug.updateAlpha.get() }
        Renderite.hoverTime = { General.animationTime.get().toLong() }
    }
}