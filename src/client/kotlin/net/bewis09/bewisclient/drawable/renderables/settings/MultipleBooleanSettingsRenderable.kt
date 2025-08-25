package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.ScreenDrawing
import net.bewis09.bewisclient.drawable.Translations
import net.bewis09.bewisclient.drawable.renderables.ImageButton
import net.bewis09.bewisclient.drawable.renderables.Switch
import net.bewis09.bewisclient.drawable.renderables.TooltipHoverable
import net.bewis09.bewisclient.game.Translation
import net.bewis09.bewisclient.interfaces.Gettable
import net.bewis09.bewisclient.interfaces.Settable
import net.minecraft.util.Identifier

class MultipleBooleanSettingsRenderable(
    val title: Translation, tooltip: Translation? = null, val settings: () -> List<Part<*>>
) : TooltipHoverable(tooltip) {
    override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
        super.render(screenDrawing, mouseX, mouseY)
        screenDrawing.fillRounded(getX(), getY(), getWidth(), getHeight(), 5, 0xFFFFFF, hoverAnimation["hovering"] * 0.1f + 0.05f)
        screenDrawing.push()
        screenDrawing.drawCenteredText(title.getTranslatedString(), getX() + getWidth() / 2, getY() + 6, 0xFFFFFF, 1.0F)
        screenDrawing.pop()
        renderRenderables(screenDrawing, mouseX, mouseY)
    }

    override fun init() {
        super.init()
        var yOffset = 18
        for (setting in settings()) {
            val renderable = setting.setPosition(getX(), getY() + 4 + yOffset).setWidth(getWidth())
            addRenderable(renderable)
            yOffset += renderable.getHeight() + 2
        }
        height = yOffset.toUInt() + 4u
    }

    class Part<T>(
        val name: Translation, tooltip: Translation? = null, val setting: T
    ) : TooltipHoverable(tooltip) where T : Settable<Boolean?>, T : Gettable<Boolean> {
        val switch = Switch(
            state = setting::get,
            onChange = setting::set,
        )

        init {
            height = 16u
        }

        val resetButton = ImageButton(Identifier.of("bewisclient", "textures/gui/sprites/reset.png"), {
            setting.set(null)
        }, Translations.RESET).setImagePadding(2).setSize(14, 14)

        override fun render(screenDrawing: ScreenDrawing, mouseX: Int, mouseY: Int) {
            super.render(screenDrawing, mouseX, mouseY)
            screenDrawing.drawHorizontalLine(getX() + 5, getY() - 2, getWidth() - 10, 0xAAAAAA, 0.2F)
            screenDrawing.push()
            screenDrawing.translate(0f, getHeight() / 2f - screenDrawing.getTextHeight() / 2f)
            screenDrawing.drawText(name.getTranslatedString(), getX() + 8, getY(), 0xFFFFFF, 1.0F)
            screenDrawing.pop()
            renderRenderables(screenDrawing, mouseX, mouseY)
        }

        override fun init() {
            super.init()
            addRenderable(resetButton.setPosition(getX() + getWidth() - resetButton.getWidth() - 4, getY() + 1))
            addRenderable(switch.setPosition(getX() + getWidth() - switch.getWidth() - 8 - resetButton.getWidth(), getY() + 2))
        }
    }

    companion object {
        fun create(id: String, title: String, description: String? = null, settings: List<Part<*>>): MultipleBooleanSettingsRenderable {
            return MultipleBooleanSettingsRenderable(Translation("menu.$id", title), description?.let { Translation("menu.$id.description", it) }) { settings }
        }

        fun create(id: String, title: String, description: String? = null, settings: () -> List<Part<*>>): MultipleBooleanSettingsRenderable {
            return MultipleBooleanSettingsRenderable(Translation("menu.$id", title), description?.let { Translation("menu.$id.description", it) }, settings)
        }
    }
}