package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.renderables.components.button.ResetButton
import net.bewis09.bewisclient.drawable.renderables.components.element.Rectangle
import net.bewis09.bewisclient.drawable.renderables.components.element.Text
import net.bewis09.bewisclient.drawable.renderables.components.logic.TextAlign
import net.bewis09.bewisclient.drawable.renderables.components.setting.Switch
import net.bewis09.bewisclient.drawable.renderables.components.logic.TooltipHoverable
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.logic.SettingInterfaceWithDefault
import net.bewis09.bewisclient.settings.structure.Feature
import net.bewis09.renderite.logic.alpha
import net.minecraft.network.chat.Component

class MultipleBooleanSettingsRenderable(p: Props<MultipleBooleanSettingsRenderable>) : SettingRenderable<MultipleBooleanSettingsRenderable>(p + { height = 22 }) {
    lateinit var title: Translation
    lateinit var settings: List<Part>

    init { props() }

    override fun init() {
        super.init()
        var yOffset = 18
        for (setting in settings) {
            val renderable = setting.updatePosition(x, y + 4 + yOffset).updateWidth(width)
            addRenderable(renderable)
            yOffset += renderable.height + 2
        }
        height = yOffset + 4
        addRenderable(Text {
            text = this@MultipleBooleanSettingsRenderable.title()
            verticalAlign = TextAlign.START
            textAlign = TextAlign.CENTER
        }(x, y + 6, width, 0))
    }

    class Part(p: Props<Part>) : TooltipHoverable<Part>(p + {
        height = 17
        minWidth = 10
        overflowVisible = true
    }) {
        lateinit var title: Component
        lateinit var setting: SettingInterfaceWithDefault<Boolean>

        init { props() }

        val switch = Switch {
            state = { setting.get() }
            onChange = setting::set
        }

        val resetButton = ResetButton {
            settable = setting
            isDefault = { setting.get() == setting.getDefault() }
        }

        override fun init() {
            super.init()
            addRenderable(resetButton.updatePosition(x2 - resetButton.width - 4, y + 1))
            addRenderable(switch.updatePosition(x2 - switch.width - 8 - resetButton.width, y + 2))
            addRenderable(Rectangle { color = 0xAAAAAA alpha 0.2F }(x + 5, y - 2, width - 10, 1))
            addRenderable(Text { text = title }(x + 8, y, 0, height))
        }
    }

    companion object {
        fun create(feature: Feature, id: String, title: String, description: String? = null, settings: List<Part>): MultipleBooleanSettingsRenderable {
            return MultipleBooleanSettingsRenderable {
                this.title = feature.createTranslation(id, title)
                this.tooltip = description?.let { feature.createTranslation("$id.description", it)() }
                this.settings = settings
            }
        }
    }
}