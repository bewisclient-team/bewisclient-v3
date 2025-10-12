package net.bewis09.bewisclient.impl.functionalities

import net.bewis09.bewisclient.drawable.renderables.options_structure.ImageSettingCategory
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.functionalities.FireHeightSettings

object FireHeight: ImageSettingCategory(
    "fire_height", Translation("menu.category.fire_height", "Fire Height"), arrayOf(
        FireHeightSettings.height.createRenderable(
            "fire_height.height", "Fire Height", "Adjust the height of the fire overlay on your screen"
        ),
    ), FireHeightSettings.enabled
)