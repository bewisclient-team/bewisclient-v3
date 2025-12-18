package net.bewis09.bewisclient.impl.functionalities

import net.bewis09.bewisclient.drawable.renderables.options_structure.ImageSettingCategory
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.functionalities.PackAdderSettings

object PackAdder : ImageSettingCategory(
    "pack_adder", Translation("menu.category.pack_adder", "Pack Adder"), arrayOf(), PackAdderSettings.enabled
)