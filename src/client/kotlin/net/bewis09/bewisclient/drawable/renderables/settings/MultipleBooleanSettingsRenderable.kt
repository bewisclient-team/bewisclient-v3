package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.renderables.components.button.ResetButton
import net.bewis09.bewisclient.drawable.renderables.components.setting.Switch
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.settings.logic.SettingInterfaceWithDefault
import net.bewis09.bewisclient.settings.structure.Feature
import net.bewis09.renderite.logic.alpha
import net.minecraft.network.chat.Component

class MultipleBooleanSettingsRenderable(
    val title: Translation, tooltip: Translation? = null, val settings: () -> List<Part>
) : SettingRenderable(tooltip, 22) {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.drawCenteredText(title.getTranslatedString(), centerX, y + 6, General.getTextThemeColor())
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        super.init()
        var yOffset = 18
        for (setting in settings()) {
            val renderable = setting.setPosition(x, y + 4 + yOffset).setWidth(width)
            addRenderable(renderable)
            yOffset += renderable.height + 2
        }
        internalHeight = yOffset + 4
    }

    class Part(
        val name: Component, tooltip: Component? = null, val setting: SettingInterfaceWithDefault<Boolean>
    ) : TooltipHoverable(tooltip) {
        val switch = Switch(
            state = setting::get,
            onChange = setting::set,
        )

        init {
            internalHeight = 17
        }

        val resetButton = ResetButton(setting) { setting.get() == setting.getDefault() }

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            super.render(screenDrawing, mouseX, mouseY)
            screenDrawing.drawHorizontalLine(x + 5, y - 2, width - 10, 0xAAAAAA alpha 0.2F)
            screenDrawing.drawText(name, x + 8, screenDrawing.getTextYCenter(this), General.getTextThemeColor())
            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            super.init()
            addRenderable(resetButton.setPosition(x2 - resetButton.width - 4, y + 1))
            addRenderable(switch.setPosition(x2 - switch.width - 8 - resetButton.width, y + 2))
        }
    }

    companion object {
        fun create(feature: Feature, id: String, title: String, description: String? = null, settings: List<Part>): MultipleBooleanSettingsRenderable {
            return MultipleBooleanSettingsRenderable(Translation(id, title), description?.let { feature.createTranslation("$id.description", it) }) { settings }
        }

        fun create(feature: Feature, id: String, title: String, description: String? = null, settings: () -> List<Part>): MultipleBooleanSettingsRenderable {
            return MultipleBooleanSettingsRenderable(feature.createTranslation(id, title), description?.let { feature.createTranslation("$id.description", it) }, settings)
        }
    }
}