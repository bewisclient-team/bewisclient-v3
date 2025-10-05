package net.bewis09.bewisclient.impl.functionalities

import net.bewis09.bewisclient.drawable.renderables.options_structure.ImageSettingCategory
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.functionalities.BetterVisibilitySettings

object BetterVisibility : ImageSettingCategory(
    "better_visibility", Translation("menu.category.better_visibility", "Better Visibility"), arrayOf(
        BetterVisibilitySettings.nether.createRenderable("better_visibility.nether", "Nether", "Improve visibility in the Nether dimension"),
        BetterVisibilitySettings.water.createRenderable("better_visibility.water", "Water", "Enhance visibility underwater"),
        BetterVisibilitySettings.lava.createRenderable("better_visibility.lava", "Lava", "Boost visibility in lava"),
        BetterVisibilitySettings.powder_snow.createRenderable("better_visibility.powder_snow", "Powder Snow", "Increase visibility in powder snow")
    ), BetterVisibilitySettings.enabled
)