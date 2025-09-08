package net.bewis09.bewisclient.impl.functionalities

import net.bewis09.bewisclient.drawable.renderables.options_structure.ImageSettingCategory
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.functionalities.ShulkerBoxTooltipSettings

object ShulkerBoxTooltip : ImageSettingCategory(
    "shulker_box_tooltip", Translation("menu.category.shulker_box_tooltip", "Shulker Box Tooltip"), arrayOf(), ShulkerBoxTooltipSettings.enabled
)