package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.renderables.components.button.ColorInfoButton
import net.bewis09.bewisclient.drawable.renderables.components.button.ResetButton
import net.bewis09.bewisclient.drawable.renderables.components.element.Text
import net.bewis09.bewisclient.drawable.renderables.components.logic.TextAlign
import net.bewis09.bewisclient.drawable.renderables.components.setting.Fader
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.types.FloatSetting
import net.bewis09.bewisclient.settings.types.Setting
import net.bewis09.bewisclient.util.color.ColorSaver

class ColorFaderSettingRenderable(p: Props<ColorFaderSettingRenderable>) : SettingRenderable<ColorFaderSettingRenderable>(p + {
    height = 35
}) {
    lateinit var title: Translation
    lateinit var setting: Setting<ColorSaver>
    lateinit var types: Array<String>
    lateinit var setting2: FloatSetting
    lateinit var title2: Translation

    init { props() }

    val colorInfoButton = ColorInfoButton {
        state = { setting.get() }
        onChange = setting::set
        types = this@ColorFaderSettingRenderable.types
    }

    val fader = Fader {
        value = { setting2.get() }
        onChange = setting2::set
        precision = setting2.precision
    }

    val resetButton = ResetButton<Nothing> {
        settable = {
            setting.set(null)
            setting2.set(null)
        }
        isDefault = {
            setting.isDefault() && setting2.isDefault()
        }
    }

    override fun init() {
        super.init()
        addRenderable(resetButton.updatePosition(x2 - resetButton.width - 4, y + 4))
        addRenderable(colorInfoButton.updatePosition(x2 - colorInfoButton.width - 8 - resetButton.width, y + 4))
        addRenderable(fader.updatePosition(x2 - fader.width - 8 - resetButton.width, y + 20 - if (General.isMinecrafty) 1 else 0))
        addSettingText { title() }
        addRenderable(Text {
            textProvider = { (title2.getTranslatedString() + ": " + setting2.get()).toText() }
            x = this@ColorFaderSettingRenderable.x2 - fader.width - 12 - resetButton.width
            y = this@ColorFaderSettingRenderable.y + 22
            height = 10
            textAlign = TextAlign.END
        })
    }
}