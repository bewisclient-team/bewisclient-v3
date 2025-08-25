package net.bewis09.bewisclient.impl.functionalities.entity_highlight

import net.bewis09.bewisclient.drawable.renderables.options_structure.ImageSettingCategory
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.functionalities.EntityHighlightSettings

object EntityHighlight : ImageSettingCategory(
    "entity_highlight", Translation("menu.category.entity_highlight", "Entity Highlight"), arrayOf(
        EntityHighlightSettings.color.createRenderable("entity_highlight.color", "Color", "Change the color of the entity highlight"),
        EntityHighlightSettings.alpha.createRenderable("entity_highlight.alpha", "Alpha", "Adjust the alpha of the entity highlight"),
    ), EntityHighlightSettings.enabled
) {
}