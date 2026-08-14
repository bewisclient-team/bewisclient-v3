package net.bewis09.bewisclient.features.utilities

import net.bewis09.bewisclient.common.color
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.settings.structure.ImageFeature
import net.bewis09.bewisclient.util.color.StaticColorSaver

object EntityHighlight : ImageFeature(createIdentifier("bewisclient","entity_highlight"), "Entity Highlight") {
    var color by color("color", StaticColorSaver(0xFF0000.color), "Color", quickSetting = true)
    var alpha by float("alpha", 0.31f, 0f, 1f, 0.01f, 2, "Transparency", quickSetting = true)
}