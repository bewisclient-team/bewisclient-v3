package net.bewis09.bewisclient.impl

import net.bewis09.bewisclient.api.BewisclientAPIEntrypoint
import net.bewis09.bewisclient.core.getColor
import net.bewis09.bewisclient.drawable.Renderable
import net.bewis09.bewisclient.drawable.renderables.options_structure.SidebarCategory
import net.bewis09.bewisclient.game.*
import net.bewis09.bewisclient.impl.functionalities.*
import net.bewis09.bewisclient.impl.settings.BewisclientSettings
import net.bewis09.bewisclient.impl.settings.DefaultWidgetSettings
import net.bewis09.bewisclient.impl.widget.*
import net.bewis09.bewisclient.logic.EventEntrypoint
import net.bewis09.bewisclient.mixin.MixinFunctionImpl
import net.bewis09.bewisclient.settings.Settings
import net.bewis09.bewisclient.settings.SettingsLoader
import net.bewis09.bewisclient.widget.Widget
import net.bewis09.bewisclient.widget.WidgetLoader
import net.minecraft.entity.EntityType
import kotlin.jvm.optionals.getOrNull

class BewisclientSelfAPIEntrypoint : BewisclientAPIEntrypoint() {
    override fun getEventEntrypoints(): List<EventEntrypoint> = listOf(
        WidgetLoader, SettingsLoader, KeybindingImplementer, TranslationLoader, BiomeWidget, SpeedWidget, TiwylaWidget, ShulkerBoxTooltipComponent.Entrypoint, MixinFunctionImpl
    )

    override fun getSettingsObjects(): List<Settings> = listOf(
        BewisclientSettings
    )

    override fun getKeybinds(): List<Keybind> = listOf(
        OpenOptionScreen, Fullbright.ToggleNightVision, Fullbright.ToggleFullbright, Fullbright.IncreaseBrightness, Fullbright.DecreaseBrightness, Zoom.ZoomKeybind, Perspective.EnablePerspective
    )

    override fun getWidgets(): List<Widget> = listOf(
        FPSWidget, BiomeWidget, DayWidget, CoordinatesWidget, DaytimeWidget, PingWidget, CPSWidget, KeyWidget, InventoryWidget, SpeedWidget, TiwylaWidget, CustomWidget, ServerWidget
    )

    override fun getUtilities(): List<Renderable> = listOf(
        Fullbright, BlockHighlight, EntityHighlight, HeldItemTooltip, Zoom, PumpkinOverlay,
        // Crosshair,
        BetterVisibility, Scoreboard, ShulkerBoxTooltip, Perspective
        // Chat Enhancements,
    )

    override fun getGeneralWidgetSettings(): List<Renderable> = listOf(
        DefaultWidgetSettings.gap.createRenderable("widget.gap", "Gap", "Set the gap between widgets in a row"),
        DefaultWidgetSettings.screenEdgeDistance.createRenderable("widget.screen_edge_distance", "Screen Edge Distance", "Set the snapping distance of a widget to the screen edge"),
        DefaultWidgetSettings.backgroundColor.createRenderableWithFader("widget.default_background", "Default Background", "Set the default color and opacity of a widget", DefaultWidgetSettings.backgroundOpacity),
        DefaultWidgetSettings.borderColor.createRenderableWithFader("widget.default_border", "Default Border", "Set the default color and opacity of a widget's border", DefaultWidgetSettings.borderOpacity),
        DefaultWidgetSettings.paddingSize.createRenderable("widget.default_padding_size", "Default Padding Size", "Set the default padding at the edge of a widget to the text"),
        DefaultWidgetSettings.lineSpacing.createRenderable("widget.default_line_spacing", "Default Line Spacing", "Set the default spacing between lines of text in a widget"),
        DefaultWidgetSettings.shadow.createRenderable("widget.default_text_shadow", "Default Text Shadow", "Set whether text in a widget has a shadow by default"),
        DefaultWidgetSettings.textColor.createRenderable("widget.default_text_color", "Default Text Color", "Set the default color of the text in a widget"),
        DefaultWidgetSettings.borderRadius.createRenderable("widget.default_border_radius", "Default Border Radius", "Set the default radius of a widget's border corners"),
        DefaultWidgetSettings.scale.createRenderable("widget.default_scale", "Default Scale", "Set the default scale of a widget"),
    )

    override fun getSidebarCategories(): List<SidebarCategory> = listOf(
        Contact
    )

    override fun getTiwylaEntityExtraInfoProviders(): List<TiwylaWidget.EntityInfoProvider<*>> = listOf(
        TiwylaWidget.EntityInfoProvider(EntityType.CAT) { it.variant?.key?.getOrNull()?.value?.toString() },
        TiwylaWidget.EntityInfoProvider(EntityType.FROG) { it.variant?.key?.getOrNull()?.value?.toString() },
        TiwylaWidget.EntityInfoProvider(EntityType.AXOLOTL) { it.variant?.name },
        TiwylaWidget.EntityInfoProvider(EntityType.HORSE) { it.getColor() + ", " + it.marking.name.lowercase() },
        TiwylaWidget.EntityInfoProvider(EntityType.RABBIT) { it.variant.name.lowercase() },
        TiwylaWidget.EntityInfoProvider(EntityType.LLAMA) { it.variant.name.lowercase() },
    )
}