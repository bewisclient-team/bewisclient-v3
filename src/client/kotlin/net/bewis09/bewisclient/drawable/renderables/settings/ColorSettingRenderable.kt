package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.renderables.components.button.ColorInfoButton
import net.bewis09.bewisclient.drawable.renderables.components.button.ColorInfoButtonElement
import net.bewis09.bewisclient.drawable.renderables.components.button.ResetButton
import net.bewis09.bewisclient.drawable.renderables.components.button.ResetButtonElement
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.types.Setting
import net.bewis09.bewisclient.util.color.ColorSaver

class ColorSettingRenderable(p: Props<ColorSettingRenderable>) : SettingRenderable<ColorSettingRenderable>(p + {
    height = 22
}) {
    lateinit var title: Translation
    lateinit var setting: Setting<ColorSaver>
    lateinit var types: Array<String>

    init { props() }

    val colorInfoButton = ColorInfoButtonElement {
        state = { setting.get() }
        onChange = setting::set
        types = this@ColorSettingRenderable.types
    }

    val resetButton = ResetButtonElement {
        settable = this@ColorSettingRenderable.setting
        isDefault = { this@ColorSettingRenderable.setting.isDefault() }
    }

    override fun Init.init() {
        ResetButton {
            settable = this@ColorSettingRenderable.setting
            isDefault = { this@ColorSettingRenderable.setting.isDefault() }
        }.updatePosition(x2 - resetButton.width - 4, y + 4)
        ColorInfoButton {
            state = { setting.get() }
            onChange = setting::set
            types = this@ColorSettingRenderable.types
        }.updatePosition(x2 - colorInfoButton.width - 8 - resetButton.width, y + 4)
        SettingText {
            textProvider = { title() }
        }
    }
}