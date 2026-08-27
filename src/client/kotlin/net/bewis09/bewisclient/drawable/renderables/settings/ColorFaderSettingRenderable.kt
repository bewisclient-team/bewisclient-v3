package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.renderables.components.button.ColorInfoButton
import net.bewis09.bewisclient.drawable.renderables.components.button.ResetButton
import net.bewis09.bewisclient.drawable.renderables.components.setting.Fader
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.types.FloatSetting
import net.bewis09.bewisclient.settings.types.Setting
import net.bewis09.bewisclient.util.color.ColorSaver
import net.bewis09.renderite.logic.TextAlign

class ColorFaderSettingRenderable(p: Props<ColorFaderSettingRenderable>) : SettingRenderable<ColorFaderSettingRenderable>(p + {
    height = 35
}) {
    lateinit var title: Translation
    lateinit var setting: Setting<ColorSaver>
    lateinit var types: Array<String>
    lateinit var setting2: FloatSetting
    lateinit var title2: Translation

    init {
        props()
    }

    override fun Init.init() {
        ResetButton<Nothing> {
            settable = {
                setting.set(null)
                setting2.set(null)
            }
            isDefault = {
                setting.isDefault() && setting2.isDefault()
            }
        }.updatePosition(x2 - 18, y + 4)
        ColorInfoButton {
            state = { setting.get() }
            onChange = setting::set
            types = this@ColorFaderSettingRenderable.types
        }.updatePosition(x2 - 182, y + 4)
        Fader {
            value = { setting2.get() }
            onChange = setting2::set
            precision = setting2.precision
        }.updatePosition(x2 - 122, y + 20 - if (General.isMinecrafty) 1 else 0)
        Text {
            textProvider = { (title2.getTranslatedString() + ": " + setting2.get()).toText() }
            textAlign = TextAlign.END
            overflowVisible = true
        }(x2 - 126, y + 22 - if (General.isMinecrafty) 1 else 0, 0, 10)
        SettingText {
            textProvider = { title() }
        }
    }
}