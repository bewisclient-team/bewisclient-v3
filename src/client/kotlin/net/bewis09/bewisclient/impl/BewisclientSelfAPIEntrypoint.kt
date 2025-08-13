package net.bewis09.bewisclient.impl

import net.bewis09.bewisclient.api.BewisclientAPIEntrypoint
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.option_screen.SidebarCategory
import net.bewis09.bewisclient.game.Keybind
import net.bewis09.bewisclient.game.KeybindingImplementer
import net.bewis09.bewisclient.impl.functionalities.fullbright.BlockHighlight
import net.bewis09.bewisclient.impl.functionalities.fullbright.Fullbright
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.bewis09.bewisclient.impl.settings.BewisclientSettings
import net.bewis09.bewisclient.impl.widget.*
import net.bewis09.bewisclient.settings.Settings
import net.bewis09.bewisclient.settings.SettingsLoader
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.WidgetLoader

class BewisclientSelfAPIEntrypoint : BewisclientAPIEntrypoint {
    override fun getEventEntrypoints(): List<EventEntrypoint> = listOf(
        WidgetLoader,
        SettingsLoader,
        KeybindingImplementer,
        TranslationLoader,
        BiomeWidget
    )

    override fun getSettingsObjects(): List<Settings> = listOf(
        BewisclientSettings
    )


    override fun getKeybinds(): List<Keybind> = listOf(
        OpenOptionScreen,
        Fullbright.ToggleNightVision,
        Fullbright.ToggleFullbright,
        Fullbright.IncreaseBrightness,
        Fullbright.DecreaseBrightness,
    )


    override fun getWidgets(): List<Widget> = listOf(
        FPSWidget,
        BiomeWidget,
        DayWidget,
        CoordinatesWidget,
        DaytimeWidget,
        PingWidget,
        CPSWidget
    )


    override fun getUtilities(): List<Renderable> = listOf(
        Fullbright,
        BlockHighlight,
        // Entity Highlight,
        // Held Item Info,
        // Zoom,
        // Pumpkin Overlay,
        // Crosshair,
        // Better Visibility,
        // Scoreboard,
        // Cleaner Debug Menu,
        // Chat Enhancements,
    )

    override fun getDefaultWidgetSettings(): List<Renderable> = listOf(
        getSettings().widgetSettings.defaults.paddingSize.createRenderable("widget.padding_size", "Padding Size"),
        getSettings().widgetSettings.defaults.lineSpacing.createRenderable("widget.line_spacing", "Line Spacing"),
        getSettings().widgetSettings.defaults.borderRadius.createRenderable("widget.border_radius", "Border Radius"),
        getSettings().widgetSettings.defaults.scale.createRenderable("widget.scale", "Scale"),
        getSettings().widgetSettings.defaults.gap.createRenderable("widget.gap", "Gap"),
    )

    override fun getSidebarCategories(): List<SidebarCategory> = listOf(
        Contact
    )
}