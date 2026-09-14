package net.bewis09.bewisclient.drawable

import com.mojang.blaze3d.platform.InputConstants
import net.bewis09.bewisclient.features.sidebar.Debug
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.util.BewisclientDataGenerator
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.renderite.Renderite

object RenderiteData: EventEntrypoint {
    override fun onInitializeClient() {
        Renderite.debugHighlightEnabled = { Debug.elementHighlight() }
        Renderite.debugBorderEnabled = { Debug.elementBorders() }
        Renderite.debugUpdateHighlightEnabled = { Debug.showUpdates() }
        Renderite.debugHighlightColor = { Debug.highlightColor.get().getColor() alpha Debug.highlightAlpha.get() }
        Renderite.debugBorderColor = { Debug.highlightBorderColor.get().getColor() alpha Debug.highlightBorderAlpha.get() }
        Renderite.debugUpdateHighlightColor = { Debug.updateColor.get().getColor() alpha Debug.updateAlpha.get() }
        Renderite.debugUpdateBorderColor = { Debug.updateColor.get().getColor() alpha Debug.updateAlpha.get() }
        Renderite.hoverTime = { General.animationTime.get().toLong() }
        Renderite.isStructureGenerating = { BewisclientDataGenerator.datagenEnabled }
        Renderite.mainMouseButtonCode = { InputConstants.MOUSE_BUTTON_LEFT }
    }
}