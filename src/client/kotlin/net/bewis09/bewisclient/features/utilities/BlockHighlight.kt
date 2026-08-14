package net.bewis09.bewisclient.features.utilities

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.settings.structure.ImageFeature
import net.bewis09.bewisclient.util.color.StaticColorSaver

object BlockHighlight : ImageFeature(createIdentifier("bewisclient", "block_highlight"), "Block Highlight") {
    var color by color("color", StaticColorSaver(0f, 0f, 0f), "Color", quickSetting = true)
    var thickness by float("thickness", 0.4f, 0f, 1f, 0.01f, 2, "Thickness", quickSetting = true)
}