package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.impl.settings.functionalities.BlockHighlightSettings
import net.bewis09.bewisclient.impl.settings.functionalities.FullbrightSettings
import net.bewis09.bewisclient.impl.settings.functionalities.HeldItemTooltipSettings
import net.bewis09.bewisclient.impl.settings.functionalities.PumpkinOverlaySettings
import net.bewis09.bewisclient.impl.settings.functionalities.ZoomSettings
import net.bewis09.bewisclient.settings.types.ObjectSetting

object BewisclientSettingsObject : ObjectSetting() {
    init {
        create("options_menu", OptionsMenuSettings)
        create("widgets", WidgetSettings)
        create("fullbright", FullbrightSettings)
        create("block_highlight", BlockHighlightSettings)
        create("zoom", ZoomSettings)
        create("held_item_tooltip", HeldItemTooltipSettings)
        create("pumpkin_overlay", PumpkinOverlaySettings)
    }
}