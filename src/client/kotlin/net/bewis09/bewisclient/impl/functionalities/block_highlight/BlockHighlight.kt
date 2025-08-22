package net.bewis09.bewisclient.impl.functionalities.block_highlight

import net.bewis09.bewisclient.drawable.renderables.options_structure.ImageSettingCategory
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.functionalities.BlockHighlightSettings

object BlockHighlight: ImageSettingCategory(
    "block_highlight", Translation("menu.category.block_highlight", "Block Highlight"), arrayOf(
        BlockHighlightSettings.color.createRenderable("block_highlight.color", "Color", "Change the color of the block highlight"),
        BlockHighlightSettings.thickness.createRenderable("block_highlight.thickness", "Thickness", "Adjust the thickness of the block highlight"),
    ), BlockHighlightSettings.enabled
)