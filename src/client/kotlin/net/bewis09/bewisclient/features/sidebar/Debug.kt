package net.bewis09.bewisclient.features.sidebar

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.features.sidebar.General.animationTime
import net.bewis09.bewisclient.settings.structure.SidebarFeature
import net.bewis09.renderite.components.DivElement
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.FitType

object Debug: SidebarFeature(createIdentifier("bewisclient", "debug"), "Debug") {
    val elementHighlight = boolean("element_highlight", false)
    val elementBorders = boolean("element_borders", false)
    val showUpdates = boolean("show_updates", false)
    val highlightColor = color("highlight_color", Color.YELLOW)
    val highlightAlpha = float("highlight_alpha", 0.1f, 0f, 1f, 0.01f, 2)
    val borderColor = color("border_color", Color.YELLOW)
    val borderAlpha = float("border_alpha", 0.2f, 0f, 1f, 0.01f, 2)
    val updateColor = color("update_color", Color.RED)
    val updateAlpha = float("update_alpha", 0.1f, 0f, 1f, 0.01f, 2)
    val updateBorderColor = color("update_border_color", Color.RED)
    val updateBorderAlpha = float("update_border_alpha", 0.2f, 0f, 1f, 0.01f, 2)

    override fun getRenderable(): Renderable = DivElement {
        gap = 1
        fitType = FitType.SCROLL
        cacheChildren = true
        onInit = {
            animationTime.createRenderable(this@Debug, "animation_time", "Animation Time", "The time (in milliseconds) it takes for animations to complete").add()
            elementHighlight.createRenderable(this@Debug, "element_highlight", "Element Highlight").add()
            elementBorders.createRenderable(this@Debug, "element_borders", "Element Border").add()
            showUpdates.createRenderable(this@Debug, "show_updates", "Show Updates").add()
            highlightColor.createRenderableWithFader(this@Debug, "highlight_color", "Highlight Color", null, highlightAlpha).add()
            borderColor.createRenderableWithFader(this@Debug, "border_color", "Border Color", null, borderAlpha).add()
            updateColor.createRenderableWithFader(this@Debug, "update_color", "Update Color", null, updateAlpha).add()
            updateBorderColor.createRenderableWithFader(this@Debug, "update_border_color", "Update Border Color", null, updateBorderAlpha).add()
        }
    }
}