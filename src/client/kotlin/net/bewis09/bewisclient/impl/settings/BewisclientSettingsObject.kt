package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.impl.settings.functionalities.*
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
        create("better_visibility", BetterVisibilitySettings)
        create("scoreboard", ScoreboardSettings)
        create("entity_highlight", EntityHighlightSettings)
        create("perspective", PerspectiveSettings)
    }
}
