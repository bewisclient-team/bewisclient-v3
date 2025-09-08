package net.bewis09.bewisclient.impl.functionalities

import net.bewis09.bewisclient.drawable.renderables.options_structure.ImageSettingCategory
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.functionalities.ScoreboardSettings

object Scoreboard : ImageSettingCategory(
    "scoreboard", Translation("menu.category.scoreboard", "Scoreboard"), arrayOf(
        ScoreboardSettings.scale.createRenderable("scoreboard.scale", "Scale", "Adjust the size of the scoreboard")
    ), ScoreboardSettings.enabled
)