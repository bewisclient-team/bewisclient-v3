package net.bewis09.bewisclient.settings.structure

import net.bewis09.bewisclient.common.Identifier
import net.bewis09.bewisclient.common.createIdentifier
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.renderite.logic.Color

abstract class ImageFeature(id: Identifier, text: String) : CategorizedFeature(id, text) {
    constructor(id: String, text: String) : this(createIdentifier("bewisclient", id), text)

    val identifier = createIdentifier(id.namespace, "textures/gui/features/${id.path}.png")

    override fun createRenderable(): SettingCategory = object : SettingCategory() {
        override fun renderContent(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            val textHeight = (screenDrawing.wrapText(title.getTranslatedString(), width - 10).size - 1) * screenDrawing.getTextHeight()
            screenDrawing.drawCenteredWrappedText(title.getTranslatedString(), centerX, y2 - 27 - textHeight / 3 * 2, width - 10, if (isMinecrafty) Color.WHITE else General.getThemeColor(white = state.get() / 2))
            screenDrawing.drawTexture(identifier, centerX - 20, y + 14, 40, 40, if (isMinecrafty) Color.WHITE else General.getThemeColor(white = state.get()))
        }
    }
}