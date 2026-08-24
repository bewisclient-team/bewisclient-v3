package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.renderables.components.button.ResetButton
import net.bewis09.bewisclient.drawable.renderables.components.setting.Switch
import net.bewis09.bewisclient.drawable.screen_drawing.ScreenDrawing
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.types.Setting

class BooleanSettingRenderable(p: Props<BooleanSettingRenderable>) : SettingRenderable<BooleanSettingRenderable>(p + {
    height = 22
}) {
    lateinit var title: Translation
    lateinit var setting: Setting<Boolean>

    init { props() }

    val switch = Switch {
        state = { setting.get() }
        onChange = setting::set
    }

    val resetButton = ResetButton {
        this.settable = setting
        this.isDefault = setting::isDefault
    }

    override fun init() {
        super.init()
        addRenderable(resetButton.updatePosition(x2 - resetButton.width - 4, y + 4))
        addRenderable(switch.updatePosition(x2 - switch.width - 8 - resetButton.width, y + 5))
        addSettingText { title() }
    }
}