package net.bewis09.bewisclient.features.utilities

import net.bewis09.bewisclient.settings.structure.ImageFeature

object Scoreboard : ImageFeature("scoreboard", "Scoreboard") {
    val scale by float("scale", 1.0f, 0.5f, 2.0f, 0.01f, 2) menuQuick "Scale"
}