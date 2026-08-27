package net.bewis09.bewisclient.settings.structure

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.logic.Color
import net.bewis09.renderite.logic.TextAlign

abstract class ImageFeature(id: Identifier, text: String) : CategorizedFeature(id, text) {
    constructor(id: String, text: String) : this(createIdentifier("bewisclient", id), text)

    val identifier = createIdentifier(id.namespace, "textures/gui/features/${id.path}.png")

    override fun createRenderable(): SettingCategory = object : SettingCategory() {
        override fun renderElement(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            screenDrawing.drawTexture(identifier, centerX - 20, y + 14 - if(isMinecrafty) 3 else 0, 40, 40, if (isMinecrafty) Color.WHITE else General.getThemeColor(white = state.get()))
        }

        override fun Init.init() {
            EnableButton()
            Text {
                wrap = true
                text = title()
                overflowVisible = true
                textAlign = TextAlign.CENTER
            }(x + 5, y2 - 25 - if(isMinecrafty) 3 else 0, width - 10, 0)
        }
    }
}