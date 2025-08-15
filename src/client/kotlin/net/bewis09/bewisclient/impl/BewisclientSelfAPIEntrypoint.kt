package net.bewis09.bewisclient.impl

import net.bewis09.bewisclient.api.BewisclientAPIEntrypoint
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.option_screen.SidebarCategory
import net.bewis09.bewisclient.game.Keybind
import net.bewis09.bewisclient.game.KeybindingImplementer
import net.bewis09.bewisclient.impl.functionalities.block_highlight.BlockHighlight
import net.bewis09.bewisclient.impl.functionalities.fullbright.Fullbright
import net.bewis09.bewisclient.impl.functionalities.zoom.Zoom
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.bewis09.bewisclient.impl.settings.BewisclientSettings
import net.bewis09.bewisclient.impl.settings.DefaultWidgetSettings
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
        Zoom.ZoomKeybind
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
        Zoom,
        // Pumpkin Overlay,
        // Crosshair,
        // Better Visibility,
        // Scoreboard,
        // Cleaner Debug Menu,
        // Chat Enhancements,
    )

    override fun getDefaultWidgetSettings(): List<Renderable> = listOf(
        DefaultWidgetSettings.backgroundColor.createRenderableWithFader("widget.background", "Background", "Set the default color and opacity of a widget", DefaultWidgetSettings.backgroundOpacity),
        DefaultWidgetSettings.borderColor.createRenderableWithFader("widget.border", "Border", "Set the default color and opacity of a widget's border", DefaultWidgetSettings.borderOpacity),
        DefaultWidgetSettings.paddingSize.createRenderable("widget.padding_size", "Padding Size", "Set the default padding at the edge of a widget to the text"),
        DefaultWidgetSettings.lineSpacing.createRenderable("widget.line_spacing", "Line Spacing", "Set the default spacing between lines of text in a widget"),
        DefaultWidgetSettings.textColor.createRenderable("widget.text_color", "Text Color", "Set the default color of the text in a widget"),
        DefaultWidgetSettings.borderRadius.createRenderable("widget.border_radius", "Border Radius", "Set the default radius of a widget's border corners"),
        DefaultWidgetSettings.scale.createRenderable("widget.scale", "Scale", "Set the default scale of a widget"),
        DefaultWidgetSettings.gap.createRenderable("widget.gap", "Gap", "Set the default gap between widgets in a row"),
    )

    override fun getSidebarCategories(): List<SidebarCategory> = listOf(
        Contact
    )
}