package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.common.toText
import net.bewis09.bewisclient.drawable.renderables.components.button.ResetButton
import net.bewis09.bewisclient.drawable.renderables.components.element.Text
import net.bewis09.bewisclient.drawable.renderables.components.logic.TextAlign
import net.bewis09.bewisclient.drawable.renderables.components.setting.Fader
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.logic.SettingInterfaceWithDefault
import net.bewis09.bewisclient.util.number.Precision

open class FaderSettingRenderable<T : Number, P: FaderSettingRenderable<T, P>>(p: Props<P>) : SettingRenderable<P>(p + {
    height = 22
}) {
    lateinit var title: Translation
    lateinit var setting: SettingInterfaceWithDefault<T>
    lateinit var precision: Precision
    lateinit var parser: (original: Float) -> T

    val fader by lazy {
        Fader {
            value = { setting.get().toFloat() }
            onChange = { value ->
                setting.set(parser(value))
            }
            precision = this@FaderSettingRenderable.precision
        }
    }

    val resetButton by lazy {
        ResetButton {
            settable = setting
            isDefault = { setting.get() == setting.getDefault() }
        }
    }

    override fun init() {
        super.init()
        addRenderable(resetButton.updatePosition(x2 - resetButton.width - 4, y + 4))
        addRenderable(fader.updateWidth(if (this.width > 200) 100 else 50).updatePosition(x2 - fader.width - 8 - resetButton.width, y + 4))
        addSettingText { title() }
        addRenderable(Text {
            textAlign = TextAlign.END
            textProvider = { precision.roundToString(setting.get().toFloat()).toText() }
        }(x2 - fader.width - 12 - resetButton.width, y, width, height))
    }
}