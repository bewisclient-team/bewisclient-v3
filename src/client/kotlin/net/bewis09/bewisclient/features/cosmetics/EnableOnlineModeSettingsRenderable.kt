package net.bewis09.bewisclient.features.cosmetics

import net.bewis09.bewisclient.drawable.draw_methods.SelectiveScreenDrawer
import net.bewis09.bewisclient.drawable.renderables.components.button.Button
import net.bewis09.bewisclient.drawable.renderables.components.button.ResetButton
import net.bewis09.bewisclient.drawable.renderables.components.element.Text
import net.bewis09.bewisclient.drawable.renderables.components.logic.TextAlign
import net.bewis09.bewisclient.drawable.renderables.components.setting.Switch
import net.bewis09.bewisclient.drawable.renderables.settings.SettingRenderable
import net.bewis09.bewisclient.game.translations.Translation
import net.bewis09.bewisclient.server.Authorization
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.settings.types.Setting

class EnableOnlineModeSettingsRenderable(p: Props<EnableOnlineModeSettingsRenderable>) : SettingRenderable<EnableOnlineModeSettingsRenderable>(p + { height = 44 }) {
    lateinit var title: Translation
    lateinit var setting: Setting<Boolean>

    init { props() }

    val switch = Switch{
        state = { setting.get() }
        onChange = { if (!it || General.acceptedEULA()) setting.set(it) else AcceptPrivacyPage.openPrivacyPage() }
    }

    companion object {
        val reloadWarning = Translation("menu.cosmetics.online_mode_reload_warning", "⚠ You need to restart the game for this setting to take effect.")
        val readPrivacyNotice = Translation("menu.cosmetics.read_privacy_notice", "Privacy Notice")
        val needToAccept = Translation("menu.cosmetics.need_to_accept_privacy_notice", "You need to accept the privacy notice to enable online mode.")
    }

    val resetButton = ResetButton {
        settable = setting
        isDefault = setting::isDefault
    }

    override fun init() {
        super.init()
        addRenderable(resetButton.updatePosition(x2 - resetButton.width - 4, y + 4))
        addRenderable(switch.updatePosition(x2 - switch.width - 8 - resetButton.width, y + 5))
        addRenderable(Button {
            text = readPrivacyNotice()
            onClick = { AcceptPrivacyPage.openPrivacyPage() }
        }(x2 - 104, y + height - SelectiveScreenDrawer.getSideButtonHeight() * 2 + 14, 100, SelectiveScreenDrawer.getSideButtonHeight()))
        addRenderable(Text {
            text = title()
        }).updatePosition(x + 8, y + 11)
        if (Authorization.onlineModeEnabled != setting.get()) {
            addRenderable(Text {
                text = reloadWarning()
                color = General.getTextThemeColor() alpha 0.7f
            }).updatePosition(x + 8, y + 22)
        }
        addRenderable(Text {
            text = needToAccept()
            verticalAlign = TextAlign.START
            color = General.getTextThemeColor() alpha 0.7f
        }).updatePosition(x + 8, y + 33)
    }
}