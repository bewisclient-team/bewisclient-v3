package net.bewis09.bewisclient.settings.structure

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.TextAlign

abstract class DescriptionFeature(id: Identifier, title: String, descriptionText: String) : CategorizedFeature(id, title) {
    val description = Translation(id.namespace, "category.${id.path}.description", descriptionText)

    override fun createRenderable() = object : SettingCategory() {
        override fun Init.init() {
            EnableButton()
            Text {
                text = title()
                wrap = true
                overflowVisible = true
                color = if (isMinecrafty) Color.WHITE else General.getThemeColor(state.get() / 2)
                textAlign = TextAlign.CENTER
            }(x + 5, y + 18 - if(isMinecrafty) 2 else 0, width - 10, 0)
            Text {
                text = description()
                wrap = true
                overflowVisible = true
                color = if (isMinecrafty) Color.WHITE alpha 0.65f else General.getThemeColor(state.get() / 2, 0.65f)
                textAlign = TextAlign.CENTER
            }(x + 5, y2 - 38 - if(isMinecrafty) 3 else 0, width - 10, 0)
        }
    }
}