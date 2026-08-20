package net.bewis09.bewisclient.features.utilities

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.settings.structure.ImageFeature
import net.bewis09.renderite.logic.Color

object BlockHighlight : ImageFeature(createIdentifier("bewisclient", "block_highlight"), "Block Highlight") {
    var color by color("color", Color.BLACK) menuQuick "Color"
    var thickness by float("thickness", 0.4f, 0f, 1f, 0.01f, 2) menuQuick "Thickness"
}