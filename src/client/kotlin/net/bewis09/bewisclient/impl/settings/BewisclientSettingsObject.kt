package net.bewis09.bewisclient.impl.settings

import net.bewis09.bewisclient.impl.settings.functionalities.BetterVisibilitySettings
import net.bewis09.bewisclient.impl.settings.functionalities.BlockHighlightSettings
import net.bewis09.bewisclient.impl.settings.functionalities.EntityHighlightSettings
import net.bewis09.bewisclient.impl.settings.functionalities.FullbrightSettings
import net.bewis09.bewisclient.impl.settings.functionalities.HeldItemTooltipSettings
import net.bewis09.bewisclient.impl.settings.functionalities.PumpkinOverlaySettings
import net.bewis09.bewisclient.impl.settings.functionalities.ScoreboardSettings
import net.bewis09.bewisclient.impl.settings.functionalities.ZoomSettings
import net.bewis09.bewisclient.settings.types.ObjectSetting

object BewisclientSettingsObject : ObjectSetting() {
    val optionsMenu = create("options_menu", OptionsMenuSettings)
    val widgets = create("widgets", WidgetSettings)
    val fullbright = create("fullbright", FullbrightSettings)
    val blockHighlight = create("block_highlight", BlockHighlightSettings)
    val zoom = create("zoom", ZoomSettings)
    val heldItemTooltip = create("held_item_tooltip", HeldItemTooltipSettings)
    val pumpkinOverlay = create("pumpkin_overlay", PumpkinOverlaySettings)
    val betterVisibility = create("better_visibility", BetterVisibilitySettings)
    val scoreboard = create("scoreboard", ScoreboardSettings)
    val entityHighlight = create("entity_highlight", EntityHighlightSettings)
}
