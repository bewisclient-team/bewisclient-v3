package net.bewis09.bewisclient.drawable.renderables.settings

import net.bewis09.bewisclient.drawable.renderables.components.button.ResetButton
import net.bewis09.bewisclient.drawable.renderables.components.setting.Switch
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.settings.types.Setting

class BooleanSettingRenderable(p: Props<BooleanSettingRenderable>) : SettingRenderable<BooleanSettingRenderable>(p + {
    height = 22
}) {
    lateinit var title: Translation
    lateinit var setting: Setting<Boolean>

    init { props() }

    override fun Init.init() {
        ResetButton {
            this.settable = setting
            this.isDefault = setting::isDefault
        }.updatePosition(x2 - 18, y + 4)
        Switch {
            state = { setting.get() }
            onChange = setting::set
        }.updatePosition(x2 - 46, y + 5)
        SettingText {
            textProvider = { title() }
        }
    }
}