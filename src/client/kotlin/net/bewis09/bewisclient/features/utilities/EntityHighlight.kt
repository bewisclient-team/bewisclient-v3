package net.bewis09.bewisclient.features.utilities

import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.settings.structure.ImageFeature

object EntityHighlight : ImageFeature(createIdentifier("bewisclient","entity_highlight"), "Entity Highlight") {
    var color by color("color", !0xFF0000) menuQuick "Color"
    var alpha by float("alpha", 0.31f, 0f, 1f, 0.01f, 2) menuQuick "Transparency"
}