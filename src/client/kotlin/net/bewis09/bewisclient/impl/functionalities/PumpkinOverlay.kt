package net.bewis09.bewisclient.impl.functionalities

import net.bewis09.bewisclient.drawable.renderables.options_structure.ImageSettingCategory
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.functionalities.PumpkinOverlaySettings

object PumpkinOverlay : ImageSettingCategory(
    "pumpkin_overlay", Translation("menu.category.pumpkin_overlay", "Pumpkin Overlay"), arrayOf(), PumpkinOverlaySettings.enabled
)