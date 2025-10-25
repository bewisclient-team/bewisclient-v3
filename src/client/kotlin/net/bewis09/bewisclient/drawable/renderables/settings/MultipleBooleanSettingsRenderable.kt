package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.renderables.*
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.drawable.screen_drawing.translate
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.impl.settings.OptionsMenuSettings
import net.bewis09.bewisclient.interfaces.SettingInterface
import net.bewis09.bewisclient.util.color.*
import net.minecraft.text.Text

class MultipleBooleanSettingsRenderable(
    val title: Translation, tooltip: Translation? = null, val settings: () -> List<Part>
) : SettingRenderable(tooltip, 22) {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.drawCenteredText(title.getTranslatedString(), centerX, y + 6, 0.5f within (Color.WHITE to OptionsMenuSettings.themeColor.get().getColor()))
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
        val name: Translation, tooltip: Text? = null, val setting: SettingInterface<Boolean>
    ) : TooltipHoverable(tooltip) {
        val switch = Switch(
            state = setting::get,
            onChange = setting::set,
        )

        init {
            internalHeight = 16
        }

        val resetButton = ResetButton(setting)

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            super.render(screenDrawing, mouseX, mouseY)
            screenDrawing.drawHorizontalLine(x + 5, y - 2, width - 10, 0xAAAAAA alpha 0.2F)
            screenDrawing.translate(0f, height / 2f - screenDrawing.getTextHeight() / 2f) {
                screenDrawing.drawText(name.getTranslatedString(), x + 8, y, 0.5f within (Color.WHITE to OptionsMenuSettings.themeColor.get().getColor()))
            }
            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            super.init()
            addRenderable(resetButton.setPosition(x2 - resetButton.width - 4, y + 1))
            addRenderable(switch.setPosition(x2 - switch.width - 8 - resetButton.width, y + 2))
        }
    }

    companion object {
        fun create(id: String, title: String, description: String? = null, settings: List<Part>): MultipleBooleanSettingsRenderable {
            return MultipleBooleanSettingsRenderable(Translation("menu.$id", title), description?.let { Translation("menu.$id.description", it) }) { settings }
        }

        fun create(id: String, title: String, description: String? = null, settings: () -> List<Part>): MultipleBooleanSettingsRenderable {
            return MultipleBooleanSettingsRenderable(Translation("menu.$id", title), description?.let { Translation("menu.$id.description", it) }, settings)
        }
    }
}